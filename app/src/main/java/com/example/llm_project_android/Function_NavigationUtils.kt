package com.example.llm_project_android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

fun Context.navigateTo(target: Class<out AppCompatActivity>) {
    val intent = Intent(this, target)
    startActivity(intent)
}