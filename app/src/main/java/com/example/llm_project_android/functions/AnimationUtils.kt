package com.example.llm_project_android.functions

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.example.llm_project_android.R

// 진동 애니메이션 정의 함수 파일
fun View.startShakeAnimation(context: Context) {
    val shake = AnimationUtils.loadAnimation(context, R.anim.shake)
    this.startAnimation(shake)
}