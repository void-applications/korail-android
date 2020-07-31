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
import org.personal.korail_android.item.ChatRoomItem
import org.personal.korail_android.item.HiddenAreaItem

class HiddenAreaAdapter (private val context: Context, private var hiddenAreaList: ArrayList<HiddenAreaItem>, private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<HiddenAreaAdapter.ViewHolder>() {

    private val TAG = javaClass.name

    class ViewHolder(itemView: View, private val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener{


        val representativeImageIV : ImageView = itemView.findViewById(R.id.representativeImageIV)
        val locationTV :TextView =itemView.findViewById(R.id.locationTV)
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val categoryTV: TextView = itemView.findViewById(R.id.categoryTV)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            if (adapterPosition != RecyclerView.NO_POSITION) {

                itemClickListener.onItemClick(itemView, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hidden_area, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int {
        return hiddenAreaList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hiddenAreaItem: HiddenAreaItem = hiddenAreaList[position]

        holder.representativeImageIV.setImageResource(hiddenAreaItem.imageResourceID)
        holder.locationTV.text = hiddenAreaItem.location
        holder.titleTV.text = hiddenAreaItem.title
        holder.categoryTV.text = hiddenAreaItem.category
    }

    fun filterList(filteredHiddenAreaList: ArrayList<HiddenAreaItem>) {
        hiddenAreaList = filteredHiddenAreaList
        notifyDataSetChanged()
    }
}