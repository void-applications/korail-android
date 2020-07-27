package org.personal.korail_android.background

import android.annotation.SuppressLint
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import org.personal.korail_android.`interface`.HTTPConnectionListener
import org.personal.korail_android.utils.serverConnection.HTTPRequest

class HTTPConnectionThread(name: String?, private val httpConnectionListener: HTTPConnectionListener) : HandlerThread(name) {

    // msg.what, msg.arg1 의 value 값 선언
    companion object {
        const val GET_REQUEST = 1
        const val POST_REQUEST = 2
        const val PUT_REQUEST = 3
        const val DELETE_REQUEST = 4


        // get 메소드 관련
        const val REQUEST_SIMPLE_GET_METHOD = 1

        // post 메소드 관련
        const val REQUEST_SIMPLE_POST_METHOD = 1
    }

    private val TAG = javaClass.name

    private lateinit var handler: Handler

    @SuppressLint("HandlerLeak")
    override fun onLooperPrepared() {
        Log.i("thread-test", "onLooperPrepared")

        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                // 메인에 보낼 데이터 저장하는 헤시 맵
                val httpRespondData = HashMap<String, Any?>()
                // 어떤 HTTPRequest 메소드를 사용할 지 정함
                val whichRequest: Int? = msg.arg1
                // 메인에서 어떤 메시지를 보낼지 정한다.
                httpRespondData["whichRespond"] = msg.arg2

                when (msg.what) {

                    // 서버로부터 데이터를 받아오고 결과를 UIHandler 로 전송
                    GET_REQUEST -> {
                        val requestPage = msg.obj.toString()
                        val httpRequest = HTTPRequest(requestPage)

                        when (whichRequest) {
                            // 간단한 GET request
                            REQUEST_SIMPLE_GET_METHOD -> {
                                httpRespondData["respondData"] = httpRequest.getMethodToServer()
                            }
                        }
                    }

                    // 서버에 데이터를 전송하고 서버에서 받은 결과를 UIHandler 로 전송
                    POST_REQUEST -> {
                        // HashMap 으로 보낸 msg.obj 객체 캐스팅
                        val msgObjHashMap = msg.obj as HashMap<*, *>
                        val serverPage = msgObjHashMap["serverPage"].toString()
                        val httpRequest = HTTPRequest(serverPage)
                        val postData: Any

                        when (whichRequest) {
                            // 간단한 데이터(메인 스레드에서 jsonString 으로 만들어서 보내는 경우)
                            REQUEST_SIMPLE_POST_METHOD -> {
                                postData = msgObjHashMap["postData"].toString()
                                httpRespondData["respondData"] = httpRequest.postMethodToServer(postData)
                            }
                        }
                    }

                    PUT_REQUEST -> {
                        // HashMap 으로 보낸 msg.obj 객체 캐스팅
                        val msgObjHashMap = msg.obj as HashMap<*, *>
                        val serverPage = msgObjHashMap["serverPage"].toString()
                        val httpRequest = HTTPRequest(serverPage)
                        val putData: Any

                        when (whichRequest) {

                        }
                    }

                    DELETE_REQUEST -> {
                        // HashMap 으로 보낸 msg.obj 객체 캐스팅
                        val msgObjHashMap = msg.obj as HashMap<*, *>
                        val serverPage = msgObjHashMap["serverPage"].toString()
                        val httpRequest = HTTPRequest(serverPage)
                        val deleteData: Any

                        when (whichRequest) {

                        }
                    }
                }
                httpConnectionListener.onHttpRespond(httpRespondData)
            }
        }
    }

    fun getHandler(): Handler {
        return handler
    }
}