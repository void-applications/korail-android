package org.personal.korail_android.service

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import org.personal.korail_android.R
import org.personal.korail_android.item.ChatData
import org.personal.korail_android.item.LocalStoredChatRoom
import org.personal.korail_android.utils.SharedPreferenceHelper
import org.personal.korail_android.utils.serverConnection.HTTPRequest
import java.lang.Integer.parseInt

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = javaClass.name

    companion object {
        const val ACTION_RECEIVE_CHAT = "INTENT_ACTION_RECEIVE_CHAT"
    }

    /* 새로운 토큰을 파이어베이스로부터 받으면 shared preference 에 저장 -> 서버에 userColumnID 와 함께 저장하기 위해 대기
    TODO: 다음 과정 하기
    1. 회원 가입 완료 시 -> 서버 데이터 베이스에 userColumnID 와 함께 저장
    2. 로그인 시 -> 서버 데이터 베이스에 userColumnID 수정
     */
    override fun onNewToken(token: String) {

        Log.i(TAG, "onNewToken : $token")
        // 새로운 토큰을 받으면 shared preference 에 저장해 놓고, 회원가입이 완료 되거나 로그인을 했을 시에 DB 에 업데이트 해준다.
        SharedPreferenceHelper.setString(this, getText(R.string.firebaseMessagingToken).toString(), token)
        // FCM 토큰 값 업데이트해야 한다는 것을 확인하는 변수 저장
        SharedPreferenceHelper.setBoolean(this, getText(R.string.isTokenUpdated).toString(), false)
        uploadTokenToServer(token)
    }

    private fun uploadTokenToServer(token:String) {
        val postData = JSONObject().apply {
            put("what", "uploadToken")
            put("token", token)
        }

        val httpRequest = HTTPRequest("korail-chat")
        val tokenTableId = parseInt(httpRequest.postMethodToServer(postData.toString()))


        // 데이터 베이스에 저장된 테이블 id 저장하기 -> 각 디바이스의 id 값으로 사용 (채팅 시 본인 채팅과 상대방 채팅 구분하기 위해)
        SharedPreferenceHelper.setInt(this, getText(R.string.tokenId).toString(), tokenTableId)
        // FCM 토큰 값 업데이트 됬다는 것을 확인하는 변수 저장
        SharedPreferenceHelper.setBoolean(this, getText(R.string.isTokenUpdated).toString(), true)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i(TAG, "onMessageReceived: ${remoteMessage.data["message"]}")
        Log.i(TAG, "onMessageReceived: ${remoteMessage.data["senderId"]}")

        handleMessage(remoteMessage.data)
    }

    // 채팅 액티비티로 전송하는 데이터
    private fun handleMessage(fcmData: Map<String, String>) {
        val station = fcmData["station"]
        val senderId = parseInt(fcmData["senderId"]!!)
        val senderName = fcmData["senderName"]
        val message = fcmData["message"]
        val messageTime = fcmData["messageTime"]
        val chatMessage = ChatData(senderId, senderName, message, messageTime) // 로컬에 저장할 메시지

        // 로컬 브로드캐스트에 메시지 전송 -> 채팅 액티비티에서 메시지 받음
        val toChat = Intent().apply {
            action = ACTION_RECEIVE_CHAT
            putExtra("station", station)
            putExtra("senderId", senderId)
            putExtra("senderName", senderName)
            putExtra("message", message)
            putExtra("messageTime", messageTime)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(toChat)

        Log.i(TAG, "handleMessage: $chatMessage")
        storeChatMessage(station!!, chatMessage)
    }

    private fun storeChatMessage(station: String, chatMessage: ChatData) {
        val gson = Gson()
        val myFirebaseTokenId = SharedPreferenceHelper.getInt(this, getText(R.string.tokenId).toString())
        val isMyMessage = isMyMessage(myFirebaseTokenId, chatMessage.sender_id!!)
        val jsonChatRoomList = SharedPreferenceHelper.getString(this, getText(R.string.chatData).toString())
        // station : 역이름 , 읽었는지 안읽었는지,chattingList
        val chatRoomList = gson.fromJson<ArrayList<LocalStoredChatRoom>>(jsonChatRoomList, object : TypeToken<ArrayList<LocalStoredChatRoom>>() {}.type)
        var isUploaded = false

        Log.i(TAG, "storeChatMessage: $myFirebaseTokenId")
        if (isMyMessage != null) {
            // 쉐어드에 저장이 되어 있을 때
            if (chatRoomList != null) {
                for (i in 0..chatRoomList.size - 1) {
                    val localChatRoom = chatRoomList[i]

                    if (localChatRoom.station == station) {
                        Log.i(TAG, "storeChatMessage: 같은 역")

                        if (!isMyMessage) localChatRoom.unReadChatCount += 1
                        Log.i(TAG, "storeChatMessage: ${localChatRoom.unReadChatCount}")
                        localChatRoom.chatMessageList.add(chatMessage)
                        isUploaded = true
                        break
                    }
                }

                if (!isUploaded) {
                    Log.i(TAG, "storeChatMessage: 같은 역이 없음")
                    if (!isMyMessage) {
                        val chatMessageList = ArrayList<ChatData>()
                        chatMessageList.add(chatMessage)

                        val newChatRoomSet = if (isMyMessage) {
                            LocalStoredChatRoom(station, 0, chatMessageList)
                        } else {
                            LocalStoredChatRoom(station, 1, chatMessageList)
                        }
                        chatRoomList.add(newChatRoomSet)
                    }
                }
                Log.i(TAG, "storeChatMessage: ${chatRoomList}")
                storeChatInPreference(gson, chatRoomList)

                // 쉐어드에 저장이 안되어 있을 때
            } else {
                Log.i(TAG, "storeChatMessage: 쉐어드에 아예 저장이 되어있지 않을 떄")

                val newChatRoomList = ArrayList<LocalStoredChatRoom>()
                val chatMessageList = ArrayList<ChatData>()
                chatMessageList.add(chatMessage)

                val newChatRoomSet = if (isMyMessage) {
                    LocalStoredChatRoom(station, 0, chatMessageList)
                } else {
                    LocalStoredChatRoom(station, 1, chatMessageList)
                }

                newChatRoomList.add(newChatRoomSet)

                storeChatInPreference(gson, newChatRoomList)
            }
        }
    }

    private fun storeChatInPreference(gson: Gson, chatRoomList: ArrayList<LocalStoredChatRoom>) {
        val jsonNewChatRoomSet = gson.toJson(chatRoomList)
        SharedPreferenceHelper.setString(this, getText(R.string.chatData).toString(), jsonNewChatRoomSet)
    }

    private fun isMyMessage(myFirebaseTokenId: Int?, senderId: Int): Boolean? {
        var isMyMessage: Boolean? = null

        if (myFirebaseTokenId != null) {
            isMyMessage = myFirebaseTokenId == senderId
        }

        return isMyMessage
    }
}


