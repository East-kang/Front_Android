package com.example.llm_project_android

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView

// 입력 텍스트 실시간 반영 함수 정의 파일
fun createFlexibleTextWatcher(
    targetTextView: TextView? = null,                   // 변경하려는 target
    hideOnInput: Boolean = false,                       // 입력 시 INVISIBLE 처리 여부
    updateText: ((String) -> String)? = null,           // 텍스트 내용 변환 (null이면 무시)
    updateTextColor: ((String) -> Int)? = null,         // 색상 변경 (null이면 무시)
    onValidStateChanged: ((Boolean) -> Unit)? = null,   // 규칙 만족 여부
    validateInput: ((String) -> Boolean)? =null,        // 버튼 활성화 제어
    enableFormatting: Boolean = false,                  // 문자 포맷팅 기능 사용 여부
    formatChar: String = "/",                           // 삽입할 문자
    formatPositions: List<Int> = listOf(4, 6),          // 삽입 위치
): TextWatcher {
    return object : TextWatcher {

        private var isFormatting = false
        private var previousText: String = ""
        private var cursorPosition: Int = 0

        // 입력이 변경되기 전 호출 (사용하지 않음)
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            previousText = s?.toString() ?: ""
            cursorPosition = start
        }

        // 입력 도중 호출됨: 실시간으로 변화 감지
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val input = s?.toString() ?: ""

            // 입력 시 TextView '숨김' 처리
            if(hideOnInput) {
                targetTextView?.visibility = View.INVISIBLE
            }
            
            // 텍스트뷰 '내용' 업데이트
            updateText?.let {
                targetTextView?.text = it(input)
            }

            // TextView '색상' 업데이트
            updateTextColor?.let {
                targetTextView?.setTextColor(it(input))
            }

            // 버튼 활성화 제어 처리
            validateInput?.let{
                val isValid = it(input)
                onValidStateChanged?.invoke(isValid)
            }
        }

        // 입력 후 호출 (사용x)
        override fun afterTextChanged(s: Editable?) {if (isFormatting) return
            if (isFormatting) return
            isFormatting = true

            val editText = targetTextView as? EditText
            val raw = s?.toString() ?: ""
            var formatted = raw
            var newCursorPosition = cursorPosition

            if (enableFormatting) {
                val clean = raw.replace(formatChar, "")
                val sb = StringBuilder()

                // 삭제 시 포맷 문자 제거 고려
                if (previousText.length > raw.length &&
                    previousText.getOrNull(cursorPosition) == formatChar.firstOrNull()
                ) {
                    newCursorPosition = (cursorPosition - 1).coerceAtLeast(0)
                }

                for (i in clean.indices) {
                    sb.append(clean[i])
                    if ((i + 1) in formatPositions && i != clean.lastIndex) {
                        sb.append(formatChar)
                    }
                }

                formatted = sb.toString()

                if (formatted != raw) {
                    editText?.setText(formatted)
                    editText?.setSelection(newCursorPosition.coerceAtMost(formatted.length))
                }
            }

            validateInput?.let {
                val isValid = it(formatted)
                onValidStateChanged?.invoke(isValid)
            }

            isFormatting = false
        }
    }
}
