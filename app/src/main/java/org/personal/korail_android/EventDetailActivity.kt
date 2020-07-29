package org.personal.korail_android

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.personal.korail_android.adapter.EventReviewAdapter
import org.personal.korail_android.background.HTTPConnectionThread.Companion.REQUEST_EVENT_REVIEW_LIST
import org.personal.korail_android.interfaces.HTTPConnectionListener
import org.personal.korail_android.item.EventItem
import org.personal.korail_android.item.EventReviewItem
import org.personal.korail_android.service.HTTPConnectionService

class EventDetailActivity : AppCompatActivity(), HTTPConnectionListener, RatingBar.OnRatingBarChangeListener {

    private val TAG = javaClass.name
    private val serverPage = "korail-event-detail"

    private lateinit var httpConnectionService: HTTPConnectionService
    private val GET_EVENT_REVIEW_LIST = 1
    private val eventItem by lazy { intent.getParcelableExtra<EventItem>("eventItem") }

    private val eventReviewList by lazy { ArrayList<EventReviewItem>() }
    private val eventReviewAdapter by lazy { EventReviewAdapter(this, eventReviewList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        setInitView()
        setListener()
        buildRecyclerView()
        Log.i(TAG, "onCreate: ${eventItem!!.id}")
    }

    private fun setInitView() {
        stationNameTV.text = eventItem!!.location
        performerTV.text = eventItem!!.performer
        contentTV.text = eventItem!!.content
        startTimeTV.text = String.format("시작시간 : %s", eventItem!!.start_time)
        endTimeTV.text = String.format("종료시간 : %s", eventItem!!.end_time)
        reviewRB.onRatingBarChangeListener = this
    }

    override fun onStart() {
        super.onStart()
        startBoundService()
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    private fun setListener() {

    }

    private fun buildRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        eventReviewRV.setHasFixedSize(true)
        eventReviewRV.layoutManager = layoutManager
        eventReviewRV.adapter = eventReviewAdapter
    }

    // 현재 액티비티와 HTTPConnectionService(Bound Service)를 연결하는 메소드
    private fun startBoundService() {
        val startService = Intent(this, HTTPConnectionService::class.java)
        bindService(startService, connection, BIND_AUTO_CREATE)
    }

    // -------------------------- 리뷰 작성 클릭 리스너 관련 이벤트 --------------------------
    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
        val starNum = reviewRB.rating.toInt()

        if (starNum != 0) {
            val ratingIntent = Intent(applicationContext, EventWriteReviewActivity::class.java)
            ratingIntent.putExtra("id", eventItem!!.id)
            ratingIntent.putExtra("starNum", starNum)
            startActivity(ratingIntent)
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
            httpConnectionService.setOnHttpRespondListener(this@EventDetailActivity)
            httpConnectionService.serverGetRequest(makeRequestUrl(), REQUEST_EVENT_REVIEW_LIST, GET_EVENT_REVIEW_LIST)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i(TAG, "바운드 서비스 연결 종료")
        }
    }

    private fun makeRequestUrl(): String {
        return "$serverPage?what=getEventReviewList&&id=${eventItem!!.id}"
    }

    // http 통신 결과를 handling 하는 인터페이스 메소드
    override fun onHttpRespond(responseData: HashMap<*, *>) {
        val handler = Handler(Looper.getMainLooper())
        when (responseData["whichRespond"] as Int) {
            GET_EVENT_REVIEW_LIST -> {
                Log.i(TAG, "onHttpRespond: ${responseData["respondData"]}")
                val responseEventReviewList = responseData["respondData"] as ArrayList<EventReviewItem>?
                if (responseEventReviewList != null) {

                    responseEventReviewList.forEach { eventReviewList.add(it) }
                    handler.post { eventReviewAdapter.notifyDataSetChanged() }
                }
            }
        }
    }
}