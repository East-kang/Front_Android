package com.example.llm_project_android.db.Users// 테이블 스키마 정의

data class UserDto(
    val userId: String,
    val password: String,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val birthDate: String,              // ISO 8601 문자열로 받을 경우: ex. "1995-08-15"
    val gender: String,
    val isMarried: Boolean,
    val job: String,
    val diseases: List<String>,
    val subscriptions: List<String>,
    val createdAt: String,              // Date는 보통 문자열로 받음
    val modifiedAt: String,
    val isLogin: Boolean,
    val isDeleted: Boolean
)
