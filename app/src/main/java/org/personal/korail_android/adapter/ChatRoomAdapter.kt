package org.personal.korail_android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.personal.korail_android.R
import org.personal.korail_android.interfaces.ItemClickListener
import org.personal.korail_android.item.ChatRoomItem

class ChatRoomAdapter(private val context: Context, private var chatRoomList: ArrayList<ChatRoomItem>, private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    private val TAG = javaClass.name

    class ViewHolder(itemView: View, private val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {


        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val participantCountTV: TextView = itemView.findViewById(R.id.participantCountTV)

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int {
        return chatRoomList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatRoomItem: ChatRoomItem = chatRoomList[position]
        holder.titleTV.text = chatRoomItem.title
        holder.participantCountTV.text = String.format("참여인원 %s명", chatRoomItem.participants)
    }

    fun filterList(filteredChatRoomList: ArrayList<ChatRoomItem>) {
        chatRoomList = filteredChatRoomList
        notifyDataSetChanged()
    }
}