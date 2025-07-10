package com.example.llm_project_android.functions

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.llm_project_android.R

// 화면 전환 정의 함수 파일

// 화면 전환 함수
fun Context.navigateTo(target: Class<out AppCompatActivity>, vararg extras: Pair<String, Any?>, reverseAnimation: Boolean = false) {
    val intent = Intent(this, target)
    for ((key, value) in extras) {          // 다중 데이터 전달
        when (value) {
            null -> intent.putExtra(key, null as String?)   // null 허용
            is String -> intent.putExtra(key, value)
            is Boolean -> intent.putExtra(key, value)
            is Int -> intent.putExtra(key, value)
            is ArrayList<*> -> {
                if (value.all { it is String}) {
                    @Suppress("UNCHECKED_CAST")
                    intent.putStringArrayListExtra(key, value as ArrayList<String>)
                } else {
                    throw IllegalArgumentException("Only ArrayList<String> is supported for key=$key")
                }
            }
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

// 단일 데이터 타입 키 가져오기 (단일 키용 오버로드 함수)
fun AppCompatActivity.getPassedExtra(key: String, type: Class<*>): Map<String, Any?> {
    return getPassedExtras(listOf(key to type))
}

// 다중 데이터 타입 키값 가져오기 (다중 키용 메인 함수)
fun AppCompatActivity.getPassedExtras(keys: List<Pair<String, Class<*>>>): Map<String, Any?> {
    return keys.associate { (key, type) ->
        val value = when (type) {
            String::class.java -> intent.getStringExtra(key)
            Boolean::class.java -> intent.getBooleanExtra(key, false)
            Int::class.java -> intent.getIntExtra(key, 0)
            else -> throw IllegalArgumentException("Unsupported type: $key → $type")
        }
        key to value
    }
}
