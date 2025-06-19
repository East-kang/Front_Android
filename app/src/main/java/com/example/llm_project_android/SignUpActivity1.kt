package com.example.llm_project_android

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.graphics.toColorInt
import kotlin.properties.Delegates

class SignUpActivity1 : AppCompatActivity() {

    private lateinit var btn_next: Button
    var is_Id_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }        // 아이디 생성 완료 여부
    var is_Pw_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }        // 비밀번호 생성 완료 여부
    var is_Pw_Check_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }  // 비밀번호 확인 완료 여부
    var is_Email_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }     // 이메일 생성 완료 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_up_view1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_next = findViewById<Button>(R.id.next_Button)               // 다음 버튼
        val btn_back = findViewById<ImageButton>(R.id.backButton)       // 뒤로 가기 버튼
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
        var check_id: Boolean = false               // 아이디 중복 여부 (true: 존재, false: 비존재)
        var check_pw: Boolean = false               // 비밀번호 일치 여부 (true: 존재, false: 비존재)
        var pw_visible: Boolean = false             // 비밀번호 시각화 여부 (true: 시각화, false: 비시각화)
        var pw_check_visible: Boolean = false       // 비밀번호 확인 시각화 여부 (true: 시각화, false: 비시각화)

        // 초기 버튼 비활성화
        updateNextButton()

        // 아이디 생성 (입력 text, 입력 상태, 존재하는 아이디, 중복 확인 버튼, 완료 상태)
        create_id(id_text, id_rule, id_test, btn_idCheck, { is_Id_Confirmed }, {is_Id_Confirmed = it})

        // 비밀번호 생성 (입력 text, 입력 상태, 완료 상태)
        create_pw(pw_text, pw_rule, pw_check, { is_Pw_Confirmed }, { is_Pw_Confirmed = it })

        // 비밀번호 확인 (입력된 비밀번호 동적 text, 비밀번호 입력란, 입력 text, 입력 상태 text, 완료 상태)
        check_pw({ pw_text.text.toString() }, pw_text, pw_check, pw_check_text, { is_Pw_Check_Confirmed }, { is_Pw_Check_Confirmed = it })

        // 이메일 생성 (입력 text, 생성 여부 text, 완료 상태)
        create_email(email_text, email_check, { is_Email_Confirmed }, { is_Email_Confirmed = it })

        // 비밀번호 & 비밀번호 확인란 시각화 버튼 클릭 이벤트
        pw_eye_visibility(btn_eye, pw_text, {pw_visible}, {pw_visible = it})
        pw_eye_visibility(btn_eye_check, pw_check, {pw_check_visible}, {pw_check_visible = it})

        // 뒤로가기 버튼 클릭 이벤트
        btn_back.setOnClickListener { navigateTo(InitActivity::class.java) }

        // 다음 버튼 클릭 이벤트
        btn_next.setOnClickListener {

        }

    }

    // 아이디 생성 기능 함수
    fun create_id(input_text: EditText, rule: TextView, test: String, idCheck: Button, getIdConfirmed: () -> Boolean, setIsIdConfirmed: (Boolean) -> Unit) {
        val id_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,12}$")    // 영문, 숫자 (6-12자리)
        var background = input_text.background as GradientDrawable   // 박스 테두리 변수
        var strokeWidth = TypedValue.applyDimension(            // 테두리 두께 변수 (1f=1dp)
            TypedValue.COMPLEX_UNIT_DIP, 1.5f, resources.displayMetrics
        ).toInt()

        // 중복 확인 버튼 클릭 이벤트
        idCheck.setOnClickListener {
            if (input_text.text.toString() == test) {   // 기존 아이디 존재
                rule.setText("이미 존재하는 아이디입니다")
                rule.setTextColor("#FF0000".toColorInt())
                rule.startShakeAnimation(this)
                background.setStroke(strokeWidth, "#FF0000".toColorInt())
                setIsIdConfirmed(false)
            } else {                                    // 존재x (사용 가능)
                rule.setText("사용 가능한 아이디입니다")
                rule.setTextColor("#4B9F72".toColorInt())
                background.setStroke(strokeWidth, "#4B9F72".toColorInt())
                setIsIdConfirmed(true)
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
                    setIsIdConfirmed(false)
                    idCheck.setBackgroundResource(
                        if (isValid)    { R.drawable.enabled_button }     // 사용 가능 상태
                        else            { R.drawable.disabled_button }   // 비활성 상태
                    )
                    background.setStroke(strokeWidth, "#666666".toColorInt()) }))
    }

    // 비밀번호 생성 기능 함수
    fun create_pw(input_text: EditText, rule: TextView, check_text: EditText, getPwConfirmed: () -> Boolean, setPwIdConfirmed: (Boolean) -> Unit) {
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
                onValidStateChanged = { isValid ->
                    when {
                        input_text.text.toString().isEmpty() -> {                           // 공란
                            background.setStroke(strokeWidth, "#666666".toColorInt())
                            check_text.isEnabled = false                                       // 비밀번호 확인란 비활성화
                            setPwIdConfirmed(false) }                                          // 다음 버튼 조건 미충족
                        isValid -> {                                                        // 비밀번호 사용 가능
                            background.setStroke(strokeWidth, "#4B9F72".toColorInt())
                            check_text.isEnabled = true                                        // 비밀번호 확인란 활성화
                            setPwIdConfirmed(true) }                                           // 다음 버튼 조건 충족
                        else -> {                                                           // 비밀번호 사용 불가
                            background.setStroke(strokeWidth, "#FF0000".toColorInt())
                            check_text.isEnabled = false                                       // 비밀번호 확인란 비활성화
                            setPwIdConfirmed(false) }                                          // 다음 버튼 조건 미충족
                    }
                }
            )
        )
    }

    // 비밀번호 확인 함수
    fun check_pw(pw_text: () -> String, pw_window: EditText, input_text: EditText, check_text: TextView, getPwCheckConfirmed: () -> Boolean, setPwCheckConfirmed: (Boolean) -> Unit) {
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
                        input_text.text.toString().isEmpty() -> "동일한 암호를 입력하세요"       // 공란
                        pw_text() == input_text.text.toString() -> "비밀번호가 일치합니다"      // 비밀번호 일치
                        else -> "비밀번호가 일치하지 않습니다"                                   // 비밀번호 불일치
                    }},
                updateTextColor = { input ->
                    when {
                        input_text.text.toString().isEmpty() -> "#1F70CC".toColorInt()      // 공란
                        pw_text() == input_text.text.toString() -> "#4B9F72".toColorInt()   // 비밀번호 일치
                        else -> "#FF0000".toColorInt()                                      // 비밀번호 불일치
                    }},
                validateInput = { input -> pw_text() == input_text.text.toString() },
                onValidStateChanged = { isValid ->
                    when {
                        input_text.text.toString(). isEmpty() -> {                             // 공란
                            background.setStroke(strokeWidth, "#666666".toColorInt())
                            pw_window.isEnabled = true                                           // 비밀번호 입력창 활성화
                            setPwCheckConfirmed(false) }                                         // 다음 버튼 클릭 조건 미충족
                        isValid -> {                                                         // 비밀번호 일치
                            background.setStroke(strokeWidth, "#4B9F72".toColorInt())
                            pw_window.isEnabled = false                                          // 비밀번호 입력창 비활성화
                            setPwCheckConfirmed(true) }                                          // 다음 버튼 클릭 조건 충족
                        else -> {                                                              // 비밀번호 불일치
                            background.setStroke(strokeWidth, "#FF0000".toColorInt())
                            pw_window.isEnabled = false                                          // 비밀번호 입력창 비활성화
                            setPwCheckConfirmed(false) }                                         // 다음 버튼 클릭 조건 미충족
                    }
                }
            )
        )
    }

    // 이메일 생성 기능 함수
    fun create_email(input_text: EditText, check: TextView, getEmailConfirmed: () -> Boolean, setEmailIsIdConfirmed: (Boolean) -> Unit) {
        val email_Pattern = Regex("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}\$")    // '계정@도메인.최상위도메인'
        var background = input_text.background as GradientDrawable   // 박스 테두리 변수
        var strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 1.5f, resources.displayMetrics
        ).toInt()   // 테두리 두께 변수 (1f=1dp)

        input_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = check,
                updateText = { input ->
                    when {
                        input_text.text.toString().isEmpty() -> "이메일을 정확히 입력해주세요"
                        !email_Pattern.matches(input_text.text.toString()) -> "사용 불가능한 이메일 형식입니다"
                        else -> "사용 가능한 이메일입니다"
                    }
                },
                updateTextColor = { input ->
                    when {
                        input_text.text.toString().isEmpty() -> "#1F70CC".toColorInt()                         // 공란
                        !email_Pattern.matches(input_text.text.toString()) -> "#FF0000".toColorInt()    // 이메일 형식 불일치
                        else -> "#4B9F72".toColorInt()                                                         // 사용 가능한 이메일 형식 일치
                    }
                },
                validateInput = { input -> email_Pattern.matches(input_text.text.toString()) },
                onValidStateChanged = { isValid ->
                    when {
                        input_text.text.toString().isEmpty() -> {                              // 공란
                            background.setStroke(strokeWidth, "#666666".toColorInt())
                            setEmailIsIdConfirmed(false) }
                        !isValid -> {         // 이메일 형식 불일치
                            background.setStroke(strokeWidth, "#FF0000".toColorInt())
                            setEmailIsIdConfirmed(false) }
                        else -> {                                                              // 사용 가능한 이메일 형식 일치
                            background.setStroke(strokeWidth, "#4B9F72".toColorInt())
                            setEmailIsIdConfirmed(true) }
                    }
                }
            )
        )
    }

    // 다음 버튼 클릭 조건 함수
    fun isAllConfirmed(is_Id_Confirmed: Boolean, is_Pw_Confirmed: Boolean, is_Pw_Check_Confirmed: Boolean, is_Email_Confirmed: Boolean): Boolean {
        return is_Id_Confirmed && is_Pw_Confirmed && is_Pw_Check_Confirmed && is_Email_Confirmed
    }

    // 다음 버튼 클릭 함수
    fun updateNextButton() {
        if (isAllConfirmed(is_Id_Confirmed, is_Pw_Confirmed, is_Pw_Check_Confirmed, is_Email_Confirmed)) {
            btn_next.isEnabled = true
            btn_next.setBackgroundResource(R.drawable.enabled_button)
        } else {
            btn_next.isEnabled = false
            btn_next.setBackgroundResource(R.drawable.disabled_button)
        }
    }
}