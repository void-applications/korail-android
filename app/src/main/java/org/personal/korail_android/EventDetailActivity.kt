package org.personal.korail_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.personal.korail_android.item.EventItem

class EventDetailActivity : AppCompatActivity() {

    private val eventItem by lazy { intent.getParcelableExtra<EventItem>("eventItem") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        setInitView()
    }

    private fun setInitView() {
        locationIV.setImageResource(eventItem!!.locationImage)
        stationNameTV.text = eventItem!!.location
        performerTV.text = eventItem!!.performer
        contentTV.text = eventItem!!.content
        startTimeTV.text = String.format("시작시간 : %s", eventItem!!.start_time)
        endTimeTV.text = String.format("종료시간 : %s", eventItem!!.end_time)
    }
}