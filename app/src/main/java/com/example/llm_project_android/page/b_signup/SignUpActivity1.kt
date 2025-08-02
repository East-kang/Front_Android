package com.example.llm_project_android.page.b_signup

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.clearUserFields
import com.example.llm_project_android.functions.createFlexibleTextWatcher
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.getUserInfo
import com.example.llm_project_android.functions.handleTouchOutsideEditText
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.pw_eye_visibility
import com.example.llm_project_android.functions.saveUserInfo
import com.example.llm_project_android.functions.setBoxField
import com.example.llm_project_android.functions.startShakeAnimation
import com.example.llm_project_android.page.a_intro.InitActivity
import com.example.llm_project_android.page.a_intro.LoginActivity
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class SignUpActivity1 : AppCompatActivity() {

    private lateinit var btn_next: Button
    private lateinit var btn_idCheck: Button
    private lateinit var btn_back: ImageButton
    private lateinit var btn_eye:ImageButton
    private lateinit var btn_eye_check: ImageButton
    private lateinit var id_text: EditText
    private lateinit var pw_text: EditText
    private lateinit var pw_check: EditText
    private lateinit var email_text: EditText
    private lateinit var id_rule: TextView
    private lateinit var pw_rule: TextView
    private lateinit var pw_check_text: TextView
    private lateinit var email_check: TextView

    var is_Id_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }        // 아이디 생성 완료 여부
    var is_Pw_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }        // 비밀번호 생성 완료 여부
    var is_Pw_Check_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }  // 비밀번호 확인 완료 여부
    var is_Email_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }     // 이메일 생성 완료 여부

    private val id_test: String = "qwer1234"    // 테스트 아이디
    var source: String = ""                     // 이전 화면 소스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.b_page_sign_up_view1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_next = findViewById<Button>(R.id.next_Button)               // 다음 버튼
        btn_idCheck = findViewById<Button>(R.id.checkButton)            // 아이디 중복 확인 버튼
        btn_back = findViewById<ImageButton>(R.id.backButton)           // 뒤로 가기 버튼
        btn_eye = findViewById<ImageButton>(R.id.eyeButton1)            // 비밀번호 시각화 버튼
        btn_eye_check = findViewById<ImageButton>(R.id.eyeButton2)      // 비밀번호 확인 시각화 버튼

        id_text = findViewById<EditText>(R.id.id_editText)              // 아이디 입력란
        pw_text = findViewById<EditText>(R.id.password_editText)        // 비밀번호 입력란
        pw_check = findViewById<EditText>(R.id.check_editText)          // 비밀번호 확인 입력란
        email_text = findViewById<EditText>(R.id.email_editText)        // 이메일 입력란

        id_rule = findViewById<TextView>(R.id.id_rule)                  // 아이디 규칙 텍스트
        pw_rule = findViewById<TextView>(R.id.pw_rule)                  // 비밀번호 규칙 텍스트
        pw_check_text = findViewById<TextView>(R.id.pw_check_text)      // 비밀번호 확인 여부 텍스트
        email_check = findViewById<TextView>(R.id.email_check)          // 이메일 확인 여부 텍스트


        var check_id: Boolean = false               // 아이디 중복 여부 (true: 존재, false: 비존재)
        var check_pw: Boolean = false               // 비밀번호 일치 여부 (true: 존재, false: 비존재)
        var pw_visible: Boolean = false             // 비밀번호 시각화 여부 (true: 시각화, false: 비시각화)
        var pw_check_visible: Boolean = false       // 비밀번호 확인 시각화 여부 (true: 시각화, false: 비시각화)

        val extras = getPassedExtras("source", String::class.java)
        source = extras["source"] as? String ?: ""  // source 값 intent에서 수신

        // 초기 설정 (버튼 비활성화, 입력 값 초기화)
        updateNextButton()

        // 아이디 생성
        create_id({ is_Id_Confirmed }, {is_Id_Confirmed = it})

        // 비밀번호 생성
        create_pw({ is_Pw_Confirmed }, { is_Pw_Confirmed = it })

        // 비밀번호 확인
        check_pw({ pw_text.text.toString() }, { is_Pw_Check_Confirmed }, { is_Pw_Check_Confirmed = it })

        // 이메일 생성
        create_email(email_check, { is_Email_Confirmed }, { is_Email_Confirmed = it })

        // 비밀번호 & 비밀번호 확인란 시각화 버튼 클릭 이벤트
        pw_eye_visibility(btn_eye, pw_text, { pw_visible }, { pw_visible = it })
        pw_eye_visibility(btn_eye_check, pw_check, { pw_check_visible }, { pw_check_visible = it })

        // 뒤로가기 이벤트 (to InitActivity or LoginActivity)
        clickBackButton()

        // 다음 버튼 클릭 이벤트 (to SignUpActivity2)
        clickNextButton(SignUpActivity2::class.java)

        // 화면 전환으로 인한 데이터 수신
        restorePassedData()
    }

    // 화면 전환간 데이터 수신 및 적용
    fun restorePassedData() {
        lifecycleScope.launch {
            val user = getUserInfo(applicationContext)

            // 데이터 필드가 모두 유효할 경우에만 복원
            if (user != null && user.userId.isNotBlank() && user.password.isNotBlank() && user.email.isNotBlank()) {

                id_text.setText(user.userId);   pw_text.setText(user.password);   pw_check.setText(user.password);   email_text.setText(user.email)

                // 유효성 플래그 설정
                is_Id_Confirmed = true;         is_Pw_Confirmed = true;           is_Pw_Check_Confirmed = true;      is_Email_Confirmed = true

                // 테두리 색상 설정 및 안내 문구 업데이트s
                setBoxField(id_text, "#4B9F72".toColorInt());   setBoxField(pw_text, "#4B9F72".toColorInt())
                setBoxField(pw_check, "#4B9F72".toColorInt());  setBoxField(email_text, "#4B9F72".toColorInt())

                id_rule.text = "사용 가능한 아이디입니다";         id_rule.setTextColor("#4B9F72".toColorInt())
                pw_rule.text = "사용 가능한 비밀번호입니다";       pw_rule.setTextColor("#4B9F72".toColorInt())
                pw_check_text.text = "비밀번호가 일치합니다";     pw_check_text.setTextColor("#4B9F72".toColorInt())
                email_check.text = "사용 가능한 이메일입니다";     email_check.setTextColor("#4B9F72".toColorInt())

                // 버튼 및 필드 상태 설정
                updateNextButton()
                pw_text.isEnabled = false;  pw_check.isEnabled = true
            }
        }
    }

    // '아이디' 생성 기능 함수
    fun create_id(getIdConfirmed: () -> Boolean, setIsIdConfirmed: (Boolean) -> Unit) {
        val id_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,12}$")    // 영문, 숫자 (6-12자리)

        // 중복 확인 버튼 클릭 이벤트
        btn_idCheck.setOnClickListener {
            if (id_text.text.toString() == id_test) {   // 기존 아이디 존재
                id_rule.setText("이미 존재하는 아이디입니다")
                id_rule.setTextColor("#FF0000".toColorInt())
                id_rule.startShakeAnimation(this)
                setBoxField(id_text, "#FF0000".toColorInt())
                setIsIdConfirmed(false)
            } else {                                    // 존재x (사용 가능)
                id_rule.setText("사용 가능한 아이디입니다")
                id_rule.setTextColor("#4B9F72".toColorInt())
                setBoxField(id_text, "#4B9F72".toColorInt())
                setIsIdConfirmed(true)
            }
        }

        // 아이디 입력란 실시간 감지 이벤트 (정규식에 대한 버튼 활성화 여부)
        id_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = id_rule,
                updateText = { "6~12자의 영문, 숫자를 사용하세요" },
                updateTextColor = { "#1F70CC".toColorInt() },
                validateInput = { input -> id_Pattern.matches(id_text.text.toString()) },
                onValidStateChanged = { isValid ->
                    btn_idCheck.isEnabled = isValid
                    setIsIdConfirmed(false)
                    btn_idCheck.setBackgroundResource(
                        if (isValid) {
                            R.drawable.design_enabled_button
                        }     // 사용 가능 상태
                        else {
                            R.drawable.design_disabled_button
                        }   // 비활성 상태
                    )
                    setBoxField(id_text, "#666666".toColorInt())
                })
        )
    }

    // '비밀번호' 생성 기능 함수
    fun create_pw(getPwConfirmed: () -> Boolean, setPwIdConfirmed: (Boolean) -> Unit) {
        val pw_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,16}$")    // 영문, 숫자 (8-16자리)

        // 비밀번호 입력란 실시간 감지 이벤트
        pw_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = pw_rule,
                updateText = { input ->
                    if (pw_Pattern.matches(pw_text.text.toString())) "사용 가능한 비밀번호입니다"    // 비밀번호 정규식 만족
                    else "8~16자의 영문, 숫자를 사용하세요"
                },
                updateTextColor = { input ->
                    when {
                        pw_text.text.toString().isEmpty() -> "#1F70CC".toColorInt()
                        pw_Pattern.matches(input) -> "#4B9F72".toColorInt()
                        else -> "#FF0000".toColorInt()
                    }
                },
                validateInput = { input -> pw_Pattern.matches(pw_text.text.toString()) },
                onValidStateChanged = { isValid ->
                    when {
                        pw_text.text.toString().isEmpty() -> {                           // 공란
                            setBoxField(pw_text, "#666666".toColorInt())
                            pw_check.isEnabled = false                                   // 비밀번호 확인란 비활성화
                            setPwIdConfirmed(false)
                        }                                          // 다음 버튼 조건 미충족
                        isValid -> {                                                     // 비밀번호 사용 가능
                            setBoxField(pw_text, "#4B9F72".toColorInt())
                            pw_check.isEnabled = true                                    // 비밀번호 확인란 활성화
                            setPwIdConfirmed(true)
                        }                                           // 다음 버튼 조건 충족
                        else -> {                                                        // 비밀번호 사용 불가
                            setBoxField(pw_text, "#FF0000".toColorInt())
                            pw_check.isEnabled = false                                   // 비밀번호 확인란 비활성화
                            setPwIdConfirmed(false)
                        }
                    }
                }
            )
        )
    }

    // '비밀번호' 확인 함수
    fun check_pw(pw: () -> String, getPwCheckConfirmed: () -> Boolean, setPwCheckConfirmed: (Boolean) -> Unit) {
        // 비밀번호 확인 입력란 실시간 감지 이벤트
        pw_check.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = pw_check_text,
                updateText = { input ->
                    when {
                        pw_check.text.toString().isEmpty() -> "동일한 암호를 입력하세요"       // 공란
                        pw() == pw_check.text.toString() -> "비밀번호가 일치합니다"            // 비밀번호 일치
                        else -> "비밀번호가 일치하지 않습니다"                                  // 비밀번호 불일치
                    }
                },
                updateTextColor = { input ->
                    when {
                        pw_check.text.toString().isEmpty() -> "#1F70CC".toColorInt()       // 공란
                        pw() == pw_check.text.toString() -> "#4B9F72".toColorInt()         // 비밀번호 일치
                        else -> "#FF0000".toColorInt()                                     // 비밀번호 불일치
                    }
                },
                validateInput = { input -> pw() == pw_check.text.toString() },
                onValidStateChanged = { isValid ->
                    when {
                        pw_check.text.toString().isEmpty() -> {                             // 공란
                            setBoxField(pw_check, "#666666".toColorInt())
                            pw_text.isEnabled = true                                        // 비밀번호 입력창 활성화
                            setPwCheckConfirmed(false)
                        }                                         // 다음 버튼 클릭 조건 미충족
                        isValid -> {                                                         // 비밀번호 일치
                            setBoxField(pw_check, "#4B9F72".toColorInt())
                            pw_text.isEnabled = false                                        // 비밀번호 입력창 비활성화
                            setPwCheckConfirmed(true)
                        }                                          // 다음 버튼 클릭 조건 충족
                        else -> {                                                            // 비밀번호 불일치
                            setBoxField(pw_check, "#FF0000".toColorInt())
                            pw_text.isEnabled = false                                        // 비밀번호 입력창 비활성화
                            setPwCheckConfirmed(false)
                        }
                    }
                }
            )
        )
    }

    // '이메일' 생성 기능 함수
    fun create_email(check: TextView, getEmailConfirmed: () -> Boolean, setEmailIsIdConfirmed: (Boolean) -> Unit) {
        val email_Pattern = Regex("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}\$")    // '계정@도메인.최상위도메인'

        email_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = check,
                updateText = { input ->
                    when {
                        email_text.text.toString().isEmpty() -> "이메일을 정확히 입력해주세요"
                        !email_Pattern.matches(email_text.text.toString()) -> "사용 불가능한 이메일 형식입니다"
                        else -> "사용 가능한 이메일입니다"
                    }
                },
                updateTextColor = { input ->
                    when {
                        email_text.text.toString()
                            .isEmpty() -> "#1F70CC".toColorInt()        // 공란
                        !email_Pattern.matches(email_text.text.toString()) -> "#FF0000".toColorInt()    // 이메일 형식 불일치
                        else -> "#4B9F72".toColorInt()                  // 사용 가능한 이메일 형식 일치
                    }
                },
                validateInput = { input -> email_Pattern.matches(email_text.text.toString()) },
                onValidStateChanged = { isValid ->
                    when {
                        email_text.text.toString().isEmpty() -> {       // 공란
                            setBoxField(email_text, "#666666".toColorInt())
                            setEmailIsIdConfirmed(false)
                        }
                        !isValid -> {       // 이메일 형식 불일치
                            setBoxField(email_text, "#FF0000".toColorInt())
                            setEmailIsIdConfirmed(false)
                        }
                        else -> {           // 사용 가능한 이메일 형식 일치
                            setBoxField(email_text, "#4B9F72".toColorInt())
                            setEmailIsIdConfirmed(true)
                        } } }))
    }

    // 뒤로가기 이벤트 정의 함수
    fun AppCompatActivity.clickBackButton() {
        // 뒤로가기 버튼 클릭
        btn_back.setOnClickListener {
            lifecycleScope.launch {
                clearUserFields(
                    context = this@SignUpActivity1,
                    fieldsToClear = listOf("userId", "password", "email")
                )
            }
            when (source) {
                "InitActivity" -> navigateTo(InitActivity::class.java, reverseAnimation = true)   // 초기화된 화면
                "LoginActivity" -> navigateTo(LoginActivity::class.java, reverseAnimation = true) // 값 유지된 화면
                else -> finish()
            }
        }

        // 기기 내장 뒤로가기 버튼 클릭
        onBackPressedDispatcher.addCallback(this) {
            lifecycleScope.launch {
                clearUserFields(
                    context = this@SignUpActivity1,
                    fieldsToClear = listOf("userId", "password", "email")
                )
            }
            when (source) {
                "InitActivity" -> navigateTo(InitActivity::class.java, reverseAnimation = true)   // 초기화된 화면
                "LoginActivity" -> navigateTo(LoginActivity::class.java, reverseAnimation = true) // 값 유지된 화면
                else -> finish()
            }
        }
    }

    // '다음' 버튼 클릭 조건 함수
    fun isAllConfirmed(Confirmed1: Boolean, Confirmed2: Boolean, Confirmed3: Boolean, Confirmed4: Boolean): Boolean {
        return Confirmed1 && Confirmed2 && Confirmed3 && Confirmed4
    }

    // '다음' 버튼 활성화 함수
    fun updateNextButton() {
        if (isAllConfirmed(is_Id_Confirmed, is_Pw_Confirmed, is_Pw_Check_Confirmed, is_Email_Confirmed)) {
            btn_next.isEnabled = true
            btn_next.setBackgroundResource(R.drawable.design_enabled_button)
        } else {
            btn_next.isEnabled = false
            btn_next.setBackgroundResource(R.drawable.design_disabled_button)
        }
    }

    // '다음' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickNextButton(targetActivity: Class<out AppCompatActivity>) {
        btn_next.setOnClickListener {
            // 코루틴으로 DB 저장
            lifecycleScope.launch {
                saveUserInfo(
                    context = this@SignUpActivity1,
                    userId = id_text.text.toString(),
                    password = pw_text.text.toString(),
                    email = email_text.text.toString()
                )
            }

            // 회원가입2 화면으로 이동
            navigateTo(
                targetActivity,
                "source" to source
            )

        }
    }

    // 키보드 숨기기 이벤트 (editText 이외의 영역을 눌렀을 경우, 스크롤 제외)
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        handleTouchOutsideEditText(this, ev)
        return super.dispatchTouchEvent(ev)
    }
}