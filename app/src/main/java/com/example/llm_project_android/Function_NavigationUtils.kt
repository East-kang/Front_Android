package com.example.llm_project_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

// 화면 전환 정의 함수 파일

// 화면 전환 함수
fun Context.navigateTo(target: Class<out AppCompatActivity>, vararg extras: Pair<String, Any?>, reverseAnimation: Boolean = false) {
    val intent = Intent(this, target)
    for ((key, value) in extras) {          // 다중 데이터 전달
        when (value) {
            null -> intent.putExtra(key, null as String?)   // null 허용
            is String -> intent.putExtra(key, value)
            is Boolean -> intent.putExtra(key, value)
            else -> throw IllegalArgumentException("Unsupported type for intent extra: key=$key, value=$value")
        }
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

// 다중 키에 대한 Boolean 값 가져오기 (기본값 포함)
fun AppCompatActivity.getPassedBooleans(vararg keys: String, default: Boolean = false): Map<String, Boolean> {
    return keys.associateWith { intent.getBooleanExtra(it, default) }
}