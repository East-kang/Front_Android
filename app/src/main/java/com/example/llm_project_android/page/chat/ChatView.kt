package com.example.llm_project_android.page.chat

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.adapter.ChatAdapter
import com.example.llm_project_android.data.local.db.AppDatabase
import com.example.llm_project_android.data.mapper.toEntity
import com.example.llm_project_android.data.mapper.toModel
import com.example.llm_project_android.data.model.Chat
import kotlinx.coroutines.launch

class ChatView : AppCompatActivity() {

    private lateinit var adapter: ChatAdapter       // 어뎁터
    private val messages = mutableListOf<Chat>()    // 메시지 목록
    private lateinit var recyclerView: RecyclerView // 채팅 목록
    private lateinit var inputBox: EditText         // 텍스트 입력창
    private lateinit var btn_send: ImageButton      // 전송 버튼
    private lateinit var btn_back: ImageButton      // 뒤로가기 버튼
    private lateinit var btn_clear: Button          // 초기화 버튼

    private var currentPage = 0
    private val pageSize = 10
    private var isLoading = false

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
        adapter = ChatAdapter(messages) { suggestion ->
            addUserMessage(suggestion)
            addAiMessage("'$suggestion'에 대한 AI의 응답입니다.")
        }
        recyclerView.adapter = adapter

        // 전송 버튼 초기 상태 (비활성화)
        btn_send.isEnabled = false

        // 과거 대화 내역 불러오기
        loadMoreMessages()

        // 인삿말 항상 출력 및 저장
        AI_initMessage()

        // 스크롤이 맨 위에 닿았을 때 더 불러오기
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

                if (firstVisiblePosition == 0 && !isLoading) {
                    loadMoreMessages()
                }
            }
        })

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
        saveMessage(chat)   // 내부 db에 저장
    }

    // AI 메시지
    private fun addAiMessage(message: String, suggestions: List<String>? = null) {
        val chat = Chat(content = message, isUser = false, timestamp = System.currentTimeMillis(), suggestions = suggestions, showTime = true)
        messages.add(chat)
        adapter.notifyItemInserted(messages.lastIndex)
        recyclerView.scrollToPosition(messages.lastIndex)
        saveMessage(chat)   // 내부 db에 저장
    }

    // 메시지 내부 db 저장 함수
    private fun saveMessage(chat: Chat) {
        val dao = AppDatabase.getDatabase(applicationContext).chatDao()
        val entity = chat.toEntity()

        lifecycleScope.launch {
            dao.insert(entity)
        }
    }

    // 대화내역 불러오기 (최조 진입 시 + 페이징)
    private fun loadMoreMessages() {
        if (isLoading) return
        isLoading = true

        val dao = AppDatabase.getDatabase(applicationContext).chatDao()

        lifecycleScope.launch {
            val entities = dao.getPagedChats(limit = pageSize, offset = currentPage * pageSize)
            val loaded = entities.map { it.toModel() }.reversed()

            if (loaded.isNotEmpty()) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val prevPosition = layoutManager.findFirstVisibleItemPosition()

                // 스크롤 위치 보정 (이전 메시지 수만큼)
                messages.addAll(0, loaded)
                adapter.notifyItemRangeInserted(0, loaded.size)

                layoutManager.scrollToPositionWithOffset(prevPosition + loaded.size, 0)
                currentPage++
            }

            isLoading = false
        }
    }

    // DB 초기화
    private fun clear_DB() {
        btn_clear.setOnClickListener {
            val dao = AppDatabase.getDatabase(applicationContext).chatDao()

            lifecycleScope.launch {
                dao.clearAll()                  // DB에서 모든 대화 내역 삭제
                messages.clear()                // 메모리상의 메시지 리스트도 모두 제거
                adapter.notifyDataSetChanged()  // RecyclerView 갱신 → 화면에서 대화가 전부 사라짐
                AI_initMessage()                // AI 인삿말 메시지를 다시 추가 및 표시 + DB에 저장
            }


        }
    }
}