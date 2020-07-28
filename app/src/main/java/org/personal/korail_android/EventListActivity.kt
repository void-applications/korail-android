package org.personal.korail_android

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_event_list.*
import kotlinx.android.synthetic.main.activity_event_list.clearSearchInputIB
import kotlinx.android.synthetic.main.activity_event_list.searchStationET
import org.personal.korail_android.adapter.EventAdapter
import org.personal.korail_android.interfaces.ItemClickListener
import org.personal.korail_android.item.EventItem

class EventListActivity : AppCompatActivity(), ItemClickListener, View.OnClickListener, TextWatcher {

    private val eventList by lazy { ArrayList<EventItem>() }
    private val eventAdapter by lazy { EventAdapter(this, eventList, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        setListener()
        buildRecyclerView()
    }

    private fun setListener() {
        clearSearchInputIB.setOnClickListener(this)
        searchStationET.addTextChangedListener(this)
    }

    private fun buildRecyclerView() {
        val layoutManager = LinearLayoutManager(this)

        // 이벤트 더미 아이템 테스트
        // TODO : 지우기
        val eventItem = EventItem(R.drawable.location, "인디밴드", "남성역", "2020-03-30", "2020-03-30")
        eventList.add(eventItem)

        stationEventRV.setHasFixedSize(true)
        stationEventRV.layoutManager = layoutManager
        stationEventRV.adapter = eventAdapter
    }

    override fun onItemClick(view: View?, itemPosition: Int) {

    }

    override fun onItemLongClick(view: View?, itemPosition: Int) {

    }

    // -------------------------- 버튼 클릭 리스너 관련 이벤트 --------------------------
    override fun onClick(p0: View?) {
    }

    // -------------------------- 검색 EditText 리스너 관련 이벤트 --------------------------
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {
    }
}