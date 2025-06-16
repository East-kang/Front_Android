package com.example.llm_project_android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.InputType
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        btn_pw.setOnClickListener {
            pw_visible = togglePasswordVisibility(pw_text, pw_visible)      // 비밀번호 시각화
            restoreCursorPosition(pw_text)  // 커서 위치 복원
        }


        btn_login.setOnClickListener {      // 로그인 버튼 클릭 메서드
            if (id_text.text.toString() == id && pw_text.text.toString() == pw) {             // 로그인 정보 일치 시 (화면 전환)
                val intent1 = Intent(this, MainViewActivity::class.java)
                startActivity(intent1)
            } else {                        // 로그인 정보 불일치 시 (경고 메시지 띄우기) (+ 진동 모드 탑재 예정)
                pw_warning_text.visibility = View.VISIBLE                                     // 경고 메시지 띄우기
                val shake = AnimationUtils.loadAnimation(this, R.anim.shake)    // 진동 애니메시션 리소스 불러오기
                pw_warning_text.startAnimation(shake)                              // 애니메이션 적용
            }
        }

        signUp_text.setOnClickListener {    // 회원가입 텍스트 클릭 메서드
            val intent2 = Intent(this, SignUpActivity1::class.java)
            startActivity(intent2)  // 회원가입 뷰로 이동
        }
    }

    // 비밀번호 시각화 함수
    fun togglePasswordVisibility(editText: EditText, isVisible: Boolean): Boolean {
        return if (!isVisible) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD // 비밀번호 보기
            true
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD  // 비밀번호 숨기기
            false
        }
    }

    // 커서 위치 복원 함수
    fun restoreCursorPosition(editText: EditText) {
        val cursorLocation = editText.selectionStart
        editText.setSelection(cursorLocation)
    }

//        fun getSerialNumber(): String{  // Android 고유값 반환 함수
//
//        }
}