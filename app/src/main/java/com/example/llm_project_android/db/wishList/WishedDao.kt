// SQL 기반 데이터 접근 메서드 정의

package com.example.llm_project_android.db.wishList

import androidx.room.*

@Dao
interface WishedDAO {

    // 상품 찜하기(있으면 덮어쓰기)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWished(wish: WishList)

    // 특정 상품 조회 (찜 여부 확인용, 상품명으로 조회)
    @Query("SELECT * FROM wished_table WHERE insuranceName = :insuranceName LIMIT 1")
    suspend fun getWishByName(insuranceName: String): WishList?

    // 찜 목록 전체 조회 (시간 순서대로)
    @Query("SELECT * FROM wished_table ORDER BY wishedTimeStamp DESC")
    suspend fun getAllWishes(): List<WishList>

    // 특정 상품 찜 해제 (상품명으로 조회)
    @Query("DELETE FROM wished_table WHERE insuranceName = :insuranceName")
    suspend fun deleteWishByName(insuranceName: String)

    // 전체 찜 삭제
    @Query("DELETE FROM wished_table")
    suspend fun deleteAllWishes()

    // 시퀀스 초기화
    @Query("DELETE FROM sqlite_sequence WHERE name = 'wished_table'")
    suspend fun resetAutoIncrement()
}