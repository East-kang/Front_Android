package com.example.llm_project_android

import android.content.res.Resources
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.properties.Delegates


class SignUpActivity2 : AppCompatActivity() {

    private lateinit var btn_next: Button
    private lateinit var name: EditText
    private lateinit var birth: EditText
    private lateinit var phone: EditText
    private lateinit var gender_M: RadioButton
    private lateinit var gender_F: RadioButton
    private lateinit var married_N: RadioButton
    private lateinit var married_Y: RadioButton
    private lateinit var job_spinner: Spinner

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

        btn_next = findViewById<Button>(R.id.next_Button)               // 다음 버튼
        name = findViewById<EditText>(R.id.name_editText)               // 이름 입력란
        birth = findViewById<EditText>(R.id.birth_editText)             // 생년월일 입력란
        phone = findViewById<EditText>(R.id.phone_number_editText)      // 전화번호 입력란
        gender_M = findViewById<RadioButton>(R.id.radioMale)            // 성별 (남)
        gender_F = findViewById<RadioButton>(R.id.radioFemale)          // 성별 (여)
        married_N = findViewById<RadioButton>(R.id.radioSingle)         // 결혼여부 (미혼)
        married_Y = findViewById<RadioButton>(R.id.radioMarried)        // 결혼여부 (기혼)
        job_spinner = findViewById<Spinner>(R.id.job_spinner)           // 직업 드롭다운

        val btn_back = findViewById<ImageButton>(R.id.backButton)       // 뒤로가기 버튼
        val married = findViewById<RadioGroup>(R.id.radioMaritalStatus) // 결혼 여부 체크 그룹
        val gender = findViewById<RadioGroup>(R.id.radioGender)         // 성별 체크 그룹

        // 초기 설정 (버튼 비활성화, 입력 값 초기화)
        updateNextButton()

        // 이전 화면에서 받아온 데이터
        val id = intent.getStringExtra("id") ?: ""
        val pw = intent.getStringExtra("pw") ?: ""
        val email = intent.getStringExtra("email") ?: ""

        // 화면 전환 간 데이터 유지 (SignUpAcitivity3.kt -> SignUpAcitivity2.kt)
        restorePassedData()

        // 이름 생성
        create_name(name, { is_Name_Confirmed }, { is_Name_Confirmed = it })

        // 생년월일 생성
        create_birth(birth, { is_Birth_Confirmed }, { is_Birth_Confirmed = it })

        // 전화번호 생성
        create_phone(phone, { is_Phone_Confirmed }, { is_Phone_Confirmed = it })
        phone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        // 성별 체크
        isChecked_gender(gender, { is_Gender_Confirmed = it})

        // 결혼 여부 체크
        isChecked_married(married, { is_Married_Confirmed = it})

        // 직업 선택
        var selectedJob: String? = null
        select_job(job_spinner, { job -> selectedJob }, { is_Job_Confirmed = it })


        // 뒤로가기 버튼 클릭 이벤트 (to SignUpActivity1)
        btn_back.setOnClickListener {
            navigateTo(
                SignUpActivity1::class.java,
                "id" to id,
                "pw" to pw,
                "email" to email
            )
        }

