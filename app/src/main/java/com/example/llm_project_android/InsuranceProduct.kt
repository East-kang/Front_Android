package com.example.llm_project_android

import android.media.Image

data class InsuranceProduct(
    var company_icon: Image,    // 기업 아이콘
    var company_name: String,   // 기업명
    var name: String,           // 상품명
    var description: String,    // 상품 설명
    var payment: String         // 월 납입금
)