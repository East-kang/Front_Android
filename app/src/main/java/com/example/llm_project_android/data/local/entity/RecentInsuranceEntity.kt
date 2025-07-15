package com.example.llm_project_android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_insurance")
data class RecentInsuranceEntity(
    @PrimaryKey val id: String,       // 보험 상품 고유 ID (또는 이름 등)
    val name: String,                 // 상품 이름
    val company: String,              // 보험사
    val timestamp: Long               // 마지막으로 조회한 시간
)

