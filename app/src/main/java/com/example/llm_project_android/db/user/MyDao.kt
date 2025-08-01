// SQL 기반 데이터 접근 메서드 정의

package com.example.llm_project_android.db.user

import androidx.room.*

@Dao
interface MyDAO {

    // 유저 저장 또는 덮어쓰기 (로그인 시 사용)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // 유저 정보 가져오기 (로그인 유저 조회)
    @Query("SELECT * FROM user_table LIMIT 1")
    suspend fun getLoggedInUser(): User?

    // 유저 정보 수정 (부분 업데이트용)
    @Update
    suspend fun updateUser(user: User)

    // 전체 삭제 (로그아웃 시 사용)
    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()

    // 시퀀스 초기화
    @Query("DELETE FROM sqlite_sequence WHERE name = 'user_table'")
    suspend fun resetAutoIncrement()
}