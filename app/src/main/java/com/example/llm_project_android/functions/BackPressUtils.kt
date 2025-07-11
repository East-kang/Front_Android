package com.example.llm_project_android.functions

import android.app.Activity
import android.app.AlertDialog
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

// 앱 종료 함수 정의
fun AppCompatActivity.registerExitDialogOnBackPressed() {
    onBackPressedDispatcher.addCallback(this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog(this@registerExitDialogOnBackPressed)
            }
        })
}

// 메시지 창 정의
private fun showDialog(activity: Activity) {
    AlertDialog.Builder(activity)
        .setTitle("앱 종료")
        .setMessage("앱을 종료하시겠습니까?")
        .setPositiveButton("예") { _, _, ->
            activity.finishAffinity()
        }
        .setNegativeButton("아니오", null)
        .show()
}