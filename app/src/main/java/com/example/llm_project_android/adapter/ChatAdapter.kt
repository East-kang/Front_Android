package com.example.llm_project_android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.data.model.Chat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter (
    private val messages: List<Chat>,                // 메시지 리스트
    private val onSuggestionClick: (String) -> Unit // 빠른말 서비스 클릭
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 0        // 유저 입력
        private const val VIEW_TYPE_AI = 1          // AI 입력

        // 전송 시간 포맷팅
        private fun formatTime(time: Long): String {
            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
            return sdf.format(Date(time))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_USER) {
            val view = inflater.inflate(R.layout.design_user_message, parent, false)
            UserViewHoler(view)
        } else {
            val view = inflater.inflate(R.layout.design_ai_message, parent, false)
            AiViewHolder(view, messages, onSuggestionClick)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserViewHoler) holder.bind(message)
        if (holder is AiViewHolder) holder.bind(message)
    }

    // User 메시지 바인딩 함수
    class UserViewHoler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.message_user)
        private val timeText: TextView = itemView.findViewById(R.id.send_time_user)

        fun bind(msg: Chat) {       // 메시지, 전송 시간 바인딩
            messageText.text = msg.content                                      // 메시지 내용 바인딩
            timeText.text = formatTime(msg.timestamp)                           // 전송 시간 포맷팅
            timeText.visibility = if (msg.showTime) View.VISIBLE else View.GONE // 전송 시간 시각화 여부
        }
    }

    // AI 메시지 바인딩 함수
    class AiViewHolder(itemView: View, private val messages: List<Chat>, private val onSuggestionClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.message_ai)
        private val timeText:TextView = itemView.findViewById(R.id.send_time_ai)

        private val serviceLayout: LinearLayout = itemView.findViewById(R.id.service_Layout)
        private val serviceButton: List<AppCompatButton> = listOf(
            itemView.findViewById(R.id.service0),
            itemView.findViewById(R.id.service1),
            itemView.findViewById(R.id.service2),
            itemView.findViewById(R.id.service3)
        )

        fun bind(msg: Chat) {
            messageText.text = msg.content
            timeText.text = formatTime(msg.timestamp)
            timeText.visibility = if (msg.showTime) View.VISIBLE else View.GONE

            // 빠른말 서비스가 있는 경우
            if (msg.suggestions != null && msg.suggestions.isNotEmpty() &&
                bindingAdapterPosition == messages.indexOfFirst { it.suggestions?.isNotEmpty() == true }) {

                serviceLayout.visibility = View.VISIBLE
                for (i in serviceButton.indices) {
                    if (i < msg.suggestions.size) {
                        val suggestion = msg.suggestions[i]
                        serviceButton[i].apply {
                            visibility = View.VISIBLE
                            text = suggestion
                            setOnClickListener { onSuggestionClick(suggestion) }
                        }
                    } else {
                        serviceLayout.visibility = View.GONE
                    }
                }
            }
        }
    }
}