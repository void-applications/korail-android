package org.personal.korail_android.utils.serverConnection

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.personal.korail_android.item.EventItem
import org.personal.korail_android.item.EventReviewItem
import org.personal.korail_android.item.HiddenReviewItem
import org.personal.korail_android.item.LocalStoredChatRoom

class HTTPRequest(serverPage: String) : HTTPOutPut {

    companion object {
        const val POST = 1
        const val PUT = 2
    }

    private val TAG = javaClass.name

    // 서버 domain : ip 주소는 https redirect 설정이 되어 있어 aws domain 사용
    private val serverDomain = "http://ec2-13-125-99-215.ap-northeast-2.compute.amazonaws.com/"

    // 서버와 연결을 관리하는 클래스
    // domain 과 페이지를 통해 url 완성
    private val hTTPConnection: HTTPConnection = HTTPConnection(serverDomain + serverPage)

    //------------------ GET 관련 메소드 모음 ------------------
    override fun getMethodToServer(): String {
        val jsonString: String = hTTPConnection.getRequest()
        Log.i(TAG, "getMethodToServer : ${hTTPConnection.responseCode}")
        return jsonString.replace("\"", "")
    }

    override fun getEventItemList(): ArrayList<EventItem> {
        val jsonString: String = hTTPConnection.getRequest()
        val gson = Gson()

        return gson.fromJson(jsonString, object : TypeToken<ArrayList<EventItem>>() {}.type)
    }

    override fun getEventReviewList(): ArrayList<EventReviewItem>? {
        val jsonString: String = hTTPConnection.getRequest()
        val gson = Gson()

        Log.i(TAG, "getEventReviewList: $jsonString")
        return if (hTTPConnection.responseCode == 200) {
            gson.fromJson(jsonString, object : TypeToken<ArrayList<EventReviewItem>>() {}.type)
        } else {
            null
        }
    }

    override fun getHiddenReviewList(): ArrayList<HiddenReviewItem>? {
        val jsonString: String = hTTPConnection.getRequest()
        val gson = Gson()

        Log.i(TAG, "getEventReviewList: $jsonString")
        return if (hTTPConnection.responseCode == 200) {
            gson.fromJson(jsonString, object : TypeToken<ArrayList<HiddenReviewItem>>() {}.type)
        } else {
            null
        }
    }

    //------------------ POST 관련 메소드 모음 ------------------
    override fun postMethodToServer(postJsonString: String): String {
        val jsonString = hTTPConnection.postRequest(postJsonString)
        Log.i(TAG, "postMethodToServer: $jsonString")

        return jsonString.replace("\"", "")
    }

    //------------------ PUT 관련 메소드 모음 ------------------
    override fun putMethodToServer(postJsonString: String): String {
        val jsonString = hTTPConnection.putRequest(postJsonString)
        Log.i(TAG, "put, delete 체크 : $jsonString ")
        return jsonString.replace("\"", "")
    }

    //------------------ DELETE 관련 메소드 모음 ------------------
    // 삭제하는 메소드
    override fun deleteMethodToServer(postJsonString: String): String? {
        val jsonString = hTTPConnection.deleteRequest(postJsonString)

        if (hTTPConnection.responseCode == 200) {
            return jsonString.replace("\"", "")
        }
        return null
    }
}