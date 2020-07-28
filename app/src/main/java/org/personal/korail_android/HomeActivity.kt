package org.personal.korail_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setListener()
    }

    private fun setListener() {
        chatIB.setOnClickListener(this)
        eventIB.setOnClickListener(this)
        lostAndFoundIB.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.chatIB -> {
                val toChatList = Intent(this, ChatListActivity::class.java)
                startActivity(toChatList)
            }

            R.id.eventIB -> {
                val toEventList = Intent(this, EventListActivity::class.java)
                startActivity(toEventList)
            }

            R.id.lostAndFoundIB -> {
                val toLostAndFound = Intent(this, lostAndFoundStep::class.java)
                startActivity(toLostAndFound)
            }
        }
    }
}