package org.personal.korail_android

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import org.personal.korail_android.item.EventItem
import org.personal.korail_android.item.HiddenAreaItem

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
                val toHiddenRestArea = Intent(this, HiddenRestAreaListActivity::class.java).apply {
                    addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT)
                }
                startActivity(toHiddenRestArea)
            }
            R.id.chat -> {
                val toChat = Intent(this, ChatListActivity::class.java).apply {
                    addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT)
                }
                startActivity(toChat)
            }
            R.id.lostAndFound -> {
                val toLostAndFound = Intent(this, LostAndFoundSearch::class.java).apply {
                    addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT)
                }
                startActivity(toLostAndFound)
            }

            R.id.facilities -> {
                val toFacilities = Intent(this, MoreInfoActivity::class.java).apply {
                    addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT)
                }
                startActivity(toFacilities)
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
                val eventItem = EventItem(0, R.drawable.ic_baseline_train_24, "푸른하늘", "통기타라이브", "대전역","2020-08-01 19:00", "2020-08-01 21:00", "진행 예정")
                val toEvent = Intent(this, EventDetailActivity::class.java).apply {
                    putExtra("eventItem", eventItem)
                }
                startActivity(toEvent)
            }

            R.id.eventIB -> {
                val workAndChargeZone = HiddenAreaItem(1, R.drawable.working_and_charging_zone_detail, "동대문역사문화공원역", "2", "STRESS FREE ZONE", "카테고리 : 충전 / 휴식", "content")

                val hiddenAreaItem = Intent(this, HiddenAreaDetailActivity::class.java).apply {
                    putExtra("hiddenAreaItem",workAndChargeZone)
                }
                startActivity(hiddenAreaItem)
            }
        }
    }
}