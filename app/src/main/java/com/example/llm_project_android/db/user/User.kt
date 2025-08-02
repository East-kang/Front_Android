// User 엔티티 정의 (내부 DB 테이블 스키마 정의)

package com.example.llm_project_android.db.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")

data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String = "",
    val password: String = "",
    val email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val isMarried: Boolean = false,
    val job: String = "",
    val diseases: List<String> = emptyList(),
    val subscriptions: List<String> = emptyList(),
    val createdAt: String = "",
    val modifiedAt: String = "",
    val isLogin: Boolean = false,
    val isDeleted: Boolean = false
) {
    companion object {
        fun empty(): User = User()  // 기본 생성자 호출
    }
}