package org.personal.korail_android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.personal.korail_android.R
import org.personal.korail_android.item.EventReviewItem

class EventReviewAdapter(private val context: Context, private var eventReviewList: ArrayList<EventReviewItem>) :
    RecyclerView.Adapter<EventReviewAdapter.ViewHolder>() {

    private val TAG = javaClass.name

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewRB: RatingBar = itemView.findViewById(R.id.reviewRB)
        val reviewTV: TextView = itemView.findViewById(R.id.reviewTV)
        val dateTV: TextView = itemView.findViewById(R.id.dateTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cultural_facilities_review, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return eventReviewList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventReviewItem: EventReviewItem = eventReviewList[position]
        holder.reviewRB.rating = eventReviewItem.stars.toFloat()
        holder.reviewTV.text = eventReviewItem.review_message
        holder.dateTV.text = eventReviewItem.date
    }
}