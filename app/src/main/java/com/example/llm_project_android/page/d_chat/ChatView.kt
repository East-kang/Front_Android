package com.example.llm_project_android.page.d_chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.adapter.ChatAdapter
import com.example.llm_project_android.data.model.Chat
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.page.c_product.ProductDetailActivity

class ChatView : AppCompatActivity() {

    private lateinit var adapter: ChatAdapter       // 어뎁터
    private val messages = mutableListOf<Chat>()    // 메시지 목록
    private lateinit var recyclerView: RecyclerView // 채팅 목록
    private lateinit var inputBox: EditText         // 텍스트 입력창
    private lateinit var btn_send: ImageButton      // 전송 버튼
    private lateinit var btn_back: ImageButton      // 뒤로가기 버튼
    private lateinit var btn_clear: Button          // 초기화 버튼

    private var companyIcon: Int = -1
    private var companyName: String = ""
    private var category: String = ""
    private var productName: String = ""
    private var recommendation: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.page_chat_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.listView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        inputBox = findViewById(R.id.chat_window)
        btn_send = findViewById(R.id.send_button)
        btn_back = findViewById(R.id.backButton)
        btn_clear = findViewById(R.id.clearButton)

        // 초기 설정 (어뎁터, 전송 버튼 초기화)
        init()

        // 메시지 전송 버튼 활성화 (텍스트 입력 시, 활성화)
        active_send_button()

        // 전송 버튼 클릭 이벤트
        click_send_button()
    }

    private fun AI_initMessage() {
        addAiMessage("안녕하세요! AI 보험 상담사입니다.\uD83E\uDD16\n\n어떤 보험에 대해 궁금한 점이 있으신가요? 아래 버튼을 선택하시거나 직접 질문해주세요!",
            listOf(
                "암보험 추천 서비스",
                "보험료 계산 서비스",
                "자동차 보험 서비스",
                "건강 보험 상담 서비스"))
    }

    // 초기 설정
    private fun init() {
        // 어뎁터 초기화
        adapter = ChatAdapter(
            messages = messages,
            onSuggestionClick = { suggestion ->
                addUserMessage(suggestion)
                addAiMessage("'$suggestion'에 대한 AI의 응답입니다.")
            },
            onRecommendationClick = { companyIcon, companyName, category, productName, recommendation ->
                navigateTo(
                    ProductDetailActivity::class.java,
                    "source" to "ChatView",
                    "company_icon" to companyIcon,
                    "company_name" to companyName,
                    "category" to category,
                    "insurance_name" to productName,
                    "recommendation" to recommendation
                )
            }
        )
        recyclerView.adapter = adapter

        // 전송 버튼 초기 상태 (비활성화)
        btn_send.isEnabled = false

        // 인삿말 항상 출력 및 저장
        AI_initMessage()
    }

    // 전송 버튼 활성화 함수
    private fun active_send_button() {
        inputBox.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    btn_send.isEnabled = true
                    btn_send.setBackgroundResource(R.drawable.design_arrow_up_active)
                } else {
                    btn_send.isEnabled = false
                    btn_send.setBackgroundResource(R.drawable.design_arrow_up_inactive)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // 메시지 전송 클릭 버튼 이벤트 함수
    private fun click_send_button() {
        btn_send.setOnClickListener {
            val userMessage = inputBox.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                addUserMessage(userMessage)
                inputBox.setText("")
                addAiMessage("답장이다!")
            }
        }
    }

    // User 메시지
    private fun addUserMessage(message: String) {
        val chat = Chat(content = message, isUser = true, timestamp = System.currentTimeMillis(), showTime = true)
        messages.add(chat)
        adapter.notifyItemInserted(messages.lastIndex)
        recyclerView.scrollToPosition(messages.lastIndex)
    }

    // AI 메시지
    private fun addAiMessage(message: String, suggestions: List<String>? = null) {
        val chat = Chat(content = message, isUser = false, timestamp = System.currentTimeMillis(), suggestions = suggestions, showTime = true)
        messages.add(chat)
        adapter.notifyItemInserted(messages.lastIndex)
        recyclerView.scrollToPosition(messages.lastIndex)
    }

}