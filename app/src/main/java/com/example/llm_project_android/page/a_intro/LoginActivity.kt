package com.example.llm_project_android.page.a_intro

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.createFlexibleTextWatcher
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.pw_eye_visibility
import com.example.llm_project_android.functions.startShakeAnimation
import com.example.llm_project_android.page.c_product.MainViewActivity
import com.example.llm_project_android.page.b_signup.SignUpActivity1

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.a_page_login_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id_text = findViewById<EditText>(R.id.id_editText)
        val pw_text = findViewById<EditText>(R.id.password_editText)
        val btn_pw = findViewById<ImageButton>(R.id.eyeButton)
        val btn_login = findViewById<Button>(R.id.login_Button)
        val warning_text = findViewById<TextView>(R.id.pw_warning_textView)
        val signUp_text = findViewById<TextView>(R.id.signUpText)
        var pw_visible: Boolean = false                                      // 비밀번호 시각화 여부 (true: 시각화, false: 비시각화)
        var check_id: Boolean = false                                        // 아이디 존재 여부 (true: 유, false: 무)
        var check_pw: Boolean = false                                        // 비밀번호 존재 여부 (true: 유, false: 무)


        val id: String = "qwer" // 임시 아이디
        val pw: String = "1234" // 임시 비밀번호

        // 비밀번호 시각화 버튼 클릭 이벤트
        pw_eye_visibility(btn_pw, pw_text, { pw_visible }, { pw_visible = it })

        // 로그인 버튼 클릭 이벤트
        btn_login.setOnClickListener {
            if (id_text.text.toString() == id && pw_text.text.toString() == pw)           // 로그인 정보 일치 시 (화면 전환)
                navigateTo(MainViewActivity::class.java)
            else
                showLoginError(warning_text)
        }

        // 회원가입 텍스트 클릭 이벤트
        signUp_text.setOnClickListener {
            navigateTo(SignUpActivity1::class.java, "source" to "LoginActivity")
        }

        // 경고 메시지 플로팅 메서드
        id_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = warning_text,
                hideOnInput = true
            )
        )
        pw_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = warning_text,
                hideOnInput = true
            )
        )
    }

    // 입력 시 경고 메시지 숨김 처리
    fun showLoginError(text: TextView) {
        text.visibility = View.VISIBLE          // 경고 메시지 띄우기
        text.startShakeAnimation(this) // 애니메이션 적용
    }
}