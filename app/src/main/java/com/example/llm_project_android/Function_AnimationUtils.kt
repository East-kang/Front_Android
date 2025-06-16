package com.example.llm_project_android

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils

fun View.startShakeAnimation(context: Context) {
    val shake = AnimationUtils.loadAnimation(context, com.example.llm_project_android.R.anim.shake)
    this.startAnimation(shake)
}