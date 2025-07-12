package com.example.llm_project_android.page.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.adapter.ChatAdapter
import com.example.llm_project_android.data.model.Chat

class ChatView : AppCompatActivity() {

    private lateinit var adpater: ChatAdapter
    private val messages = mutableListOf<Chat>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputBox: EditText
    private lateinit var btn_send: ImageButton

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
        inputBox = findViewById(R.id.chat_window)
        btn_send = findViewById(R.id.send_button)

        // 초기 설정 (어뎁터, 전송 버튼 초기화)
        init()

        // 메시지 전송 버튼 활성화 (텍스트 입력 시, 활성화)
        active_send_button()

        // 전송 버튼 클릭 이벤트
        click_send_button()
    }

    // 초기 설정
    private fun init() {
        // 어뎁터 초기화
        adpater = ChatAdapter(messages) { suggestion ->
            inputBox.setText(suggestion)
            inputBox.setSelection(suggestion.length)
        }
        recyclerView.adapter = adpater

        // 전송 버튼 초기 상태 (비활성화)
        btn_send.isEnabled = false
    }

    // 전송 버튼 활성화 함수
    private fun active_send_button() {
        inputBox.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btn_send.isEnabled = !s.isNullOrBlank()
                btn_send.setBackgroundResource(
                    if (!s.isNullOrBlank())
                        R.drawable.design_arrow_up_active
                    else
                        R.drawable.design_arrow_up_inactive
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // 메시지 전송 클릭 버튼 이벤트 함수
    private fun click_send_button() {
        val userMessage = inputBox.text.toString().trim()
        if (userMessage.isNotEmpty()) {

        }
    }

}