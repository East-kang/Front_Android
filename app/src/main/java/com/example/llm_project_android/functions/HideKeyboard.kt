package com.example.llm_project_android.functions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = activity.currentFocus ?: View(activity)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

// 실제로 터치한 뷰가 EditText인지 확인
private fun findTouchedView(view: View?, x: Int, y: Int): View? {
    if (view !is ViewGroup) {
        val rect = Rect()
        view?.getGlobalVisibleRect(rect)
        return if (rect.contains(x, y)) view else null
    }

    for (i in 0 until view.childCount) {
        val child = view.getChildAt(i)
        val touched = findTouchedView(child, x, y)
        if (touched != null) return touched
    }

    val rect = Rect()
    view.getGlobalVisibleRect(rect)
    return if (rect.contains(x, y)) view else null
}

// 터치가 EditText 외 영역이면 키보드 내리기
var downX = 0f
var downY = 0f
const val TOUCH_SLOP = 1  // 허용되는 짧은 움직임 거리(px)

fun handleTouchOutsideEditText(activity: Activity, event: MotionEvent): Boolean {
    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            downX = event.rawX
            downY = event.rawY
        }

        MotionEvent.ACTION_UP -> {
            val deltaX = Math.abs(event.rawX - downX)
            val deltaY = Math.abs(event.rawY - downY)

            // 움직임이 거의 없는 경우 = 짧은 클릭
            if (deltaX < TOUCH_SLOP && deltaY < TOUCH_SLOP) {
                val focusedView = activity.currentFocus
                if (focusedView is EditText) {
                    val rootView = activity.window.decorView
                    val touchedView = findTouchedView(rootView, event.rawX.toInt(), event.rawY.toInt())

                    if (touchedView !is EditText) {
                        hideKeyboard(activity)
                    }
                }
            }
        }
    }
    return false
}
