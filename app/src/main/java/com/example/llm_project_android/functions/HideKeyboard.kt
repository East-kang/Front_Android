package com.example.llm_project_android.functions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = activity.currentFocus ?: View(activity)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

// 터치가 EditText 외 영역이면 키보드 내리기
fun handleTouchOutsideEditText(activity: Activity, event: MotionEvent): Boolean {
    val focusedView = activity.currentFocus
    if (focusedView != null && focusedView is View) {
        val outRect = Rect()
        focusedView.getGlobalVisibleRect(outRect)
        if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
            hideKeyboard(activity)
        }
    }
    return false
}