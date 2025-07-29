package com.example.llm_project_android.functions

import android.app.Activity
import android.app.AlertDialog

// 오류 메시지 창 정의
fun showErrorDialog(activity: Activity, message: String) {
    AlertDialog.Builder(activity)
        .setTitle("알림")
        .setMessage(message)
        .setPositiveButton("예") { dialog, _ ->
            dialog.dismiss()    // 창 닫기
        }
        .show()
}

// 예/아니오 메시지 창 정의
fun showConfirmDialog(
    activity: Activity,
    title: String,
    message: String,
    onResult: (Boolean) -> Unit  // true: 예, false: 아니오
) {
    AlertDialog.Builder(activity)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("예") { dialog, _ ->
            dialog.dismiss()
            onResult(true)
        }
        .setNegativeButton("아니오") { dialog, _ ->
            dialog.dismiss()
            onResult(false)
        }
        .show()
}