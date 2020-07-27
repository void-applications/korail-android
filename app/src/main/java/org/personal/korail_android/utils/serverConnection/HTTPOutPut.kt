package org.personal.korail_android.utils.serverConnection

// HTTPRequest 에 implement, 서버와의 통신 후 결과를 반환하는 메소드 포함
interface HTTPOutPut {

    // GET 메소드
    fun getMethodToServer(): String

    // POST 메소드
    fun postMethodToServer(postJsonString: String): String

    // PUT 메소드
    fun putMethodToServer(postJsonString: String): String

    // DELETE 메소드
    fun deleteMethodToServer(postJsonString: String): String?
}