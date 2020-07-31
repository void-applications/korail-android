package org.personal.korail_android

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_hidden_rest_area_list.*
import kotlinx.android.synthetic.main.activity_hidden_rest_area_list.hiddenAreaRV
import kotlinx.android.synthetic.main.activity_hidden_rest_area_list.searchStationET
import org.personal.korail_android.adapter.HiddenAreaAdapter
import org.personal.korail_android.interfaces.ItemClickListener
import org.personal.korail_android.item.HiddenAreaItem

class HiddenRestAreaListActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, ItemClickListener, TextWatcher, View.OnClickListener {

    private val TAG = javaClass.name

    private val hiddenAreaList by lazy { ArrayList<HiddenAreaItem>() }
    private var changedHiddenAreaList = ArrayList<HiddenAreaItem>()
    private val hiddenAreaAdapter by lazy { HiddenAreaAdapter(this, hiddenAreaList, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hidden_rest_area_list)
        setListener()
        buildRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        bottomNavigation.selectedItemId = R.id.hiddenRestArea
    }

    private fun setListener() {
        bottomNavigation.setOnNavigationItemSelectedListener(this)
        searchStationET.addTextChangedListener(this)
        clearSearchInputIB.setOnClickListener(this)
    }

    private fun buildRecyclerView() {
        val layoutManager = LinearLayoutManager(this)

        val workAndChargeZone = HiddenAreaItem(R.drawable.working_and_charging_zone_detail, "동대문역사문화공원역", "STRESS FREE ZONE", "카테고리 : 충전 / 휴식")
        val platFormGallery = HiddenAreaItem(R.drawable.working_and_charging_zone_detail, "공덕역", "플랫폼 갤러리", "카테고리 : 갤러리")
        val popUpGallery = HiddenAreaItem(R.drawable.working_and_charging_zone_detail, "공덕역", "팝업 갤러리", "카테고리 : 갤러리")

        hiddenAreaList.add(workAndChargeZone)
        hiddenAreaList.add(platFormGallery)
        hiddenAreaList.add(popUpGallery)

        hiddenAreaRV.setHasFixedSize(true)
        hiddenAreaRV.layoutManager = layoutManager
        hiddenAreaRV.adapter = hiddenAreaAdapter
    }

    // -------------------------- 네비게이션 리스너 관련 이벤트 --------------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val toHome = Intent(this, HomeActivity::class.java)
                startActivity(toHome)
            }
            R.id.chat -> {
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

    // -------------------------- 아이템 클릭 리스너 관련 이벤트 --------------------------
    override fun onItemClick(view: View?, itemPosition: Int) {
        val hiddenAreaItem = getSelectedStation(itemPosition)
        val toHiddenAreaDetail = Intent(this, HiddenAreaDetailActivity::class.java).apply {
            putExtra("title", hiddenAreaItem.title)
        }
        startActivity(toHiddenAreaDetail)
        Log.i(TAG, "onItemClick: ${hiddenAreaItem.title}")
    }

    override fun onItemLongClick(view: View?, itemPosition: Int) {}
    override fun afterTextChanged(text: Editable?) {
        searchFilter(text.toString())
    }

    // 검색하게되면 어뎁터의 list 가 changedChatRoomList 로 변경되기 떄문에 예외처리하여 방의 이름을 반환
    private fun getSelectedStation(itemPosition: Int): HiddenAreaItem {
        val searchText = searchStationET.text.toString()

        return if (searchText != "") changedHiddenAreaList[itemPosition]
        else hiddenAreaList[itemPosition]
    }

    // -------------------------- 갬색 리스너 관련 이벤트 --------------------------
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }


    @SuppressLint("DefaultLocale")
    // 컨텐츠, 공연자명, 위치 결과를 모두 보여준다
    private fun searchFilter(text: String) {
        changedHiddenAreaList.clear()

        hiddenAreaList.forEach {
            if (it.location.toLowerCase().contains(text.toLowerCase())
                || it.title.toLowerCase().contains(text.toLowerCase())
                || it.category.toLowerCase().contains(text.toLowerCase())
            ) {
                changedHiddenAreaList.add(it)
            }
        }
        hiddenAreaAdapter.filterList(changedHiddenAreaList)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.clearSearchInputIB -> {
                searchStationET.text = null
            }
        }
    }
}