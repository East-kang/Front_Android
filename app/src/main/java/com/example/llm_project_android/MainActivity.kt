package com.example.llm_project_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_login = findViewById<Button>(R.id.login_Button)         // 로그인 버튼 선언
        val btn_sign_up = findViewById<Button>(R.id.sign_up_Button)     // 회원가입 버튼 선언

        btn_login.setOnClickListener {                                      // 로그인 버튼 클릭 메서드
            val intent1 = Intent(this, LoginActivity::class.java)
            startActivity(intent1)
        }

        btn_sign_up.setOnClickListener {                                      // 회원가입 버튼 클릭 메서드
            val intent2 = Intent(this, SignUpActivity1::class.java)
            startActivity(intent2)
        }
    }
}