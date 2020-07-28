package org.personal.korail_android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.personal.korail_android.R
import org.personal.korail_android.interfaces.ItemClickListener
import org.personal.korail_android.item.EventItem

class EventAdapter(private val context: Context, private var subwayEventList: ArrayList<EventItem>, private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private val TAG = javaClass.name

    class ViewHolder(itemView: View, private val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {


        val locationIV : ImageView = itemView.findViewById(R.id.locationIV)
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val locationTV :TextView = itemView.findViewById(R.id.locationTV)
        val startTimeTV :TextView = itemView.findViewById(R.id.startTimeTV)
        val endTimeTV :TextView = itemView.findViewById(R.id.endTimeTV)


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
        holder.titleTV.text = eventItem.title
        holder.locationIV.setImageResource(eventItem.locationImage)
        holder.locationTV.text = eventItem.location
        holder.startTimeTV.text = eventItem.startTime
        holder.endTimeTV.text  = eventItem.endTime
    }

    fun filterList(filteredEventList: ArrayList<EventItem>) {
        subwayEventList = filteredEventList
        notifyDataSetChanged()
    }
}