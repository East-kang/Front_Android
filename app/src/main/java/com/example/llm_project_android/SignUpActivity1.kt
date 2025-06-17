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
import androidx.core.graphics.toColorInt

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

        val id_test: String = "qwer1234"        // 테스트 아이디
        val pw_test: String = "qwer1234"        // 테스트 비밀번호
        var check_id: Boolean = false           // 아이디 중복 여부
        var check_pw: Boolean = false           // 비밀번호 일치 여부
        var pw_visible: Boolean = false         // 비밀번호 시각화 여부 (true: 시각화, false: 비시각화)
        var pw_check_visible: Boolean = false   // 비밀번호 확인 시각화 여부 (true: 시각화, false: 비시각화)

        // 아이디 생성
        create_id(id_text, id_rule, id_test, btn_idCheck)

        // 비밀번호 생성
        create_pw()

        // 비밀번호 시각화 버튼 클릭 이벤트
        btn_eye.setOnClickListener {
            var pos = pw_text.selectionStart            // 커서 위치 저장
            pw_visible = togglePasswordVisibility(pw_text, pw_visible, btn_eye)      // 비밀번호 시각화
            pw_text.post{ pw_text.setSelection(pos) }   // 커서 위치 복원
        }

        btn_eye_check.setOnClickListener {
            var pos = pw_text.selectionStart            // 커서 위치 저장
            pw_check_visible = togglePasswordVisibility(pw_check, pw_check_visible, btn_eye_check)      // 비밀번호 시각화
            pw_check.post{ pw_text.setSelection(pos) }   // 커서 위치 복원
        }
    }

    // 아이디 생성 기능 함수
    fun create_id(input: EditText, rule: TextView, test: String, idCheck: Button) {
        val id_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,12}$")    // 영문, 숫자 (6-12자리)

        // 중복 확인 버튼 클릭 이벤트
        idCheck.setOnClickListener {
            // 기존 아이디 존재
            if (input.text.toString() == test) {
                rule.setText("이미 존재하는 아이디입니다")
                rule.setTextColor("#FF0000".toColorInt())
            } else {
                rule.setText("사용 가능한 아이디입니다")
                rule.setTextColor("#1F70CC".toColorInt())
            }
        }

        // id_textView 실시간 감지 이벤트 (정규식에 대한 버튼 활성화 여부)
        input.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = rule,
                updateText = {"6~12자의 영문, 숫자를 사용하세요"},
                updateTextColor = {android.graphics.Color.parseColor("#1F70CC")},
                validateInput = {input -> id_Pattern.matches(input)},
                onValidStateChanged = {isValid -> idCheck.isEnabled = isValid
                    idCheck.setBackgroundResource(
                        if (isValid)    { R.drawable.login_button }     // 사용 가능 상태
                        else            { R.drawable.id_check_button }  // 비활성 상태
                    )}))
    }

    // 비밀번호 생성 기능 함수
    fun create_pw(input: EditText, rule: TextView, test: String, pwCheck: Button) {
        val pw_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,12}$")    // 영문, 숫자 (8-20자리)

    }

    fun pw_eye_visibility (btn: ImageButton, text: EditText, get_visible: () -> Boolean, set_visible: (Boolean) -> Unit) {
        btn.setOnClickListener {
            val pos = text.selectionStart           // 커서 위치 저장
            val newVisible = (togglePasswordVisibility(text, get_visible(), btn))      // 비밀번호 시각화
            set_visible(newVisible)                 // Boolean 값 업데이트
            text.post{ text.setSelection(pos) }     // 커서 위치 복원
        }
    }
}