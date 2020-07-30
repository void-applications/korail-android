package org.personal.korail_android

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_facilities.*
import org.personal.korail_android.interfaces.HTTPConnectionListener
import org.personal.korail_android.service.HTTPConnectionService


class FacilitiesActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, HTTPConnectionListener {

    private val TAG = javaClass.name
    private val serverPage = ""

    private lateinit var httpConnectionService:HTTPConnectionService
    private val SEARCH_STATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facilities)
        setListener()
    }

    override fun onStart() {
        super.onStart()
        startBoundService()
    }

    override fun onResume() {
        super.onResume()
        bottomNavigation.selectedItemId = R.id.facilities
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    private fun setListener() {
        bottomNavigation.setOnNavigationItemSelectedListener(this) // 바텀 네비게이션 리스너
    }


    // 현재 액티비티와 HTTPConnectionService(Bound Service)를 연결하는 메소드
    private fun startBoundService() {
        val startService = Intent(this, HTTPConnectionService::class.java)
        bindService(startService, connection, BIND_AUTO_CREATE)
    }

    //------------------ 네비게이션 바 클릭 시 이벤트 관리하는 메소드 모음 ------------------
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
            R.id.chat -> {
                val toChat = Intent(this, ChatListActivity::class.java)
                startActivity(toChat)
            }
            R.id.lostAndFound -> {
                val toLostAndFound = Intent(this, LostAndFoundSearch::class.java)
                startActivity(toLostAndFound)
            }
        }
        overridePendingTransition(0, 0)
        return true
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
            httpConnectionService.setOnHttpRespondListener(this@FacilitiesActivity)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i(TAG, "바운드 서비스 연결 종료")
        }
    }

    // http 통신 결과를 handling 하는 인터페이스 메소드
    override fun onHttpRespond(responseData: HashMap<*, *>) {
        when (responseData["whichRespond"] as Int) {
            SEARCH_STATION -> {
//                Log.i(TAG, "onHttpRespond: ${responseData["respondData"]}")
            }
        }
    }
}