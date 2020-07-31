package org.personal.korail_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HiddenAreaDetailActivity : AppCompatActivity() {

    private val TAG = javaClass.name
    private val hiddenAreaTitle by lazy { intent.getStringExtra("title") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hidden_area_detail)
        setInitView()
        setListener()
        buildRecyclerView()
    }

    private fun setInitView() {

    }

    private fun setListener() {

    }

    private fun buildRecyclerView() {
    }
}