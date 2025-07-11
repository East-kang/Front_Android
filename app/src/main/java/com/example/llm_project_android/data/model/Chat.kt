package com.example.llm_project_android.data.model

data class Chat (
    var messageID: Int,     // 메시지 ID
    var role: String,       // 역할
    var content: List<MessageData>     // 내용
)

data class MessageData (
    val content: String,
    val createdAt: Long,
    val from: Int
)