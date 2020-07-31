package org.personal.korail_android.item

import android.os.Parcel
import android.os.Parcelable

data class HiddenAreaItem(val id :Int, val imageResourceID: Int, val location: String?, val detailLocation: String?, val title: String?, val category: String?, val content: String?) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(imageResourceID)
        parcel.writeString(location)
        parcel.writeString(detailLocation)
        parcel.writeString(title)
        parcel.writeString(category)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HiddenAreaItem> {
        override fun createFromParcel(parcel: Parcel): HiddenAreaItem {
            return HiddenAreaItem(parcel)
        }

        override fun newArray(size: Int): Array<HiddenAreaItem?> {
            return arrayOfNulls(size)
        }
    }

}