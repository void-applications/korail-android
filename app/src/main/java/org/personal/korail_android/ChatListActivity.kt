package org.personal.korail_android

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_chat_list.*
import org.json.JSONObject
import org.personal.korail_android.adapter.ChatRoomAdapter
import org.personal.korail_android.background.HTTPConnectionThread
import org.personal.korail_android.dialog.ChooseChatNameDialog
import org.personal.korail_android.interfaces.HTTPConnectionListener
import org.personal.korail_android.interfaces.ItemClickListener
import org.personal.korail_android.item.ChatRoomItem
import org.personal.korail_android.item.LocalStoredChatRoom
import org.personal.korail_android.service.HTTPConnectionService
import org.personal.korail_android.service.MyFirebaseMessagingService
import org.personal.korail_android.utils.SharedPreferenceHelper

class ChatListActivity : AppCompatActivity(), View.OnClickListener, ItemClickListener, TextWatcher, ChooseChatNameDialog.DialogListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val TAG = javaClass.name
//    private val serverPage = "korail-chat" // TODO : 서버 페이지 바꾸기

//    private lateinit var httpConnectionService: HTTPConnectionService
//    private val UPLOAD_FIREBASE_TOKEN = 1

    private val chatRoomList by lazy { ArrayList<ChatRoomItem>() }
    private var changedChatRoomList = ArrayList<ChatRoomItem>()
    private val chatRoomAdapter by lazy { ChatRoomAdapter(this, chatRoomList, this) }

    // --- FCM 으로 받은 채팅 메시지를 액티비티에서 수신하는 로컬 리시버 ---
    private lateinit var broadcastReceiver: BroadcastReceiver
    private val intentFilter by lazy { IntentFilter().apply { addAction(MyFirebaseMessagingService.ACTION_RECEIVE_CHAT) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)
        setListener()
        defineReceiver()
//        SharedPreferenceHelper.setString(this, getText(R.string.chatData).toString(), null)
    }

    override fun onStart() {
        super.onStart()
        chatRoomList.clear()
        buildRecyclerView()
//        startBoundService()
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationBV.selectedItemId = R.id.chat
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onStop() {
        super.onStop()
//        unbindService(connection)
    }

    private fun setListener() {
        clearSearchInputIB.setOnClickListener(this)
        searchStationET.addTextChangedListener(this)
        bottomNavigationBV.setOnNavigationItemSelectedListener(this) // 바텀 네비게이션 리스너
    }

    private fun buildRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        val gson = Gson()
        val jsonChatRoomList = SharedPreferenceHelper.getString(this, getText(R.string.chatData).toString())
        val storedChatRoomList = gson.fromJson<ArrayList<LocalStoredChatRoom>>(jsonChatRoomList, object : TypeToken<ArrayList<LocalStoredChatRoom>>() {}.type)
        val storedUnreadMessageHash = HashMap<String, Any?>()

        if (storedChatRoomList != null) {
            Log.i(TAG, "buildRecyclerView: ${storedChatRoomList.count()}")
            storedChatRoomList.forEach {
                val station = it.station
                storedUnreadMessageHash[station] = it.unReadChatCount
            }
        }

        // 채팅 방 더미 데이터 넣는 구간
        val dummyStationTitles = arrayOf("대전역", "사당역", "노원역", "이수역", "선릉역", "동대문역사문화공원역")
        dummyStationTitles.forEach {

            val chatRoomItem: ChatRoomItem = if (storedUnreadMessageHash[it] == null) {
                ChatRoomItem(it, 0)

            } else {
                ChatRoomItem(it, storedUnreadMessageHash[it] as Int)
            }
            chatRoomList.add(chatRoomItem)
        }
        stationListRV.setHasFixedSize(true)
        stationListRV.layoutManager = layoutManager
        stationListRV.adapter = chatRoomAdapter
    }

    // 파이어베이스 서비스에서 브로드캐스트하는 채팅 메시지를 받는다.
    private fun defineReceiver() {

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.i(TAG, "working?")
                when (intent?.action) {
                    MyFirebaseMessagingService.ACTION_RECEIVE_CHAT -> {
                        Log.i(TAG, "onReceive: yes")
                        val senderStation = intent.getStringExtra("station")
                        chatRoomList.forEach {
                            if (it.title == senderStation) {
                                Log.i(TAG, "onReceive: same station")
                                it.unReadMessage += 1
                                chatRoomAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
    }

//    // 현재 액티비티와 HTTPConnectionService(Bound Service)를 연결하는 메소드
//    private fun startBoundService() {
//        val startService = Intent(this, HTTPConnectionService::class.java)
//        bindService(startService, connection, BIND_AUTO_CREATE)
//    }

    // -------------------------- 네비게이션 리스너 관련 이벤트 --------------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val toHome = Intent(this, HomeActivity::class.java)
                startActivity(toHome)
            }

            R.id.event -> {
                val toEvent = Intent(this, EventListActivity::class.java)
                startActivity(toEvent)
            }

            R.id.culturalFacilities -> {
                val toCulturalFacilities = Intent(this, CulturalFacilitiesListActivity::class.java)
                startActivity(toCulturalFacilities)
            }

            R.id.lostAndFound -> {
                val toLostAndFound = Intent(this, LostAndFoundSearch::class.java)
                startActivity(toLostAndFound)
            }
        }
        overridePendingTransition(0, 0)
        return true
    }

    // -------------------------- 버튼 클릭 리스너 관련 이벤트 --------------------------
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.clearSearchInputIB -> {
                searchStationET.text = null
            }
        }
    }

    // -------------------------- 리사이클러 뷰 관련 이벤트 --------------------------
    override fun onItemClick(view: View?, itemPosition: Int) {
        val chooseChatNameDialog = ChooseChatNameDialog()
        val arguments = Bundle().apply {
            putString("station", getSelectedStation(itemPosition))
        }
        chooseChatNameDialog.arguments = arguments
        chooseChatNameDialog.show(supportFragmentManager, "chooseChatNameDialog")
    }

    // 검색하게되면 어뎁터의 list 가 changedChatRoomList 로 변경되기 떄문에 예외처리하여 방의 이름을 반환
    private fun getSelectedStation(itemPosition: Int): String {
        val searchText = searchStationET.text.toString()

        return if (searchText != "") changedChatRoomList[itemPosition].title
        else chatRoomList[itemPosition].title
    }

    override fun onItemLongClick(view: View?, itemPosition: Int) {}

    // 사용자 이름 정하는 다이얼로그 관련 이벤트

    override fun onChooseName(station: String, chosenName: String) {
        val toChatting = Intent(this, ChattingActivity::class.java).apply {
            putExtra("station", station)
            putExtra("userName", chosenName)
        }
        startActivity(toChatting)
    }

    // -------------------------- 검색 관련 이벤트 --------------------------
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(text: Editable?) {
        searchFilter(text.toString())
    }

    private fun searchFilter(text: String) {
        changedChatRoomList.clear()

        chatRoomList.forEach {
            if (it.title.toLowerCase().contains(text.toLowerCase())) {
                changedChatRoomList.add(it)
            }
        }
        chatRoomAdapter.filterList(changedChatRoomList)
    }


    // Memo : BoundService 의 IBinder 객체를 받아와 현재 액티비티에서 서비스의 메소드를 사용하기 위한 클래스
    /*
    바운드 서비스에서는 HTTPConnectionThread(HandlerThread)가 동작하고 있으며, 이 스레드에 메시지를 통해 서버에 요청을 보낸다
    서버에서 결과를 보내주면 HTTPConnectionThread(HandlerThread)의 인터페이스 메소드 -> 바운드 서비스 -> 바운드 서비스 인터페이스 -> 액티비티 onHttpRespond 에서 handle 한다
     */
