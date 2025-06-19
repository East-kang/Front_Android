package com.example.llm_project_android

import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.widget.EditText
// 텍스트 박스 색상 함수

fun setBoxField(editText: EditText, defaultStrokeColor: Int) {
    val background = editText.background.mutate() as GradientDrawable
    val strokeWidth = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 1.5f, editText.resources.displayMetrics
    ).toInt()   // 테두리 두께 변수 (1f=1dp)
    background.setStroke(strokeWidth, defaultStrokeColor)
}