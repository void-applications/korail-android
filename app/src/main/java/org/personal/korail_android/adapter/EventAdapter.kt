package org.personal.korail_android.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.personal.korail_android.R
import org.personal.korail_android.interfaces.ItemClickListener
import org.personal.korail_android.item.EventItem
import org.personal.korail_android.utils.CalendarHelper

class EventAdapter(private val context: Context, private var subwayEventList: ArrayList<EventItem>, private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private val TAG = javaClass.name

    class ViewHolder(itemView: View, private val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {


        val locationIV: ImageView = itemView.findViewById(R.id.locationIV)
        val locationTV: TextView = itemView.findViewById(R.id.locationTV)
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val contentTV: TextView = itemView.findViewById(R.id.contentTV)
        val startTimeTV: TextView = itemView.findViewById(R.id.startTimeTV)
        val endTimeTV: TextView = itemView.findViewById(R.id.endTimeTV)
        val progressStateTV: TextView = itemView.findViewById(R.id.progressStateTV)


        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {

            if (adapterPosition != RecyclerView.NO_POSITION) {

                itemClickListener.onItemClick(itemView, adapterPosition)
            }
        }

        override fun onLongClick(p0: View?): Boolean {

            if (adapterPosition != RecyclerView.NO_POSITION) {

                itemClickListener.onItemLongClick(itemView, adapterPosition)
            }
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int {
        return subwayEventList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventItem = subwayEventList[position]
        Log.i(TAG, "onBindViewHolder: $eventItem.location")
        holder.titleTV.text = eventItem.performer
        holder.locationIV.setImageResource(getLocationResourceId(eventItem))
        holder.locationTV.text = eventItem.location
        holder.contentTV.text = String.format("내용 : %s", eventItem.content)
        holder.startTimeTV.text = String.format("시작 : %s", eventItem.start_time)
        holder.endTimeTV.text = String.format("종료 : %s", eventItem.end_time)
        setProgressState(eventItem, holder.progressStateTV)
    }

    fun filterList(filteredEventList: ArrayList<EventItem>) {
        subwayEventList = filteredEventList
        notifyDataSetChanged()
    }

    private fun getLocationResourceId(eventItem: EventItem): Int {
        val resourceId: Int = when (eventItem.location) {
            "사당역" -> R.drawable.event_sadang
            "노원역" -> R.drawable.event_nowon
            "이수역" -> R.drawable.event_isu
            "선릉역" -> R.drawable.event_seolleung
            "동대문역사문화공원역" -> R.drawable.event_dongdaemun_history_park
            else -> R.drawable.ic_baseline_train_24
        }
        eventItem.locationImage = resourceId
        return resourceId
    }

    private fun setProgressState(eventItem: EventItem, progressStateTV: TextView) {
        val currentTime = CalendarHelper.getCurrentTimeInMills()
        val eventStartTime = CalendarHelper.stringToTimeInMills(eventItem.start_time!!)
        val eventEndTime = CalendarHelper.stringToTimeInMills(eventItem.end_time!!)


        if (currentTime > eventEndTime) {
            eventItem.progressState = "종료"
            progressStateTV.text = "종료"
            progressStateTV.setTextColor(context.getColor(R.color.red))
        } else if (currentTime < eventStartTime) {
            eventItem.progressState = "공연 예정"
            progressStateTV.text = "공연 예정"
            progressStateTV.setTextColor(context.getColor(R.color.black))
        } else{
            eventItem.progressState = "진행중"
            progressStateTV.text = "진행중"
            progressStateTV.setTextColor(context.getColor(R.color.green))
        }
    }
}