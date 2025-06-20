package com.example.llm_project_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.properties.Delegates

class SignUpActivity2 : AppCompatActivity() {

    var is_Name_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }        // 아이디 생성 완료 여부
    var is_Birth_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }        // 비밀번호 생성 완료 여부
    var is_Phone_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }  // 비밀번호 확인 완료 여부
    var is_Gender_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }     // 이메일 생성 완료 여부
    var is_Married_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }     // 이메일 생성 완료 여부
    var is_Job_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }     // 이메일 생성 완료 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_up_view2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_back = findViewById<ImageButton>(R.id.backButton)       // 뒤로가기 버튼
        val btn_next = findViewById<Button>(R.id.next_Button)           // 다음 버튼
        val name = findViewById<EditText>(R.id.name_editText)           // 이름 입력란
        val birth = findViewById<EditText>(R.id.birth_editText)         // 생년월일 입력란
        val phone = findViewById<EditText>(R.id.phone_number_editText)  // 전화번호 입력란
        val gender_M = findViewById<RadioButton>(R.id.radioMale)        // 성별 (남)
        val gender_F = findViewById<RadioButton>(R.id.radioFemale)      // 성별 (여)
        val married_N = findViewById<RadioButton>(R.id.radioSingle)     // 결혼여부 (미혼)
        val married_Y = findViewById<RadioButton>(R.id.radioMarried)    // 결혼여부 (기혼)
        val job = findViewById<Spinner>(R.id.job_spinner)               // 직업 드롭다운

        // 이전 화면에서 받아온 데이터
        val id = intent.getStringExtra("id") ?: ""
        val pw = intent.getStringExtra("pw") ?: ""
        val email = intent.getStringExtra("email") ?: ""

        // 이름 생성
        create_name(name, { is_Name_Confirmed }, { is_Name_Confirmed = it })

        // 생년월일 생성
        create_birth()

        // 전화번호 생성

    }

    // 화면 전환간 데이터 수신 및 적용
    fun restorePassedData() {
        val data = getPassedStrings("name", "birth", "phone", "gender", "married", "job")
//        name.setText(data["name"] ?: "")
//        birth.setText(data["birth"] ?: "")
//        phone.setText(data["phone"] ?: "")
//        if (data["gender"] == 'M') {}
//        else {}
//
//        if (data["married"] == 'N') {}
//        else {}
//
//        if (data["job"] == '') {}
//        else {}
    }

    // '비밀번호' 생성 기능 함수
    fun create_pw(input_text: EditText, rule: TextView, check_text: EditText, getPwConfirmed: () -> Boolean, setPwIdConfirmed: (Boolean) -> Unit) {
        val pw_Pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,16}$")    // 영문, 숫자 (8-16자리)

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
                            setBoxField(input_text, "#666666".toColorInt())
                            check_text.isEnabled = false                                       // 비밀번호 확인란 비활성화
                            setPwIdConfirmed(false) }                                          // 다음 버튼 조건 미충족
                        isValid -> {                                                        // 비밀번호 사용 가능
                            setBoxField(input_text, "#4B9F72".toColorInt())
                            check_text.isEnabled = true                                        // 비밀번호 확인란 활성화
                            setPwIdConfirmed(true) }                                           // 다음 버튼 조건 충족
                        else -> {                                                           // 비밀번호 사용 불가
                            setBoxField(input_text, "#FF0000".toColorInt())
                            check_text.isEnabled = false                                       // 비밀번호 확인란 비활성화
                            setPwIdConfirmed(false) }                                          // 다음 버튼 조건 미충족
                    }
                }
            )
        )
    }

    // '이름' 생성 기능 함수
    fun create_name(input_text: EditText, getNameConfirmed: () -> Boolean, setNameConfirmed: (Boolean) -> Unit) {
        val name_Pattern = Regex("^(?:[가-힣]{2,6}|[A-Za-z]{2,30})$") // 이름 정규식 (한{2-6자} 혹은 영 {2-30자})

        // 이름 입력란 실시간 감지 이벤트
        input_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = input_text,
                validateInput = { input -> name_Pattern.matches(input_text.text.toString())},
                onValidStateChanged = { isValid ->
                    when {
                        input_text.text.toString().isEmpty() -> {       // 공란
                            setBoxField(input_text, "#1F70CC".toColorInt())
                            setNameConfirmed(false)
                        }
                        isValid -> {                                    // 이름 형식 충족
                            setBoxField(input_text, "#4B9F72".toColorInt())
                            setNameConfirmed(true)
                        }
                        else -> {                                       // 이름 형식 미충족
                            setBoxField(input_text, "#FF0000".toColorInt())
                            setNameConfirmed(false)
                        }
                    }
                }
            )
        )
    }

    // '생년월일' 생성 기능 함수
    fun create_birth(input_text: EditText, getBirthConfirmed: () -> Boolean, setBirthConfirmed: (Boolean) -> Unit) {
        val birth_Pattern = Regex("^(19[0-9]{2}|20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])$") // 생년월일 정규식 (YYYYMMDD)

        // 생년월일 입력란 실시간 감지 이벤트
        input_text.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = input_text
            )
        )
    }

    fun updateNextButton(){}
}