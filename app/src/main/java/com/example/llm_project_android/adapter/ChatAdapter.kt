package com.example.llm_project_android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.data.model.Chat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ChatView.kt  : 채팅 기능
class ChatAdapter (
    private val messages: List<Chat>,                   // 메시지 리스트
    private val onSuggestionClick: (String) -> Unit,    // 빠른말 서비스 클릭
    private val onRecommendationClick: (
        companyIcon: Int,
        companyName: String,
        category: String?,
        productName: String,
        recommendation: Boolean) -> Unit                // 추천 상품 뷰 클릭
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 0         // 유저 입력
        private const val VIEW_TYPE_AI = 1           // AI 입력

        // 전송 시간 포맷팅
        private fun formatTime(time: Long): String {
            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
            return sdf.format(Date(time))
        }
    }

    // 아이템 뷰 타입 결정( 채팅 전송: User / AI )
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_AI
    }

    // ViewHolder 객체 생성( User -> UserViewHoler / AI -> AiViewHolder 반환 )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_USER) {
            val view = inflater.inflate(R.layout.design_user_message, parent, false)
            UserViewHoler(view)
        } else {
            val view = inflater.inflate(R.layout.design_ai_message, parent, false)
            AiViewHolder(view, messages, onSuggestionClick, onRecommendationClick)
        }
    }

    // 총 메시지 개수 반환
    override fun getItemCount(): Int = messages.size

    // 생성된 ViewHolder에 데이터(채팅 내용) 바인딩
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
            messageText.text = msg.content                                      // 메시지 내용 설정
            timeText.text = formatTime(msg.timestamp)                           // 시간 포맷 적용 (formatTime)
            timeText.visibility = if (msg.showTime) View.VISIBLE else View.GONE // showTime이 true일 때만 시간 표시
        }
    }

    // AI 메시지 바인딩 함수
    class AiViewHolder(itemView: View, private val messages: List<Chat>,
                       private val onSuggestionClick: (String) -> Unit,
                       private val onRecommendationClick: (
                           companyIcon: Int,
                            companyName: String,
                            category: String?,
                            productName: String,
                            recommendation: Boolean) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.message_ai)                  // AI 메시지 내용
        private val timeText:TextView = itemView.findViewById(R.id.send_time_ai)                    // AI 메시지 전송 시간

        private val serviceLayout: LinearLayout = itemView.findViewById(R.id.service_Layout)        // 서비스 버튼 레이아웃
        private val serviceButton: List<AppCompatButton> = listOf(                                  // 서비스 버튼 리스트
            itemView.findViewById(R.id.service0),
            itemView.findViewById(R.id.service1),
            itemView.findViewById(R.id.service2),
            itemView.findViewById(R.id.service3)
        )

        private val recommendationView: LinearLayout = itemView.findViewById(R.id.recommendation)       // 추천 상품 뷰
        private val recommendationCompany: ImageView = itemView.findViewById(R.id.recommandation_icon)  // 추천 상품 회사
        private val recommendationName: TextView = itemView.findViewById(R.id.recommandation_name)      // 추천 상품명
        private var recommendationIcon: Int = -1

        // AI 메시지, 빠른말 서비스 버튼, 추천 아이템 뷰 처리
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
            } else {
                serviceLayout.visibility = View.GONE
            }

            if (msg.recommendation) {
                recommendationView.visibility = View.VISIBLE
                recommendationName.text = msg.productName

                when (msg.companyName) {
                    "삼성화재" -> recommendationIcon = R.drawable.image_logo_samsung
                    "현대해상" -> recommendationIcon = R.drawable.image_logo_hyundai
                    "KB손해보험" -> recommendationIcon = R.drawable.image_logo_kb
                    else -> recommendationIcon = R.drawable.image_main_icon
                }
                recommendationCompany.setBackgroundResource(recommendationIcon)

                recommendationView.setOnClickListener {
                    onRecommendationClick (
                        recommendationIcon,
                        msg.companyName.orEmpty(),
                        null,
                        msg.productName.orEmpty(),
                        true
                    )
                }
            } else {
                recommendationView.visibility = View.GONE
            }
        }
    }
}