package org.personal.korail_android.interfaces

import android.view.View

interface ItemClickListener {
    fun onItemClick(view: View?, itemPosition: Int)
    fun onItemLongClick(view:View?, itemPosition: Int)
}