package com.example.llm_project_android

import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.widget.EditText
// 텍스트 박스 색상 함수

fun setBoxField(editText: EditText, defaultStrokeColor: Int) {
    val background = editText.background as GradientDrawable
    val strokeWidth = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 1.5f, editText.resources.displayMetrics
    ).toInt()
    background.setStroke(strokeWidth, defaultStrokeColor)
}