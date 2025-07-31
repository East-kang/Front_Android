package com.example.llm_project_android.db.WishList

import androidx.room.*

@Dao
interface UserWishListDao {

    // 찜 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWish(wish: UserWishList)

    // 찜 삭제
    @Delete
    suspend fun deleteWish(wish: UserWishList)

    // 특정 상품 찜 여부 확인
    @Query("SELECT * FROM user_wish_list WHERE userId = :userId AND productName = :productName LIMIT 1")
    suspend fun getWishItem(userId: Int, productName: String): UserWishList?

    @Query("DELETE FROM user_wish_list WHERE userId = :userId AND productName = :productName")
    suspend fun deleteWishByProduct(userId: Int, productName: String)
}