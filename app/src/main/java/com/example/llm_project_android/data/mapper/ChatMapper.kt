package com.example.llm_project_android.data.mapper

import com.example.llm_project_android.data.local.entity.ChatEntity
import com.example.llm_project_android.data.model.Chat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun Chat.toEntity(): ChatEntity = ChatEntity(
    content = content,
    isUser = isUser,
    timestamp = timestamp,
    showTime = showTime,
    suggestionsJson = suggestions?.let { Gson().toJson(it) }
)

fun ChatEntity.toModel(): Chat = Chat(
    content = content,
    isUser = isUser,
    timestamp = timestamp,
    showTime = showTime,
    suggestions = suggestionsJson?.let {
        Gson().fromJson(it, object : TypeToken<List<String>>() {}.type)
    }
)
