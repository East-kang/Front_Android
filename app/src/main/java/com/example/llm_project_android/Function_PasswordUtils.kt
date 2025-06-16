package com.example.llm_project_android

import android.text.InputType
import android.widget.EditText
import android.widget.ImageButton

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