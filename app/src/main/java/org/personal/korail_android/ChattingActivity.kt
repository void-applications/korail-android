package org.personal.korail_android

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chatting.*
import org.json.JSONObject
import org.personal.korail_android.`interface`.HTTPConnectionListener
import org.personal.korail_android.adapter.ChatAdapter
import org.personal.korail_android.background.HTTPConnectionThread.Companion.REQUEST_SIMPLE_POST_METHOD
import org.personal.korail_android.data.ChatData
import org.personal.korail_android.service.HTTPConnectionService
import org.personal.korail_android.service.MyFirebaseMessagingService.Companion.ACTION_RECEIVE_CHAT
import org.personal.korail_android.utils.SharedPreferenceHelper
import java.lang.Integer.parseInt

class ChattingActivity : AppCompatActivity(), View.OnClickListener, HTTPConnectionListener {

    private val TAG = javaClass.name
    private val serverPage = "korail-chat"

    // --- http 통신관련 변수 모음 ---
    private lateinit var httpConnectionService: HTTPConnectionService
    private val UPLOAD_FIREBASE_TOKEN = 1
    private val SEND_MESSAGE = 2

    // --- FCM 으로 받은 채팅 메시지를 액티비티에서 수신하는 로컬 리시버 ---
    private lateinit var broadcastReceiver: BroadcastReceiver
    private val intentFilter by lazy { IntentFilter().apply { addAction(ACTION_RECEIVE_CHAT) } }

    // 채팅 리사이클러뷰
    private val tokenTableId by lazy { SharedPreferenceHelper.getInt(this, getText(R.string.tokenId).toString()) }
    private val chattingList by lazy { ArrayList<ChatData>() }
    private val chatAdapter: ChatAdapter by lazy { ChatAdapter(this, tokenTableId, chattingList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)
        setListener()
        defineReceiver()
        buildRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        startBoundService()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    private fun setListener() {
        sendBtn.setOnClickListener(this)
    }

    private fun buildRecyclerView() {
        val layoutManager = LinearLayoutManager(this)

        chattingBoxRV.setHasFixedSize(true)
        chattingBoxRV.layoutManager = layoutManager
        chattingBoxRV.adapter = chatAdapter
    }

    private fun defineReceiver() {

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.i(TAG, "working?")
                when (intent?.action) {
                    ACTION_RECEIVE_CHAT -> {
                        val senderId = intent.getIntExtra("senderId", 0)
                        val message = intent.getStringExtra("message")
                        val chatData = ChatData(senderId, message, "3030")
                        chattingList.add(chatData)
                        chatAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    // 현재 액티비티와 HTTPConnectionService(Bound Service)를 연결하는 메소드
    private fun startBoundService() {
        val startService = Intent(this, HTTPConnectionService::class.java)
        bindService(startService, connection, BIND_AUTO_CREATE)
    }

    // -------------------------- 버튼 클릭 리스너 관련 이벤트 --------------------------
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sendBtn -> sendMessage()
        }
    }

    private fun sendMessage() {
        // 빈칸이 아닐 때만 메시지를 보냄
        val chatInput = chattingInputED.text.toString()

        if (chatInput.trim() != "") {
            val postData = JSONObject()
            postData.put("what", "sendMessage")
            postData.put("senderId", tokenTableId)
            postData.put("message", chatInput)

            httpConnectionService.serverPostRequest(serverPage, postData.toString(), REQUEST_SIMPLE_POST_METHOD, SEND_MESSAGE)
            chattingInputED.text = null
        }
    }

    // Memo : BoundService 의 IBinder 객체를 받아와 현재 액티비티에서 서비스의 메소드를 사용하기 위한 클래스
    /*
    바운드 서비스에서는 HTTPConnectionThread(HandlerThread)가 동작하고 있으며, 이 스레드에 메시지를 통해 서버에 요청을 보낸다
    서버에서 결과를 보내주면 HTTPConnectionThread(HandlerThread)의 인터페이스 메소드 -> 바운드 서비스 -> 바운드 서비스 인터페이스 -> 액티비티 onHttpRespond 에서 handle 한다
     */
    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: HTTPConnectionService.LocalBinder =
                service as HTTPConnectionService.LocalBinder
            httpConnectionService = binder.getService()!!
            httpConnectionService.setOnHttpRespondListener(this@ChattingActivity)
            uploadFirebaseToken()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i(TAG, "바운드 서비스 연결 종료")
        }

        // token 업로드 하는 메소드 : 이미 업로드가 되어있다면 동작 X
        private fun uploadFirebaseToken() {
            // 토큰 값 바뀌었는지 확인하는 변수
            val isTokenUpdated = SharedPreferenceHelper.getBoolean(this@ChattingActivity, getText(R.string.isTokenUpdated).toString())

            if (!isTokenUpdated) {
                Log.i(TAG, "test: not working?")
                val token = SharedPreferenceHelper.getString(this@ChattingActivity, getText(R.string.firebaseMessagingToken).toString())
                val postData = JSONObject()
                postData.put("what", "uploadToken")
                postData.put("token", token)

                httpConnectionService.serverPostRequest(serverPage, postData.toString(), REQUEST_SIMPLE_POST_METHOD, UPLOAD_FIREBASE_TOKEN)
            }
        }
    }

    // http 통신 결과를 handling 하는 인터페이스 메소드
    override fun onHttpRespond(responseData: HashMap<*, *>) {
        when (responseData["whichRespond"] as Int) {
            UPLOAD_FIREBASE_TOKEN -> {
                Log.i(TAG, "onHttpRespond: ${responseData["respondData"]}")
                val tokenTableId = parseInt(responseData["respondData"].toString()) // token 테이블 id 값

                // 데이터 베이스에 저장된 테이블 id 저장하기 -> 각 디바이스의 id 값으로 사용 (채팅 시 본인 채팅과 상대방 채팅 구분하기 위해)
                SharedPreferenceHelper.setInt(this, getText(R.string.tokenId).toString(), tokenTableId)
                // FCM 토큰 값 업데이트 됬다는 것을 확인하는 변수 저장
                SharedPreferenceHelper.setBoolean(this, getText(R.string.isTokenUpdated).toString(), true)
            }

            SEND_MESSAGE -> {

            }
        }
    }
}