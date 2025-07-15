package com.example.llm_project_android.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// DB 저장용 Entity
@Entity(tableName = "chat_history")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val isUser: Boolean,
    val timestamp: Long,
    val showTime: Boolean,
    val suggestionsJson: String? = null
)
