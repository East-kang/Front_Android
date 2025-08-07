package com.example.llm_project_android.data.sample

import com.example.llm_project_android.R
import com.example.llm_project_android.data.model.Insurance

    object Products_Insurance {

        val productList = arrayListOf(
            // 삼성생명
            Insurance(
                company_icon = R.drawable.image_logo_samsung,
                company_name = "삼성생명",
                name = "삼성생명 건강보험",
                description = "일상생활에서 발생할 수 있는 다양한 질병과 상해를 보장합니다.",
                payment = 8,
                category = "건강",
                recommendation = true
            ),
            Insurance(
                company_icon = R.drawable.image_logo_samsung,
                company_name = "삼성생명",
                name = "e암보험(비갱신)",
                description = "보험료 인상 없이 1천만원 보장하는 수술보험입니다.",
                payment = 3,
                category = "암",
                recommendation = false
            ),
        Insurance(
            company_icon = R.drawable.image_logo_samsung,
            company_name = "삼성생명",
            name = "삼성생명 종신보험",
            description = "사망 시 유가족에게 경제적 안정을 보장하는 종신보험입니다.",
            payment = 12,
            category = "사망",
            recommendation = true
        ),
        Insurance(
            company_icon = R.drawable.image_logo_samsung,
            company_name = "삼성생명",
            name = "삼성생명 연금보험",
            description = "은퇴 후 안정적인 노후 생활을 위한 연금을 제공합니다.",
            payment = 15,
            category = "저축•연금",
            recommendation = true
        ),

        // 현대해상
        Insurance(
            company_icon = R.drawable.image_logo_hyundai,
            company_name = "현대해상",
            name = "현대해상 하이카 건강보험",
            description = "각종 질병과 입원, 수술비를 보장하는 종합 건강보험입니다.",
            payment = 9,
            category = "건강",
            recommendation = false
        ),
        Insurance(
            company_icon = R.drawable.image_logo_hyundai,
            company_name = "현대해상",
            name = "현대해상 암플러스 보장보험",
            description = "암 진단부터 치료까지 폭넓게 보장하는 암 전문 보험입니다.",
            payment = 4,
            category = "암",
            recommendation = false
        ),
        Insurance(
            company_icon = R.drawable.image_logo_hyundai,
            company_name = "현대해상",
            name = "현대해상 실손의료비보험",
            description = "입원·외래·약제비까지 실제 발생한 병원비를 보장합니다.",
            payment = 4,
            category = "기타",
            recommendation = false
        ),
        Insurance(
            company_icon = R.drawable.image_logo_hyundai,
            company_name = "현대해상",
            name = "현대해상 운전자보험",
            description = "교통사고 시 형사·벌금·치료비까지 폭넓게 보장합니다.",
            payment = 2,
            category = "기타",
            recommendation = false
        ),

        // KB손해보험
        Insurance(
            company_icon = R.drawable.image_logo_kb,
            company_name = "KB손해보험",
            name = "KB손해보험 종합건강보험",
            description = "질병과 상해를 종합적으로 보장하는 건강보험입니다.",
            payment = 15,
            category = "건강",
            recommendation = true
        ),
        Insurance(
            company_icon = R.drawable.image_logo_kb,
            company_name = "KB손해보험",
            name = "KB 암든든보험",
            description = "암 진단 시 고액 보장과 치료비를 보장합니다.",
            payment = 6,
            category = "암",
            recommendation = false
        ),
        Insurance(
            company_icon = R.drawable.image_logo_kb,
            company_name = "KB손해보험",
            name = "KB 실손의료비보험",
            description = "실제 지출한 병원비를 보장하는 기본 실손 보험입니다.",
            payment = 3,
            category = "기타",
            recommendation = false
        ),
        Insurance(
            company_icon = R.drawable.image_logo_kb,
            company_name = "KB손해보험",
            name = "KB 운전자안심보험",
            description = "교통사고 처리 지원 및 변호사 비용까지 보장합니다.",
            payment = 2,
            category = "기타",
            recommendation = false
        )
    )
}