package com.example.llm_project_android.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// DAO: 대화 내역 저장/조회/삭제
@Dao
interface ChatDao {

    // 대화 내역 1건 저장
    @Insert
    suspend fun insert(chat: ChatEntity)

    // 대화 내역 페이징 조회 (최신순 정렬)
    @Query("SELECT * FROM chat_history ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getPagedChats(limit: Int, offset: Int): List<ChatEntity>

    // 전체 삭제 (로그아웃 시 사용)
    @Query("DELETE FROM chat_history")
    suspend fun clearAll()
}
