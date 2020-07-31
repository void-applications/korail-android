package org.personal.korail_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_hidden_rest_area_list.*

class HiddenRestAreaListActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hidden_rest_area_list)
        setListener()
    }


    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }
    private fun setListener() {
        bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    // -------------------------- 네비게이션 리스너 관련 이벤트 --------------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val toHome = Intent(this, HomeActivity::class.java)
                startActivity(toHome)
            }
            R.id.chat-> {
                val toChat = Intent(this, ChatListActivity::class.java)
                startActivity(toChat)
            }

            R.id.lostAndFound -> {
                val toLostAndFound = Intent(this, LostAndFoundSearch::class.java)
                startActivity(toLostAndFound)
            }

            R.id.facilities -> {
                val toLostAndFound = Intent(this, FacilitiesActivity::class.java)
                startActivity(toLostAndFound)
            }
        }
        overridePendingTransition(0, 0)
        return true
    }
}