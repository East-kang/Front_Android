package com.example.llm_project_android.page.b_signup

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
import androidx.lifecycle.lifecycleScope
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.clearUserFields
import com.example.llm_project_android.functions.createFlexibleTextWatcher
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.getUserInfo
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.saveUserInfo
import com.example.llm_project_android.functions.setBoxField
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import kotlin.text.isNullOrEmpty


class SignUpActivity2 : AppCompatActivity() {

    private lateinit var btn_next: Button
    private lateinit var btn_back: ImageButton
    private lateinit var name: EditText
    private lateinit var birth: EditText
    private lateinit var phone: EditText
    private lateinit var job_etc: EditText
    private lateinit var married: RadioGroup
    private lateinit var gender:RadioGroup
    private lateinit var gender_M: RadioButton
    private lateinit var gender_F: RadioButton
    private lateinit var married_N: RadioButton
    private lateinit var married_Y: RadioButton
    private lateinit var job_spinner: Spinner

    private var selectedJob: String = ""
    private var source: String = ""
    private val job_Pattern = Regex("^[\\s가-힣]{2,20}$")

    private lateinit var job_list: Array<String>
    private lateinit var adapter: ArrayAdapter<String>

    var is_Name_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }     // 이름 생성 완료 여부
    var is_Birth_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }    // 생년월일 생성 완료 여부
    var is_Phone_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }    // 전화번호 생성 완료 여부
    var is_Gender_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }   // 성별 체크 완료 여부
    var is_Married_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }  // 결혼 체크 완료 여부
    var is_Job_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }      // 직업 선택 완료 여부
    var is_Etc_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }      // 기타 입력 완료 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.b_page_sign_up_view2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_next = findViewById<Button>(R.id.next_Button)               // 다음 버튼
        btn_back = findViewById<ImageButton>(R.id.backButton)           // 뒤로가기 버튼
        name = findViewById<EditText>(R.id.name_editText)               // 이름 입력란
        birth = findViewById<EditText>(R.id.birth_editText)             // 생년월일 입력란
        phone = findViewById<EditText>(R.id.phone_number_editText)      // 전화번호 입력란
        married = findViewById<RadioGroup>(R.id.radioMaritalStatus)     // 결혼 여부 체크 그룹
        gender = findViewById<RadioGroup>(R.id.radioGender)             // 성별 체크 그룹
        gender_M = findViewById<RadioButton>(R.id.radioMale)            // 성별 (남)
        gender_F = findViewById<RadioButton>(R.id.radioFemale)          // 성별 (여)
        married_N = findViewById<RadioButton>(R.id.radioSingle)         // 결혼여부 (미혼)
        married_Y = findViewById<RadioButton>(R.id.radioMarried)        // 결혼여부 (기혼)
        job_spinner = findViewById<Spinner>(R.id.job_spinner)           // 직업 드롭다운
        job_etc = findViewById<EditText>(R.id.job_etc)                  // 기타 직업 입력란

        // 초기 설정 (버튼 비활성화)
        updateNextButton()

        // 이전 화면에서 받아온 데이터
        source = intent.getStringExtra("source") ?: ""

        btn_next.isEnabled = true

        // 체크 박스 체크 취소
        gender.clearCheck()
        married.clearCheck()

        job_list = resources.getStringArray(R.array.jobs)           // 직업 목록
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, job_list)
        job_spinner.adapter = adapter

        // 이름 생성
        create_name({ is_Name_Confirmed }, { is_Name_Confirmed = it })

        // 생년월일 생성
        create_birth({ is_Birth_Confirmed }, { is_Birth_Confirmed = it })

        // 전화번호 생성
        create_phone({ is_Phone_Confirmed }, { is_Phone_Confirmed = it })

        // 성별 체크
        isChecked_gender({ is_Gender_Confirmed = it})

        // 결혼 여부 체크
        isChecked_married({ is_Married_Confirmed = it})

        // 직업 선택
        select_job({ job -> selectedJob = job }, { is_Job_Confirmed }, { is_Job_Confirmed = it }, { is_Etc_Confirmed }, { is_Etc_Confirmed = it })

        // 뒤로가기 버튼 클릭 이벤트 (to SignUpActivity1)
        clickBackButton(SignUpActivity1::class.java)

        // 다음 버튼 클릭 이벤트 (to SignUpActivity3)
        clickNextButton(SignUpActivity3::class.java)

        // 화면 전환 간 데이터 유지 (SignUpActivity3.kt -> SignUpActivity2.kt)
        restorePassedData()
    }

    // 화면 전환간 데이터 수신 및 적용
    fun restorePassedData() {
        lifecycleScope.launch {
            val user = getUserInfo(applicationContext)

            if (user != null && user.name.isNullOrBlank()
                && user.birthDate.isNullOrBlank() && user.phoneNumber.isNullOrBlank()
                && user.gender.isNullOrBlank() && user.job.isNullOrBlank()) {

                name.setText(user.name);    birth.setText(user.birthDate);    phone.setText(user.phoneNumber)

                if (user.gender == "남성") gender_M.isChecked = true
                else gender_F.isChecked = true

                if (user.isMarried) married_Y.isChecked = true
                else married_N.isChecked = true

                val job = user.job as? String?: ""
                val job_index = job_list.indexOf(job)

                // 유효성 플래그 설정
                is_Name_Confirmed = true;    is_Birth_Confirmed = true;    is_Phone_Confirmed = true;   is_Gender_Confirmed = true
                is_Married_Confirmed = true

                if (job in job_list) {
                    job_spinner.setSelection(job_index)
                    job_spinner.layoutParams.width = (200 * resources.displayMetrics.density).toInt()    // Spinner 너비 변환
                    job_etc.setText("")
                    job_etc.visibility = View.GONE
                    is_Job_Confirmed = true
                } else {
                    job_spinner.setSelection(job_list.lastIndex)
                    job_spinner.layoutParams.width = (70 * resources.displayMetrics.density).toInt()    // Spinner 너비 변환
                    job_etc.visibility = View.VISIBLE
                    job_etc.setText(job)
                    is_Job_Confirmed = false
                    is_Etc_Confirmed = job_Pattern.matches(job) // 직접 정규식 검증
                }
                updateNextButton()

                // 테두리 색상 (성공: 초록색)
                setBoxField(name, "#4B9F72".toColorInt())
                setBoxField(birth, "#4B9F72".toColorInt())
                setBoxField(phone, "#4B9F72".toColorInt())
            }
        }
    }

    // '이름' 생성 기능 함수
    fun create_name(getNameConfirmed: () -> Boolean, setNameConfirmed: (Boolean) -> Unit) {
        val name_Pattern = Regex("^(?:[가-힣]{2,6}|[A-Za-z]{2,30})$") // 이름 정규식 (한{2-6자} 혹은 영 {2-30자})

        // 이름 입력란 실시간 감지 이벤트
        name.addTextChangedListener(
            createFlexibleTextWatcher(
                validateInput = { input -> name_Pattern.matches(name.text.toString()) },
                onValidStateChanged = { isValid ->
                    when {
                        name.text.toString().isEmpty() -> {       // 공란
                            setBoxField(name, "#666666".toColorInt())
                            setNameConfirmed(false)
                        }

                        isValid -> {                                    // 이름 형식 충족
                            setBoxField(name, "#4B9F72".toColorInt())
                            setNameConfirmed(true)
                        }

                        else -> {                                       // 이름 형식 미충족
                            setBoxField(name, "#FF0000".toColorInt())
                            setNameConfirmed(false)
                        } } }))
    }

    // '생년월일' 생성 기능 함수
    fun create_birth(getBirthConfirmed: () -> Boolean, setBirthConfirmed: (Boolean) -> Unit) {
        val birth_Pattern = Regex("^(19[0-9]{2}|20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])$") // 생년월일 정규식 (YYYYMMDD)
        // 생년월일 입력란 실시간 감지 이벤트 (자동 슬래시 삽입용 TextWatcher)
        birth.addTextChangedListener(
            createFlexibleTextWatcher(
                validateInput = { input -> birth_Pattern.matches(input) },
                onValidStateChanged = { isValid ->
                    when {
                        birth.text.toString().isEmpty() -> {
                            setBoxField(birth, "#666666".toColorInt())
                            setBirthConfirmed(false)
                        }

                        isValid -> {
                            setBoxField(birth, "#4B9F72".toColorInt())
                            setBirthConfirmed(true)
                        }

                        else -> {
                            setBoxField(birth, "#FF0000".toColorInt())
                            setBirthConfirmed(false)
                        } } }))
    }

    // '전화번호' 생성 기능 함수
    fun create_phone(getPhoneConfirmed: () -> Boolean, setPhoneConfirmed: (Boolean) -> Unit) {
        val phone_Pattern = Regex("^(010-[0-9]{4}-[0-9]{4})$") // 전화번호 정규식 (010-xxxx-xxxx)

        phone.addTextChangedListener(
            createFlexibleTextWatcher(
                validateInput = { input -> phone_Pattern.matches(input) },
                onValidStateChanged = { isValid ->
                    when {
                        phone.text.toString().isEmpty() -> {
                            setBoxField(phone, "#666666".toColorInt())
                            setPhoneConfirmed(false)
                        }

                        isValid -> {
                            setBoxField(phone, "#4B9F72".toColorInt())
                            setPhoneConfirmed(true)
                        }

                        else -> {
                            setBoxField(phone, "#FF0000".toColorInt())
                            setPhoneConfirmed(false)
                        } } }))
    }

    // '성별' 체크 여부 확인 함수
    fun isChecked_gender(setGenderConfirmed: (Boolean) -> Unit) {
        gender.setOnCheckedChangeListener { _, isChecked ->
            setGenderConfirmed(isChecked != -1)
        }
    }

    // '결혼여부' 체크 여부 확인 함수
    fun isChecked_married(setMarriedConfirmed: (Boolean) -> Unit) {
        married.setOnCheckedChangeListener { _, isChecked ->
            setMarriedConfirmed(isChecked != -1)
        }
    }

    // '직업' 선택 기능 함수
    fun select_job(onJobSelected: (String) -> Unit, getJobSelected: () -> (Boolean), setJobConfirmed: (Boolean) -> Unit, getEtcSelected: () -> (Boolean), setEtcConfirmed: (Boolean) -> Unit) {
        job_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                val item = job_list[position]                           // item에 선택한 직업 할당
                onJobSelected(item)                                     // selectedJob 변수에 선택한 항목 저장
                if (position == 0) setJobConfirmed(false)               // '---직업을 선택해주세요---' 클릭 시, 선택 안된 것으로 간주
                else {                                                  // item을 선택한 경우
                    if (position == job_spinner.adapter.count - 1){
                        job_etc.visibility = View.VISIBLE
                        setJobConfirmed(false)
                        job_spinner.layoutParams.width = (70 * resources.displayMetrics.density).toInt()    // Spinner 너비 변환
                    } else {
                        job_etc.visibility = View.GONE
                        job_etc.setText("")
                        setJobConfirmed(true)
                        job_spinner.layoutParams.width = (200 * resources.displayMetrics.density).toInt()    // Spinner 너비 변환
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {   // 선택하지 않은 경우
                setJobConfirmed(false)
            }
        }

        job_etc.addTextChangedListener(
            createFlexibleTextWatcher(
                validateInput = { input -> job_Pattern.matches(input) },
                onValidStateChanged = { isValid ->
                    if (!isValid) setEtcConfirmed(false)
                    else setEtcConfirmed(true)
                }
            )
        )
    }

    // '뒤로가기' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickBackButton(targetActivity: Class<out AppCompatActivity>) {
        btn_back.setOnClickListener {
            lifecycleScope.launch {
                clearUserFields(
                    context = this@SignUpActivity2,
                    fieldsToClear = listOf("name", "birthDate", "phoneNumber", "gender", "isMarried", "job")
                )
            }
            navigateTo(
                targetActivity,
                "source" to source,
                reverseAnimation = true
            )
        }
    }

    // '다음' 버튼 활성화 함수
    fun updateNextButton() {
        if (is_Name_Confirmed && is_Birth_Confirmed && is_Phone_Confirmed && is_Gender_Confirmed && is_Married_Confirmed && (is_Job_Confirmed || is_Etc_Confirmed)) {
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
                    context = this@SignUpActivity2,
                    name = name.text.toString(),
                    birthDate = birth.text.toString(),
                    phoneNumber = phone.text.toString().replace("-", ""),
                    gender = if (gender_M.isChecked) "남성" else "여성",
                    isMarried = if (married_N.isChecked) false else true,
                    job = if (selectedJob == "기타") job_etc.text.toString() else selectedJob
                )
            }

            // 회원가입3 화면으로 이동
            navigateTo(
                targetActivity,
                "source" to source
            )
        }
    }
}