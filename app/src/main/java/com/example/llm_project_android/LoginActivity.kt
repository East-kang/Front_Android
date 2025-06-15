package com.example.llm_project_android

import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.text.InputType
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
        var pw_visible: Boolean = false                                     // 비밀번호 시각화 여부 (true: 시각화, false: 비시각화)
        var check_id: Boolean = false                                       // 아이디 존재 여부 (true: 유, false: 무)
        var check_pw: Boolean = false                                       // 비밀번호 존재 여부 (true: 유, false: 무)

        val id: String = "qwer" // 임시 아이디
        val pw: String = "1234" // 임시 비밀번호

        btn_pw.setOnClickListener {
            if (pw_visible == false) {
                pw_text.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                pw_visible = true
            }
            else {
                pw_text.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                pw_visible = false
            }
        }


        btn_login.setOnClickListener {      // 로그인 버튼 클릭 메서드
            if (id_text.text.toString() == id && pw_text.text.toString() == pw) {    // 로그인 정보 일치 시
                val intent1 = Intent(this, MainViewActivity::class.java)
                startActivity(intent1)
            } else {                        // 로그인 정보 불일치 시
                pw_warning_text.visibility
            }


        }

        signUp_text.setOnClickListener {    // 회원가입 텍스트 클릭 메서드
            val intent2 = Intent(this, SignUpActivity1::class.java)
            startActivity(intent2)
        }
    }

//    private fun showVibrator(){         // 진동 애니메이션 구현 함수
//        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
//
//    }
}