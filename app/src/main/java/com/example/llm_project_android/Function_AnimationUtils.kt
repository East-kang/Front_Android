package com.example.llm_project_android

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils

// 진동 애니메이션 정의 함수 파일
fun View.startShakeAnimation(context: Context) {
    val shake = AnimationUtils.loadAnimation(context, com.example.llm_project_android.R.anim.shake)
    this.startAnimation(shake)
}