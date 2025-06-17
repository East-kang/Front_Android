package com.example.llm_project_android

import android.text.InputType
import android.widget.EditText
import android.widget.ImageButton

// 텍스트 시각화 여부 및 눈 아이콘 상태 함수 정의 파일
fun togglePasswordVisibility(editText: EditText, isVisible: Boolean, imageButton: ImageButton): Boolean {
    return if (!isVisible) {
        editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD // 비밀번호 보기
        imageButton.setImageResource(R.drawable.resize_eye_visibility_off)
        true
    } else {
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD  // 비밀번호 숨기기
        imageButton.setImageResource(R.drawable.resize_eye_visibility)
        false
    }
}