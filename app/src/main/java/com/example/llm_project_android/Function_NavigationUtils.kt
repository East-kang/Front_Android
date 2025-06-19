package com.example.llm_project_android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
// 화면 전환 정의 함수 파일

// 화면 전환 함수
fun Context.navigateTo(target: Class<out AppCompatActivity>, vararg extras: Pair<String, String>, reverseAnimation: Boolean = false) {
    val intent = Intent(this, target)
    for ((key, value) in extras) {          // 다중 데이터 전달
        intent.putExtra(key, value)
    }
    startActivity(intent)

    if (this is AppCompatActivity) {
        if (reverseAnimation) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}

// 다중 키에 대한 String 값 가져오기 (기본값 포함)
fun AppCompatActivity.getPassedStrings(vararg keys: String, default: String = ""): Map<String, String> {
    return keys.associateWith { intent.getStringExtra(it) ?: default }
}