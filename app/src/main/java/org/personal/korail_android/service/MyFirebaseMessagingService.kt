package org.personal.korail_android.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.personal.korail_android.R
import org.personal.korail_android.utils.SharedPreferenceHelper

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = javaClass.name

    companion object {
        const val ACTION_SEND_COUPLE_CHAT = "INTENT_ACTION_SEND_COUPLE_CHAT"
        const val ACTION_SEND_OPEN_CHAT = "INTENT_ACTION_SEND_OPEN_CHAT"
    }

    /* 새로운 토큰을 파이어베이스로부터 받으면 shared preference 에 저장 -> 서버에 userColumnID 와 함께 저장하기 위해 대기
    TODO: 다음 과정 하기
    1. 회원 가입 완료 시 -> 서버 데이터 베이스에 userColumnID 와 함께 저장
    2. 로그인 시 -> 서버 데이터 베이스에 userColumnID 수정
     */
    override fun onNewToken(token: String) {

        Log.i(TAG, "onNewToken : $token")
        // 새로운 토큰을 받으면 shared preference 에 저장해 놓고, 회원가입이 완료 되거나 로그인을 했을 시에 DB 에 업데이트 해준다.
        SharedPreferenceHelper.setString(this, getText(R.string.firebaseMessagingToken).toString(), token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

    }
}


