package org.personal.korail_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import org.personal.korail_android.item.EventItem

class HomeActivity : AppCompatActivity(), View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private val TAG = javaClass.name

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
        //lostAndFoundIB.setOnClickListener(this)
        eventIB.setOnClickListener(this)
        bottomNavigation.setOnNavigationItemSelectedListener(this) // 바텀 네비게이션 리스너
    }

    //------------------ 네비게이션 바 클릭 시 이벤트 관리하는 메소드 모음 ------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.hiddenRestArea -> {
                val toHiddenRestArea = Intent(this, HiddenRestAreaListActivity::class.java)
                startActivity(toHiddenRestArea)
                finish()
            }
            R.id.chat -> {
                val toChat = Intent(this, ChatListActivity::class.java)
                startActivity(toChat)
                finish()
            }
            R.id.lostAndFound -> {
                val toLostAndFound = Intent(this, LostAndFoundSearch::class.java)
                startActivity(toLostAndFound)
                finish()
            }

            R.id.facilities -> {
                val toFacilities = Intent(this, MoreInfoActivity::class.java)
                startActivity(toFacilities)
                finish()
            }
        }
        overridePendingTransition(0, 0)
        return true
    }

    //------------------ 클릭 시 이벤트 관리하는 메소드 모음 ------------------
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.chatIB -> {
                val toChatList = Intent(this, ChatListActivity::class.java)
                startActivity(toChatList)
            }

            R.id.culturalFacilitiesIB -> {
                val toEventList = Intent(this, CulturalFacilitiesDetailActivity::class.java)
                toEventList.putExtra("id", "19");
                startActivity(toEventList)
            }

            R.id.eventIB -> {
                val eventItem = EventItem(0, R.drawable.ic_baseline_train_24, "푸른하늘", "통기타라이브", "대전역","2020-08-01 19:00", "2020-08-01 21:00", "진행 예정")
                val toEvent = Intent(this, EventDetailActivity::class.java).apply {
                    putExtra("eventItem", eventItem)
                }
                startActivity(toEvent)
            }
        }
    }
}