//    private val connection: ServiceConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName, service: IBinder) {
//            val binder: HTTPConnectionService.LocalBinder =
//                service as HTTPConnectionService.LocalBinder
//            httpConnectionService = binder.getService()!!
//            httpConnectionService.setOnHttpRespondListener(this@ChatListActivity)
//            uploadFirebaseToken()
//        }
//
//        override fun onServiceDisconnected(name: ComponentName) {
//            Log.i(TAG, "바운드 서비스 연결 종료")
//        }
//
//        // token 업로드 하는 메소드 : 이미 업로드가 되어있다면 동작 X
//        private fun uploadFirebaseToken() {
//            // 토큰 값 바뀌었는지 확인하는 변수
//            val isTokenUpdated = SharedPreferenceHelper.getBoolean(this@ChatListActivity, getText(R.string.isTokenUpdated).toString())
//            Log.i(TAG, "uploadFirebaseToken: $isTokenUpdated")
//            if (!isTokenUpdated) {
//                Log.i(TAG, "test: not working?")
//                val token = SharedPreferenceHelper.getString(this@ChatListActivity, getText(R.string.firebaseMessagingToken).toString())
//                val postData = JSONObject()
//                postData.put("what", "uploadToken")
//                postData.put("token", token)
//
//                httpConnectionService.serverPostRequest(serverPage, postData.toString(), HTTPConnectionThread.REQUEST_SIMPLE_POST_METHOD, UPLOAD_FIREBASE_TOKEN)
//            }
//        }
//    }
//
//    // http 통신 결과를 handling 하는 인터페이스 메소드
//    override fun onHttpRespond(responseData: HashMap<*, *>) {
//        when (responseData["whichRespond"] as Int) {
//            UPLOAD_FIREBASE_TOKEN -> {
//                Log.i(TAG, "onHttpRespond: ${responseData["respondData"]}")
//                val tokenTableId = Integer.parseInt(responseData["respondData"].toString()) // token 테이블 id 값
//
//                // 데이터 베이스에 저장된 테이블 id 저장하기 -> 각 디바이스의 id 값으로 사용 (채팅 시 본인 채팅과 상대방 채팅 구분하기 위해)
//                SharedPreferenceHelper.setInt(this, getText(R.string.tokenId).toString(), tokenTableId)
//                // FCM 토큰 값 업데이트 됬다는 것을 확인하는 변수 저장
//                SharedPreferenceHelper.setBoolean(this, getText(R.string.isTokenUpdated).toString(), true)
//            }
//        }
//    }
}