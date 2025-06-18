package com.example.llm_project_android

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
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

        val btn_back = findViewById<ImageButton>(R.id.backButton)       // 뒤로 가기 버튼
        val btn_next = findViewById<Button>(R.id.next_Button)           // 다음 버튼
        val btn_eye = findViewById<ImageButton>(R.id.eyeButton1)        // 비밀번호 시각화 버튼
        val btn_eye_check = findViewById<ImageButton>(R.id.eyeButton2)  // 비밀번호 확인 시각화 버튼
        val btn_idCheck = findViewById<Button>(R.id.checkButton)        // 아이디 중복 확인 버튼
        val id_text = findViewById<EditText>(R.id.id_editText)          // 아이디 입력란
        val pw_text = findViewById<EditText>(R.id.password_editText)    // 비밀번호 입력란
        val pw_check = findViewById<EditText>(R.id.check_editText)      // 비밀번호 확인 입력란
        val email_text = findViewById<EditText>(R.id.email_editText)    // 이메일 입력란
        val id_rule = findViewById<TextView>(R.id.id_rule)              // 아이디 규칙 텍스트
        val pw_rule = findViewById<TextView>(R.id.pw_rule)              // 비밀번호 규칙 텍스트
        val pw_check_text = findViewById<TextView>(R.id.pw_check_text)  // 비밀번호 확인 여부 텍스트
        val email_check = findViewById<TextView>(R.id.email_check)      // 이메일 확인 여부 텍스트

        val id_test: String = "qwer1234"            // 테스트 아이디
        val pw_test: String = "qwer1234"            // 테스트 비밀번호
        var check_id: Boolean = false               // 아이디 중복 여부 (true: 존재, false: 비존재)
        var check_pw: Boolean = false               // 비밀번호 일치 여부 (true: 존재, false: 비존재)
        var pw_visible: Boolean = false             // 비밀번호 시각화 여부 (true: 시각화, false: 비시각화)
        var pw_check_visible: Boolean = false       // 비밀번호 확인 시각화 여부 (true: 시각화, false: 비시각화)
        var is_Id_Confirmed: Boolean = false        // 아이디 생성 완료 여부
        var is_Pw_Confirmed: Boolean = false        // 비밀번호 생성 완료 여부
        var is_Pw_Check_Confirmed: Boolean = false  // 비밀번호 확인 완료 여부
        var is_Email_Confirmed: Boolean = false     // 이메일 생성 완료 여부

        // 아이디 생성 (입력 text, 입력 상태, 존재하는 아이디, 중복 확인 버튼)
        create_id(id_text, id_rule, id_test, btn_idCheck)

        // 비밀번호 생성 (입력 text, 입력 상태)
        create_pw(pw_text, pw_rule, pw_check)

        // 비밀번호 확인 (입력된 비밀번호 동적 text, 비밀번호 입력란, 입력 text, 입력 상태)
        check_pw({ pw_text.text.toString() }, pw_text, pw_check, pw_check_text)



        // 비밀번호 & 비밀번호 확인란 시각화 버튼 클릭 이벤트
        pw_eye_visibility(btn_eye, pw_text, {pw_visible}, {pw_visible = it})
        pw_eye_visibility(btn_eye_check, pw_check, {pw_check_visible}, {pw_check_visible = it})
    }

    // 아이디 생성 기능 함수
    fun create_id(input_text: EditText, rule: TextView, test: String, idCheck: Button) {
        val id_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,12}$")    // 영문, 숫자 (6-12자리)
        var background = input_text.background as GradientDrawable   // 박스 테두리 변수
        var strokeWidth = TypedValue.applyDimension(            // 테두리 두께 변수 (1f=1dp)
            TypedValue.COMPLEX_UNIT_DIP, 1.5f, resources.displayMetrics
        ).toInt()

        // 중복 확인 버튼 클릭 이벤트
        idCheck.setOnClickListener {
            // 기존 아이디 존재
            if (input_text.text.toString() == test) {
                rule.setText("이미 존재하는 아이디입니다")
                rule.setTextColor("#FF0000".toColorInt())
                rule.startShakeAnimation(this)
                background.setStroke(strokeWidth, "#FF0000".toColorInt())
            } else {    // 존재x
                rule.setText("사용 가능한 아이디입니다")
                rule.setTextColor("#4B9F72".toColorInt())
                background.setStroke(strokeWidth, "#4B9F72".toColorInt())
            }
        }

        // 아이디 입력란 실시간 감지 이벤트 (정규식에 대한 버튼 활성화 여부)
        input_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = rule,
                updateText = { "6~12자의 영문, 숫자를 사용하세요" },
                updateTextColor = { "#1F70CC".toColorInt() },
                validateInput = { input -> id_Pattern.matches(input_text.text.toString()) },
                onValidStateChanged = { isValid -> idCheck.isEnabled = isValid
                    idCheck.setBackgroundResource(
                        if (isValid)    { R.drawable.login_button }     // 사용 가능 상태
                        else            { R.drawable.id_check_button }  // 비활성 상태
                    )
                    background.setStroke(strokeWidth, "#666666".toColorInt()) }))
    }

    // 비밀번호 생성 기능 함수
    fun create_pw(input_text: EditText, rule: TextView, check_text: EditText) {
        val pw_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,16}$")    // 영문, 숫자 (8-16자리)
        var background = input_text.background as GradientDrawable   // 박스 테두리 변수
        var strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 1.5f, resources.displayMetrics
        ).toInt()   // 테두리 두께 변수 (1f=1dp)

        // 비밀번호 입력란 실시간 감지 이벤트
        input_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = rule,
                updateText = { input ->
                    if (pw_Pattern.matches(input_text.text.toString())) "사용 가능한 비밀번호입니다"    // 비밀번호 정규식 만족
                    else "8~16자의 영문, 숫자를 사용하세요" },
                updateTextColor = { input ->
                    when {
                        input_text.text.toString().isEmpty() -> "#1F70CC".toColorInt()
                        pw_Pattern.matches(input) -> "#4B9F72".toColorInt()
                        else -> "#FF0000".toColorInt()
                    }},
                validateInput = { input -> pw_Pattern.matches(input_text.text.toString()) },
                onValidStateChanged = { isChecked ->
                    when {
                        input_text.text.toString().isEmpty() -> {
                            background.setStroke(strokeWidth, "#666666".toColorInt())
                            check_text.isEnabled = false }
                        isChecked -> {
                            background.setStroke(strokeWidth, "#4B9F72".toColorInt())
                            check_text.isEnabled = true }
                        else -> {
                            background.setStroke(strokeWidth, "#FF0000".toColorInt())
                            check_text.isEnabled = false}
                    }
                }
            )
        )
    }

    // 비밀번호 확인 함수
    fun check_pw(pw_text: () -> String, pw_window: EditText, input_text: EditText, check_text: TextView) {
        var background = input_text.background as GradientDrawable   // 박스 테두리 변수
        var strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 1.5f, resources.displayMetrics
        ).toInt()   // 테두리 두께 변수 (1f=1dp)

        // 비밀번호 확인 입력란 실시간 감지 이벤트
        input_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = check_text,
                updateText = { input ->
                    when {
                        input_text.text.toString().isEmpty() -> "동일한 암호를 입력하세요"    // 공란
                        pw_text() == input_text.text.toString() -> "비밀번호가 일치합니다"      // 비밀번호 일치
                        else -> "비밀번호가 일치하지 않습니다"                                 // 비밀번호 불일치
                    }},
                updateTextColor = { input ->
                    when {
                        input_text.text.toString().isEmpty() -> "#1F70CC".toColorInt()    // 공란
                        pw_text() == input_text.text.toString() -> "#4B9F72".toColorInt()   // 비밀번호 일치
                        else -> "#FF0000".toColorInt()                                    // 비밀번호 불일치
                    }},
                validateInput = { input -> pw_text() == input_text.text.toString() },
                onValidStateChanged = { isChecked ->
                    when {
                        input_text.text.toString(). isEmpty() -> {
                            background.setStroke(strokeWidth, "#666666".toColorInt())
                            pw_window.isEnabled = true}
                        isChecked -> {
                            background.setStroke(strokeWidth, "#4B9F72".toColorInt())
                            pw_window.isEnabled = false }
                        else -> {
                            background.setStroke(strokeWidth, "#FF0000".toColorInt())
                            pw_window.isEnabled = false }
                    }
                }
            )
        )
    }

    // 다음 버튼 클릭 조건 함수
    fun isAllConfirmed(is_Id_Confirmed: Boolean, is_Pw_Confirmed: Boolean, is_Pw_Check_Confirmed: Boolean, is_Email_Confirmed: Boolean): Boolean {
        return is_Id_Confirmed && is_Pw_Confirmed && is_Pw_Check_Confirmed && is_Email_Confirmed
    }
}