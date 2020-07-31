package org.personal.korail_android

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.RatingBar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_hidden_area_detail.*
import kotlinx.android.synthetic.main.activity_hidden_area_detail.contentTV
import org.personal.korail_android.adapter.HiddenAreaReviewAdapter
import org.personal.korail_android.background.HTTPConnectionThread
import org.personal.korail_android.interfaces.HTTPConnectionListener
import org.personal.korail_android.item.HiddenAreaItem
import org.personal.korail_android.item.HiddenReviewItem
import org.personal.korail_android.service.HTTPConnectionService

class HiddenAreaDetailActivity : AppCompatActivity(), HTTPConnectionListener, RatingBar.OnRatingBarChangeListener {

    private val TAG = javaClass.name
    private val serverPage  = "korail-hidden-area"

    private lateinit var httpConnectionService: HTTPConnectionService
    private val GET_HIDDEN_REVIEW_LIST = 1

    private val hiddenAreaItem by lazy { intent.getParcelableExtra("hiddenAreaItem")  as HiddenAreaItem?}

    private val hiddenReviewList by lazy { ArrayList<HiddenReviewItem>() }
    private val hiddenReviewAdapter by lazy { HiddenAreaReviewAdapter(this, hiddenReviewList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hidden_area_detail)
        setInitView()
        setListener()
        buildRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        hiddenReviewList.clear()
        startBoundService()
        reviewRB.rating = 0f
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    private fun setInitView() {
        stationNameTV.text = hiddenAreaItem!!.location
        representativeImageIV.setImageResource(hiddenAreaItem!!.imageResourceID)
        titleTV.text =hiddenAreaItem!!.title
        categoryTV.text = hiddenAreaItem!!.category
        detailLocationTV.text = hiddenAreaItem!!.detailLocation
        contentTV.text = hiddenAreaItem!!.content
    }

    private fun setListener() {
        reviewRB.onRatingBarChangeListener = this
    }

    private fun buildRecyclerView() {
        val layoutManager = LinearLayoutManager(this)

        hiddenReviewRV.setHasFixedSize(true)
        hiddenReviewRV.layoutManager = layoutManager
        hiddenReviewRV.adapter = hiddenReviewAdapter
    }

    // 현재 액티비티와 HTTPConnectionService(Bound Service)를 연결하는 메소드
    private fun startBoundService() {
        val startService = Intent(this, HTTPConnectionService::class.java)
        bindService(startService, connection, BIND_AUTO_CREATE)
    }

    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
        val starNum = reviewRB.rating.toInt()

        if (starNum != 0) {
            val ratingIntent = Intent(applicationContext, HiddenWriteReviewActivity::class.java)
            ratingIntent.putExtra("id", hiddenAreaItem!!.id)
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
            httpConnectionService.setOnHttpRespondListener(this@HiddenAreaDetailActivity)
            val handler = Handler(Looper.getMainLooper())
            httpConnectionService.serverGetRequest(makeRequestUrl(), HTTPConnectionThread.REQUEST_HIDDEN_REVIEW_LIST, GET_HIDDEN_REVIEW_LIST)

        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i(TAG, "바운드 서비스 연결 종료")
        }
    }

    private fun makeRequestUrl(): String {
        return "$serverPage?what=getHiddenReviewList&&id=${hiddenAreaItem!!.id}"
    }

    // http 통신 결과를 handling 하는 인터페이스 메소드
    override fun onHttpRespond(responseData: HashMap<*, *>) {
        val handler = Handler(Looper.getMainLooper())
        when (responseData["whichRespond"] as Int) {
            GET_HIDDEN_REVIEW_LIST -> {
                Log.i(TAG, "onHttpRespond: ${responseData["respondData"]}")
                if (responseData["respondData"] != null) {
                    val responseEventReviewList = responseData["respondData"] as ArrayList<HiddenReviewItem>
                    var starsSum = 0f
                    val totalReviewCount = responseEventReviewList.count()

                    responseEventReviewList.forEach {
                        hiddenReviewList.add(it)
                        starsSum += it.stars
                    }

                    val average = starsSum / totalReviewCount

                    handler.post {
                        hiddenReviewAdapter.notifyDataSetChanged()

                        averageFloatTV.text = String.format("%.2f", average) + " 점"
                        averageStarRB.rating = average
                    }
                }
            }
        }
    }
}