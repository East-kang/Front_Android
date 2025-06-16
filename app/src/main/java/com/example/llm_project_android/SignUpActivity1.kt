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

        val id_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,12}$")    // 영문, 숫자 (6-12자리)
        val pw_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,12}$")    // 영문, 숫자 (8-20자리)

        // id_textView 실시간 감지 이벤트 (버튼 활성화 여부)
        id_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = id_rule,
                updateText = {"6~12자의 영문, 숫자를 사용하세요"},
                updateColor = {android.graphics.Color.parseColor("#1F70CC")},
                validateInput = {input -> id_Pattern.matches(input)},
                onValidStateChanged = {isValid -> btn_idCheck.isEnabled = isValid
                    btn_idCheck.setBackgroundColor(
                        if (isValid)
                            android.graphics.Color.parseColor("#50AAFA")  // 사용 가능 상태 색
                        else
                            android.graphics.Color.parseColor("#666666")  // 비활성 상태 색
                    )}))

        btn_idCheck.setOnClickListener {
            id_rule.setText("ㅇㅋㅇㅋㅂ")
        }

    }
}