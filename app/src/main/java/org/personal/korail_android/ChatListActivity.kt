package org.personal.korail_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_chat_list.*
import org.personal.korail_android.adapter.ChatRoomAdapter
import org.personal.korail_android.dialog.ChooseChatNameDialog
import org.personal.korail_android.interfaces.ItemClickListener
import org.personal.korail_android.item.ChatRoomItem

class ChatListActivity : AppCompatActivity(), View.OnClickListener, ItemClickListener, TextWatcher, ChooseChatNameDialog.DialogListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val TAG = javaClass.name
    private val chatRoomList by lazy { ArrayList<ChatRoomItem>() }
    private var changedChatRoomList = ArrayList<ChatRoomItem>()
    private val chatRoomAdapter by lazy { ChatRoomAdapter(this, chatRoomList, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)
        setListener()
        buildRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationBV.selectedItemId = R.id.chat
    }

    private fun setListener() {
        clearSearchInputIB.setOnClickListener(this)
        searchStationET.addTextChangedListener(this)
        bottomNavigationBV.setOnNavigationItemSelectedListener(this) // 바텀 네비게이션 리스너
    }

    private fun buildRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        // 채팅 방 더미 데이터 넣는 구간
        val dummyStationTitles = arrayOf("남성역", "서울역", "seoul", "nine")
        dummyStationTitles.forEach {
            val chatRoomItem = ChatRoomItem(it, 0)
            chatRoomList.add(chatRoomItem)
        }
        stationListRV.setHasFixedSize(true)
        stationListRV.layoutManager = layoutManager
        stationListRV.adapter = chatRoomAdapter
    }

    // -------------------------- 네비게이션 리스너 관련 이벤트 --------------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home ->   {
                val toHome  = Intent(this, HomeActivity::class.java)
                startActivity(toHome)
            }

            R.id.event -> {
                val toEvent  = Intent(this, EventListActivity::class.java)
                startActivity(toEvent)
            }

            R.id.culturalFacilities -> {
                val toCulturalFacilities  = Intent(this, CulturalFacilitiesListActivity::class.java)
                startActivity(toCulturalFacilities)
            }

            R.id.lostAndFound -> {
                val toLostAndFound  = Intent(this, lostAndFoundSearch::class.java)
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