package org.personal.korail_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_facilities.*
import org.personal.korail_android.service.HTTPConnectionService


class FacilitiesActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private val TAG = javaClass.name
    private val serverPage = ""

    private lateinit var httpConnectionService:HTTPConnectionService
    private val SEARCH_STATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facilities)
        setListener()
    }

    override fun onResume() {
        super.onResume()
        bottomNavigation.selectedItemId = R.id.facilities
    }

    private fun setListener() {
        bottomNavigation.setOnNavigationItemSelectedListener(this) // 바텀 네비게이션 리스너
        performanceIB.setOnClickListener(this)
        culturalFacilitiesIB.setOnClickListener(this)
        nursingRoomIB.setOnClickListener(this)
        serviceCenterIB.setOnClickListener(this)
        toiletIB.setOnClickListener(this)
    }

    //------------------ 네비게이션 바 클릭 시 이벤트 관리하는 메소드 모음 ------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val toHome = Intent(this, HomeActivity::class.java)
                startActivity(toHome)
            }
            R.id.hiddenRestArea -> {
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

    //------------------ 버튼 클릭 시 이벤트 관리하는 메소드 모음 ------------------
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.performanceIB -> {
                val toPerformance = Intent(this, EventListActivity::class.java)
                startActivity(toPerformance)
            }
            R.id.culturalFacilitiesIB -> {
                val toCulturalFacilities = Intent(this, CulturalFacilitiesListActivity::class.java)
                startActivity(toCulturalFacilities)
            }
            R.id.nursingRoomIB -> {
//                val toNursingRoom = Intent(this, ::class.java)
//                startActivity(toNursingRoom)
            }
            R.id.serviceCenterIB -> {
//                val toServiceCenter = Intent(this, EventListActivity::class.java)
//                startActivity(toServiceCenter)
            }
            R.id.toiletIB -> {
//                val toToilet = Intent(this, EventListActivity::class.java)
//                startActivity(toToilet)
            }
        }
    }
}