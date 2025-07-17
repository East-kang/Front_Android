package com.example.llm_project_android.page.a_intro

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.registerExitDialogOnBackPressed
import com.example.llm_project_android.page.b_signup.SignUpActivity1
import com.example.llm_project_android.page.e_chat.ChatView

class InitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.page_init_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_login = findViewById<Button>(R.id.login_Button)         // 로그인 버튼 선언
        val btn_sign_up = findViewById<Button>(R.id.sign_up_Button)     // 회원가입 버튼 선언

        // 로그인 버튼 클릭 이벤트
        btn_login.setOnClickListener {
            // navigateTo(LoginActivity::class.java)
            navigateTo(ChatView::class.java)
        }

        // 회원가입 버튼 클릭 이벤트
        btn_sign_up.setOnClickListener {
            navigateTo(SignUpActivity1::class.java, "source" to "InitActivity")
        }

        // 뒤로가기 버튼 클릭 이벤트
        registerExitDialogOnBackPressed()
    }
}