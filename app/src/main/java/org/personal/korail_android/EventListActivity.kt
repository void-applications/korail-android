package org.personal.korail_android

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_event_list.*
import kotlinx.android.synthetic.main.activity_event_list.clearSearchInputIB
import kotlinx.android.synthetic.main.activity_event_list.searchStationET
import org.personal.korail_android.adapter.EventAdapter
import org.personal.korail_android.background.HTTPConnectionThread.Companion.REQUEST_EVENT_LIST
import org.personal.korail_android.interfaces.HTTPConnectionListener
import org.personal.korail_android.interfaces.ItemClickListener
import org.personal.korail_android.item.EventItem
import org.personal.korail_android.service.HTTPConnectionService

class EventListActivity : AppCompatActivity(), ItemClickListener, View.OnClickListener, TextWatcher, HTTPConnectionListener {

    private val TAG = javaClass.name
    private val serverPage = "korail-event"

    private lateinit var httpConnectionService: HTTPConnectionService
    private val GET_EVENT_ITEM_LIST = 1

    private val eventList by lazy { ArrayList<EventItem>() }
    private var changedEventList = ArrayList<EventItem>()
    private val eventAdapter by lazy { EventAdapter(this, eventList, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        setListener()
        buildRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        eventList.clear()
        startBoundService()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    private fun setListener() {
        clearSearchInputIB.setOnClickListener(this)
        searchStationET.addTextChangedListener(this)
        clearSearchInputIB.setOnClickListener(this)
    }

    private fun buildRecyclerView() {
        val layoutManager = LinearLayoutManager(this)

        stationEventRV.setHasFixedSize(true)
        stationEventRV.layoutManager = layoutManager
        stationEventRV.adapter = eventAdapter
    }

    // 현재 액티비티와 HTTPConnectionService(Bound Service)를 연결하는 메소드
    private fun startBoundService() {
        val startService = Intent(this, HTTPConnectionService::class.java)
        bindService(startService, connection, BIND_AUTO_CREATE)
    }

    // -------------------------- 버튼 클릭 리스너 관련 이벤트 --------------------------
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.clearSearchInputIB -> {
                searchStationET.text = null
            }
        }
    }

    // -------------------------- 리사이클러 뷰 아이템 클릭 리스너 관련 이벤트 --------------------------
    //TODO : eventItem 마무리 하기
    override fun onItemClick(view: View?, itemPosition: Int) {
        val eventItem = getSelectedStation(itemPosition)
        if (eventItem.progressState == "공연 예정") {
            Log.i(TAG, "onItemClick: 공연 예정")
            val toEventDetail = Intent(this, EventDetailActivity::class.java).apply {
                putExtra("eventItem", eventItem)
            }
            startActivity(toEventDetail)
        } else {
            val toEventDetailWithReview = Intent(this, EventDetailWithReviewActivity::class.java).apply {
                putExtra("eventItem", eventItem)
            }
            startActivity(toEventDetailWithReview)
        }
    }

    override fun onItemLongClick(view: View?, itemPosition: Int) {

    }

    // 검색하게되면 어뎁터의 list 가 changedChatRoomList 로 변경되기 떄문에 예외처리하여 방의 이름을 반환
    private fun getSelectedStation(itemPosition: Int): EventItem {
        val searchText = searchStationET.text.toString()

        return if (searchText != "") changedEventList[itemPosition]
        else eventList[itemPosition]
    }

    // -------------------------- 검색 EditText 리스너 관련 이벤트 --------------------------
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(text: Editable?) {
        searchFilter(text.toString())
    }

    @SuppressLint("DefaultLocale")
    // 컨텐츠, 공연자명, 위치 결과를 모두 보여준다
    private fun searchFilter(text: String) {
        changedEventList.clear()

        eventList.forEach {
            if (it.performer!!.toLowerCase().contains(text.toLowerCase())
                || it.location!!.toLowerCase().contains(text.toLowerCase())
                || it.content!!.toLowerCase().contains(text.toLowerCase())
            ) {
                changedEventList.add(it)
            }
        }
        eventAdapter.filterList(changedEventList)
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
            httpConnectionService.setOnHttpRespondListener(this@EventListActivity)

            httpConnectionService.serverGetRequest(formRequestUrl(), REQUEST_EVENT_LIST, GET_EVENT_ITEM_LIST)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i(TAG, "바운드 서비스 연결 종료")
        }
    }

    fun formRequestUrl(): String {
        return "$serverPage?what=getEventList"
    }

    // http 통신 결과를 handling 하는 인터페이스 메소드
    override fun onHttpRespond(responseData: HashMap<*, *>) {
        val handler = Handler(Looper.getMainLooper())
        when (responseData["whichRespond"] as Int) {
            GET_EVENT_ITEM_LIST -> {
                Log.i(TAG, "onHttpRespond: ${responseData["respondData"]}")
                val responseEvenList = responseData["respondData"] as ArrayList<EventItem>
                responseEvenList.forEach { eventList.add(it) }

                handler.post { eventAdapter.notifyDataSetChanged() }
            }
        }
    }
}