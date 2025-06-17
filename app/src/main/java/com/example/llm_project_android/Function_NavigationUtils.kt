package com.example.llm_project_android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

// 화면 전환 정의 함수 파일
fun Context.navigateTo(target: Class<out AppCompatActivity>) {
    val intent = Intent(this, target)
    startActivity(intent)
}