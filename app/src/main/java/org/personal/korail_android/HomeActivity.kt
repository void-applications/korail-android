package org.personal.korail_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setListener()
    }

    override fun onResume() {
        super.onResume()
        bottomNavigation.selectedItemId = R.id.home
    }

    private fun setListener() {
        chatIB.setOnClickListener(this)
        culturalFacilitiesIB.setOnClickListener(this)
        lostAndFoundIB.setOnClickListener(this)
        eventIB.setOnClickListener(this)
        bottomNavigation.setOnNavigationItemSelectedListener(this) // 바텀 네비게이션 리스너
    }

    //------------------ 네비게이션 바 클릭 시 이벤트 관리하는 메소드 모음 ------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.event -> {
                val toEvent  = Intent(this, EventListActivity::class.java)
                startActivity(toEvent)
            }
            R.id.chat ->   {
                val toChat  = Intent(this, ChatListActivity::class.java)
                startActivity(toChat)
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

    //------------------ 클릭 시 이벤트 관리하는 메소드 모음 ------------------
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.chatIB -> {
                val toChatList = Intent(this, ChatListActivity::class.java)
                startActivity(toChatList)
            }

            R.id.culturalFacilitiesIB -> {
                val toEventList = Intent(this, CulturalFacilitiesListActivity::class.java)
                startActivity(toEventList)
            }

            R.id.eventIB -> {
                val toEvent = Intent(this, EventListActivity::class.java)
                startActivity(toEvent)
            }

            R.id.lostAndFoundIB -> {
                val toLostAndFound = Intent(this, lostAndFoundSearch::class.java)
                startActivity(toLostAndFound)
            }
        }
    }
}