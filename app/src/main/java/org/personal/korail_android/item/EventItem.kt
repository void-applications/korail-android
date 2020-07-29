package org.personal.korail_android.item

import android.os.Parcel
import android.os.Parcelable

data class EventItem(
    val id: Int?,
    var locationImage: Int,
    val performer: String?,
    val content: String?,
    val location: String?,
    val start_time: String?,
    val end_time: String?,
    var progressState: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeInt(locationImage)
        parcel.writeString(performer)
        parcel.writeString(content)
        parcel.writeString(location)
        parcel.writeString(start_time)
        parcel.writeString(end_time)
        parcel.writeString(progressState)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventItem> {
        override fun createFromParcel(parcel: Parcel): EventItem {
            return EventItem(parcel)
        }

        override fun newArray(size: Int): Array<EventItem?> {
            return arrayOfNulls(size)
        }
    }
}