package com.example.llm_project_android

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView

// 입력 텍스트 실시간 반영 함수 정의 파일
fun createFlexibleTextWatcher(
    targetTextView: TextView,
    hideOnInput: Boolean = false,                       // 입력 시 INVISIBLE 처리 여부
    updateText: ((String) -> String)? = null,           // 텍스트 내용 변환 (null이면 무시)
    updateTextColor: ((String) -> Int)? = null,         // 색상 변경 (null이면 무시)
    onValidStateChanged: ((Boolean) -> Unit)? = null,   // 규칙 만족 여부
    validateInput: ((String) -> Boolean)? =null,        // 버튼 활성화 제어
): TextWatcher {
    return object : TextWatcher {

        // 입력이 변경되기 전 호출 (사용하지 않음)
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        // 입력 도중 호출됨: 실시간으로 변화 감지
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val input = s?.toString() ?: ""

            // 입력 시 TextView '숨김' 처리
            if(hideOnInput) {
                targetTextView.visibility = View.INVISIBLE
            }
            
            // 텍스트뷰 '내용' 업데이트
            updateText?.let {
                targetTextView.text = it(input)
            }

            // TextView '색상' 업데이트
            updateTextColor?.let {
                targetTextView.setTextColor(it(input))
            }

            // 버튼 활성화 제어 처리
            validateInput?.let{
                val isValid = it(input)
                onValidStateChanged?.invoke(isValid)
            }
        }

        // 입력 후 호출 (사용x)
        override fun afterTextChanged(s: Editable?) {}
    }
}