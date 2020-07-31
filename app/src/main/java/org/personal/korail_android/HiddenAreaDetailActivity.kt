package org.personal.korail_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HiddenAreaDetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra("title")

        when (title) {
            "STRESS FREE ZONE" -> setContentView(R.layout.activity_hidden_area_detail_sf)
            "플랫폼 갤러리" -> setContentView(R.layout.activity_hidden_area_detail_platform)
            "팝업 갤러리" -> setContentView(R.layout.activity_hidden_area_detail_popup)
        }
    }
}