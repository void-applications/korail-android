package org.personal.korail_android.interfaces

interface HTTPConnectionListener {
    fun onHttpRespond(responseData : HashMap<*, *>)
}