package com.example.llm_project_android

import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageButton

// 텍스트 시각화 여부 및 눈 아이콘 상태 함수 정의 파일
fun togglePasswordVisibility(editText: EditText, isVisible: Boolean, imageButton: ImageButton): Boolean {
    return if (!isVisible) {
        editText.transformationMethod = null
        imageButton.setImageResource(R.drawable.resize_eye_visibility_off)
        true
    } else {
        editText.transformationMethod = PasswordTransformationMethod.getInstance()
        imageButton.setImageResource(R.drawable.resize_eye_visibility)
        false
    }
}