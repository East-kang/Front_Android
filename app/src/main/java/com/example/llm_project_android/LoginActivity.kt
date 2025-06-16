package com.example.llm_project_android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.llm_project_android.startShakeAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id_text = findViewById<EditText>(R.id.id_editText)          // 아이디 입력창
        val pw_text = findViewById<EditText>(R.id.password_editText)    // 비밀번호 입력창
        val btn_pw = findViewById<ImageButton>(R.id.eyeButton)          // 비밀번호 시각화 버튼
        val btn_login = findViewById<Button>(R.id.login_Button)         // 로그인 버튼
        val pw_warning_text = findViewById<TextView>(R.id.pw_warning_textView)  // 비밀번호 불일치 안내 텍스트
        val signUp_text = findViewById<TextView>(R.id.signUpText)       // 회원가입 텍스트
        var pw_visible: Boolean = false                                      // 비밀번호 시각화 여부 (true: 시각화, false: 비시각화)
        var check_id: Boolean = false                                        // 아이디 존재 여부 (true: 유, false: 무)
        var check_pw: Boolean = false                                        // 비밀번호 존재 여부 (true: 유, false: 무)


        val id: String = "qwer" // 임시 아이디
        val pw: String = "1234" // 임시 비밀번호

        // 비밀번호 시각화 버튼 클릭 메서드
        btn_pw.setOnClickListener {
            var pos = pw_text.selectionStart            // 커서 위치 저장
            pw_visible = togglePasswordVisibility(pw_text, pw_visible, btn_pw)      // 비밀번호 시각화
            pw_text.post{ pw_text.setSelection(pos) }   // 커서 위치 복원
        }

        // 로그인 버튼 클릭 메서드
        btn_login.setOnClickListener {
            if (id_text.text.toString() == id && pw_text.text.toString() == pw)           // 로그인 정보 일치 시 (화면 전환)
                navigateTo(MainViewActivity::class.java)
            else
                showLoginError(pw_warning_text)
        }

        // 회원가입 텍스트 클릭 메서드
        signUp_text.setOnClickListener {
            navigateTo(SignUpActivity1::class.java)
        }

        pw_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pw_warning_text.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // 로그인 실패 동작 함수
    fun showLoginError(text: TextView) {
        text.visibility = View.VISIBLE          // 경고 메시지 띄우기
        text.startShakeAnimation(this) // 애니메이션 적용
    }
}