        // 다음 버튼 클릭 이벤트 (to SignUpActivity3)
        btn_next.setOnClickListener {
            val gender = if (gender_M.isChecked) "남자"  else "여자"
            val married = if (married_N.isChecked) "미혼"  else "기혼"
            val job: String = ""

            navigateTo(
                SignUpActivity3::class.java,
                "id" to id,
                "pw" to pw,
                "email" to email,
                "name" to name.text.toString(),
                "birth" to birth.text.toString(),
                "phone" to phone.text.toString(),
                "gender" to gender,
                "married" to married,
                "job" to job )
            }
    }

    // 화면 전환간 데이터 수신 및 적용
    fun restorePassedData() {
        val data = getPassedStrings("name", "birth", "phone", "gender", "married", "job")
        name.setText(data["name"] ?: "")
        birth.setText(data["birth"] ?: "")
        phone.setText(data["phone"] ?: "")

        if (data["gender"] == "남자") {
            gender_M.isChecked = true
            gender_F.isChecked = false
        } else {
            gender_M.isChecked = false
            gender_F.isChecked = true
        }

        if (data["married"] == "기혼") {
            married_N.isChecked = true
            married_Y.isChecked = false
        } else {
            married_N.isChecked = false
            married_Y.isChecked = true
        }

//        if (data["job"] == '') {}
//        else {}
    }

    // '이름' 생성 기능 함수
    fun create_name(input_text: EditText, getNameConfirmed: () -> Boolean, setNameConfirmed: (Boolean) -> Unit) {
        val name_Pattern = Regex("^(?:[가-힣]{2,6}|[A-Za-z]{2,30})$") // 이름 정규식 (한{2-6자} 혹은 영 {2-30자})

        // 이름 입력란 실시간 감지 이벤트
        input_text.addTextChangedListener(
            createFlexibleTextWatcher(
                validateInput = { input -> name_Pattern.matches(input_text.text.toString())},
                onValidStateChanged = { isValid ->
                    when {
                        input_text.text.toString().isEmpty() -> {       // 공란
                            setBoxField(input_text, "#666666".toColorInt())
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
        // 생년월일 입력란 실시간 감지 이벤트 (자동 슬래시 삽입용 TextWatcher)
        input_text.addTextChangedListener(
            createFlexibleTextWatcher(
                validateInput = { input -> birth_Pattern.matches(input) },
                onValidStateChanged = { isValid ->
                    when {
                        input_text.text.toString().isEmpty() -> {
                            setBoxField(input_text, "#666666".toColorInt())
                            setBirthConfirmed(false)
                        }
                        isValid -> {
                            setBoxField(input_text, "#4B9F72".toColorInt())
                            setBirthConfirmed(true)
                        }
                        else -> {
                            setBoxField(input_text, "#FF0000".toColorInt())
                            setBirthConfirmed(false)
                        }
                    }
                }
            )
        )
    }

    // '전화번호' 생성 기능 함수
    fun create_phone(input_text: EditText, getPhoneConfirmed: () -> Boolean, setPhoneConfirmed: (Boolean) -> Unit) {
        val phone_Pattern = Regex("^(010[0-9]{8})$") // 전화번호 정규식 (010-xxxx-xxxx)

        input_text.addTextChangedListener(
            createFlexibleTextWatcher(
                validateInput = { input -> phone_Pattern.matches(input) },
                onValidStateChanged = { isValid ->
                    when {
                        input_text.text.toString().isEmpty() -> {
                            setBoxField(input_text, "#666666".toColorInt())
                            setPhoneConfirmed(false)
                        }
                        isValid -> {
                            setBoxField(input_text, "#4B9F72".toColorInt())
                            setPhoneConfirmed(true)
                        }
                        else -> {
                            setBoxField(input_text, "#FF0000".toColorInt())
                            setPhoneConfirmed(false)
                        }
                    }
                }
            )
        )
    }

    // '성별' 체크 여부 확인 함수
    fun isChecked_gender(group: RadioGroup, setGenderConfirmed: (Boolean) -> Unit) {
        group.setOnCheckedChangeListener { _, isChecked ->
            setGenderConfirmed(isChecked != -1)
        }
    }

    // '결혼여부' 체크 여부 확인 함수
    fun isChecked_married(group: RadioGroup, setMarriedConfirmed: (Boolean) -> Unit) {
        group.setOnCheckedChangeListener { _, isChecked ->
            setMarriedConfirmed(isChecked != -1)
        }
    }

    // '직업' 선택 기능 함수
    fun select_job(spinner: Spinner, onJobSelected: (String) -> Unit, setJobConfirmed: (Boolean) -> Unit) {
        var job_list = resources.getStringArray(R.array.jobs)           // 직업 목록
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, job_list)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                val item = job_list[position]
                onJobSelected(item)
                if (position == 0) setJobConfirmed(false)
                else setJobConfirmed(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                setJobConfirmed(false)
            }
        }
    }

    // '다음' 버튼 클릭 조건 함수
    fun isAllConfirmed(Confirmed1: Boolean, Confirmed2: Boolean, Confirmed3: Boolean, Confirmed4: Boolean, Confirmed5: Boolean, Confirmed6: Boolean): Boolean {
        return Confirmed1 && Confirmed2 && Confirmed3 && Confirmed4 && Confirmed5 && Confirmed6
    }

    // '다음' 버튼 활성화 함수
    fun updateNextButton() {
        if (isAllConfirmed(is_Name_Confirmed, is_Birth_Confirmed, is_Phone_Confirmed, is_Gender_Confirmed, is_Married_Confirmed, is_Job_Confirmed)) {
            btn_next.isEnabled = true
            btn_next.setBackgroundResource(R.drawable.enabled_button)
        } else {
            btn_next.isEnabled = false
            btn_next.setBackgroundResource(R.drawable.disabled_button)
        }
    }
}