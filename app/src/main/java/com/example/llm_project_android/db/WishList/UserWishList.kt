// 찜 목록 엔티티 정의 (내부 DB 테이블 스키마 정의)

package com.example.llm_project_android.db.WishList

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.llm_project_android.db.Users.User

@Entity(
    tableName = "user_wish_list",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],     // User 엔티티의 PK
            childColumns = ["userId"],  // UserWishList에서 FK
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId", "productName"], unique = true)] // 상품 중복 방지
)

data class UserWishList (
    @PrimaryKey(autoGenerate = true) val wishListId: Int = 0,
    val userId: Int,                                    // Users 테이블 FK
    val productName : String,                           // 상품명
    val wishedDate: Long = System.currentTimeMillis()   // 찜한 시간
)