package org.personal.korail_android

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_event_write_review.*
import org.json.JSONObject
import org.personal.korail_android.background.HTTPConnectionThread.Companion.REQUEST_SIMPLE_POST_METHOD
import org.personal.korail_android.interfaces.HTTPConnectionListener
import org.personal.korail_android.service.HTTPConnectionService

class EventWriteReviewActivity : AppCompatActivity(), View.OnClickListener, HTTPConnectionListener {

    private val TAG = javaClass.name
    private val serverPage = "korail-event-review"

    private val itemId by lazy { intent.getIntExtra("id", 0) }
    private val starNum by lazy { intent.getIntExtra("starNum", 0).toFloat() }

    private lateinit var httpConnectionService: HTTPConnectionService
    private val UPLOAD_EVENT_REVIEW = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_write_review)
        setInitView()
        setListener()
    }

    override fun onStart() {
        super.onStart()
        startBoundService()
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    private fun setInitView() {
        reviewRB.rating = starNum
    }

    private fun setListener() {
        writeButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)
    }

    // 현재 액티비티와 HTTPConnectionService(Bound Service)를 연결하는 메소드
    private fun startBoundService() {
        val startService = Intent(this, HTTPConnectionService::class.java)
        bindService(startService, connection, BIND_AUTO_CREATE)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.writeButton -> {
                val review = reviewET.text.toString()

                if (review.trim() != "") {
                    val postData = JSONObject().apply {
                        put("what", "uploadEventReview")
                        put("id", itemId)
                        put("review", reviewET.text.toString())
                        put("star", reviewRB.rating)
                    }
                    httpConnectionService.serverPostRequest(serverPage, postData.toString(), REQUEST_SIMPLE_POST_METHOD, UPLOAD_EVENT_REVIEW)
                } else {
                    Toast.makeText(this, "리뷰를 작성해주세요", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.cancelButton -> {
                finish()
            }
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
            httpConnectionService.setOnHttpRespondListener(this@EventWriteReviewActivity)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i(TAG, "바운드 서비스 연결 종료")
        }
    }

    // http 통신 결과를 handling 하는 인터페이스 메소드
    override fun onHttpRespond(responseData: HashMap<*, *>) {
        when (responseData["whichRespond"] as Int) {
            UPLOAD_EVENT_REVIEW -> {
                Log.i(TAG, "onHttpRespond: ${responseData["respondData"]}")
                finish()
            }
        }
    }
}