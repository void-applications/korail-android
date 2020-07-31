package org.personal.korail_android

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import org.personal.korail_android.adapter.ChatRoomAdapter
import org.personal.korail_android.dialog.ChooseChatNameDialog
import org.personal.korail_android.interfaces.ItemClickListener
import org.personal.korail_android.item.ChatRoomItem
import org.personal.korail_android.item.LocalStoredChatRoom
import org.personal.korail_android.service.MyFirebaseMessagingService
import org.personal.korail_android.utils.SharedPreferenceHelper

class ChatListActivity : AppCompatActivity(), View.OnClickListener, ItemClickListener, TextWatcher, ChooseChatNameDialog.DialogListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val TAG = javaClass.name

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
    }

    override fun onStart() {
        super.onStart()
        chatRoomList.clear()
        buildRecyclerView()
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

    // -------------------------- 네비게이션 리스너 관련 이벤트 --------------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val toHome = Intent(this, HomeActivity::class.java)
                startActivity(toHome)
            }

            R.id.hiddenRestArea -> {
                val toHiddenRestArea = Intent(this, HiddenRestAreaListActivity::class.java)
                startActivity(toHiddenRestArea)
            }

            R.id.lostAndFound -> {
                val toLostAndFound = Intent(this, LostAndFoundSearch::class.java)
                startActivity(toLostAndFound)
            }

            R.id.facilities -> {
                val toLostAndFound = Intent(this, FacilitiesActivity::class.java)
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
}