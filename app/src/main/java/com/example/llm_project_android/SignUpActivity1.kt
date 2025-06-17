package com.example.llm_project_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_up_view1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_back = findViewById<ImageButton>(R.id.backButton)
        val btn_eye = findViewById<ImageButton>(R.id.eyeButton1)
        val btn_eye_check = findViewById<ImageButton>(R.id.eyeButton2)
        val btn_idCheck = findViewById<Button>(R.id.checkButton)
        val id_text = findViewById<EditText>(R.id.id_editText)
        val pw_text = findViewById<EditText>(R.id.password_editText)
        val pw_check = findViewById<EditText>(R.id.check_editText)
        val email_text = findViewById<EditText>(R.id.email_editText)
        val id_rule = findViewById<TextView>(R.id.id_rule)
        val pw_rule = findViewById<TextView>(R.id.pw_rule)
        val pw_check_text = findViewById<TextView>(R.id.pw_check_text)
        val email_check = findViewById<TextView>(R.id.email_check)

        val id_test: String = "qwer1234"
        val pw_test: String = "qwer1234"
        var check_id: Boolean = false
        var check_pw: Boolean = false

        // 아이디 생성
        create_id(id_text, id_rule, id_test, btn_idCheck)

        // 비밀번호 생성
        create_pw()

        // 중복 확인 버튼 클릭 이벤트
//        btn_idCheck.setOnClickListener {
//            // 기존 아이디 존재
//            if (id_text.text.toString() == id_test) {
//                id_rule.setText("이미 존재하는 아이디입니다")
//                id_rule.setTextColor(0xFF0000)
//            } else {
//                id_rule.setText("사용 가능한 아이디입니다")
//                id_rule.setTextColor(0x1F70CC)
//            }
//        }
    }

    // 아이디 생성 기능 함수
    fun create_id(input: EditText, id_rule: TextView, id_test: String, btn_idCheck: Button) {
        val id_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,12}$")    // 영문, 숫자 (6-12자리)

        // 중복 확인 버튼 클릭 이벤트
        btn_idCheck.setOnClickListener {
            // 기존 아이디 존재
            if (input.text.toString() == id_test) {
                id_rule.setText("이미 존재하는 아이디입니다")
                id_rule.setTextColor(0xFF0000)
            } else {
                id_rule.setText("사용 가능한 아이디입니다")
                id_rule.setTextColor(0x1F70CC)
            }
        }

        // id_textView 실시간 감지 이벤트 (정규식에 대한 버튼 활성화 여부)
        input.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = id_rule,
                updateText = {"6~12자의 영문, 숫자를 사용하세요"},
                updateTextColor = {android.graphics.Color.parseColor("#1F70CC")},
                validateInput = {input -> id_Pattern.matches(input)},
                onValidStateChanged = {isValid -> btn_idCheck.isEnabled = isValid
                    btn_idCheck.setBackgroundResource(
                        if (isValid)
                            R.drawable.login_button // 사용 가능 상태 색
                        else
                            R.drawable.id_check_button  // 비활성 상태 색
                    )}))


    }

    // 비밀번호 생성 기능 함수
    fun create_pw() {
        val pw_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,12}$")    // 영문, 숫자 (8-20자리)

    }
}