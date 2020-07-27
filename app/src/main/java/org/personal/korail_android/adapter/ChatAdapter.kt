package org.personal.korail_android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.personal.korail_android.R
import org.personal.korail_android.data.ChatData

class ChatAdapter(val context: Context, private val myTokenTableId: Int, private val messageList: ArrayList<ChatData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = javaClass.name

    private val MY_CHAT = 1
    private val OTHER_CHAT = 2

    class OthersChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTV: TextView = itemView.findViewById(R.id.nameTV)
        val otherMessageTV: TextView = itemView.findViewById(R.id.messageTV)
        val otherMessageTimeTV: TextView = itemView.findViewById(R.id.messageTimeTV)
    }

    class MyChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myMessageTV: TextView = itemView.findViewById(R.id.messageTV)
        val myMessageTimeTV: TextView = itemView.findViewById(R.id.messageTimeTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MY_CHAT) {
            view = inflater.inflate(R.layout.item_my_chat, parent, false)
            return MyChatViewHolder(view)
        }
        view = inflater.inflate(R.layout.item_others_chat, parent, false)
        return OthersChatViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        if (myTokenTableId == messageList[position].sender_id) {
            return MY_CHAT
        }
        return OTHER_CHAT
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messageData = messageList[position]

        if (myTokenTableId == messageData.sender_id) {
            val myChatViewHolder = holder as MyChatViewHolder
            myChatViewHolder.myMessageTV.text  = messageData.message
            myChatViewHolder.myMessageTimeTV.text = messageData.message_time
        } else {
            val othersChatViewHolder = holder as OthersChatViewHolder
//            othersChatViewHolder.nameTV.text = messageData.name // TODO : 이름 랜덤으로 결정하기
            othersChatViewHolder.otherMessageTV.text = messageData.message
            othersChatViewHolder.otherMessageTimeTV.text = messageData.message_time
        }
    }
}