package com.example.llm_project_android

import android.widget.EditText
import android.widget.ImageButton

// 비밀번호 보기 버튼 리스너 등록 함수 정의 파일 (+ 커서 위치 조정 포함)
fun pw_eye_visibility (btn: ImageButton, text: EditText, get_visible: () -> Boolean, set_visible: (Boolean) -> Unit) {
    btn.setOnClickListener {
        val pos = text.selectionStart           // 커서 위치 저장
        val newVisible = (togglePasswordVisibility(text, get_visible(), btn))      // 비밀번호 시각화
        set_visible(newVisible)                 // Boolean 값 업데이트
        text.post{ text.setSelection(pos) }     // 커서 위치 복원
    }
}