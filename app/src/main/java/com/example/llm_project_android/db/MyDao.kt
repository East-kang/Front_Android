package com.example.llm_project_android.db

import androidx.room.*

@Dao
interface MyDAO {

    // 유저 저장 또는 덮어쓰기 (로그인 시 사용)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // 유저 정보 가져오기 (로그인 유저 조회)
    @Query("SELECT * FROM user_table LIMIT 1")
    suspend fun getLoggedInUser(): User?

    // 전체 삭제 (로그아웃 시 사용)
    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()
}
