package com.example.llm_project_android.data.model

data class Chat(
    val content: String,    // 메시지 내용
    val isUser: Boolean,    // 유저 판별
    val timestamp: Long,    // 메시지 생성 시간
    val suggestions: List<String>? = null,  // 빠른 질문 버튼 목록
    val showTime: Boolean = false // 이 메시지 밑에 시간을 표시할지 여부
)