// 찜 엔티티 정의 (내부 DB 테이블 스키마 정의)

package com.example.llm_project_android.db.wishList

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wished_table")

data class WishList(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val insuranceName: String = "",
    val wishedTimeStamp: Long = System.currentTimeMillis(),
    val isWished: Boolean = true
) {
    companion object {
        fun empty(): WishList = WishList()  // 기본 생성자 호출
    }
}