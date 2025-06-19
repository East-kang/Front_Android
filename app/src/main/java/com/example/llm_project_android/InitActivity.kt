package com.example.llm_project_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.init_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_login = findViewById<Button>(R.id.login_Button)         // 로그인 버튼 선언
        val btn_sign_up = findViewById<Button>(R.id.sign_up_Button)     // 회원가입 버튼 선언

        // 로그인 버튼 클릭 메서드
        btn_login.setOnClickListener {
            navigateTo(LoginActivity::class.java)
        }

        // 회원가입 버튼 클릭 메서드
        btn_sign_up.setOnClickListener {
            navigateTo(SignUpActivity1::class.java, "source" to "InitActivity")
        }
    }
}