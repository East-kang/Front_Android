package com.example.llm_project_android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.llm_project_android.data.local.entity.ChatEntity

@Dao
interface ChatDao {

    @Insert
    suspend fun insert(chat: ChatEntity)

    @Query("SELECT * FROM chat_history ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getPagedChats(limit: Int, offset: Int): List<ChatEntity>

    @Query("DELETE FROM chat_history")
    suspend fun clearAll()
}