package com.example.llm_project_android.page.d_menu

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.llm_project_android.R
import com.example.llm_project_android.db.MyDatabase
import com.example.llm_project_android.functions.createFlexibleTextWatcher
import com.example.llm_project_android.functions.handleTouchOutsideEditText
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.saveUserInfo
import com.example.llm_project_android.functions.showConfirmDialog
import com.example.llm_project_android.functions.showErrorDialog
import com.example.llm_project_android.page.c_product.MainViewActivity
import kotlinx.coroutines.launch

class ProfileView : AppCompatActivity() {

    private lateinit var btn_back: ImageButton
    private lateinit var btn_edit: Button
    private lateinit var btn_cancel: Button

    private lateinit var user_id: TextView
    private lateinit var user_name: TextView
    private lateinit var user_gender: TextView
    private lateinit var user_married: TextView
    private lateinit var user_job: TextView
    private lateinit var pw_rule: TextView
    private lateinit var email_rule: TextView
    private lateinit var birth_rule: TextView
    private lateinit var phone_rule: TextView
    private lateinit var job_rule: TextView

    private lateinit var pw_field: ConstraintLayout
    private lateinit var line: View

    private lateinit var user_pw: EditText
    private lateinit var user_email: EditText
    private lateinit var user_birth: EditText
    private lateinit var user_phone: EditText
    private lateinit var job_etc: EditText

    private lateinit var group_gender: RadioGroup
    private lateinit var group_married: RadioGroup

    private lateinit var radio_male: RadioButton
    private lateinit var radio_female: RadioButton
    private lateinit var radio_single: RadioButton
    private lateinit var radio_married: RadioButton

    private lateinit var job_spinner: Spinner

    private var edit_state: Boolean = false         // 편집 여부 상태 변수 (true: 편집 중, false: 편집x)

    private var isPasswordValid: Boolean = true     // 비밀번호 정규식 조건 만족 유효성 (true: 정규식 만족, false: 정규식 불만족)
    private var isEmailValid: Boolean = true        // 이메일 정규식 조건 만족 유효성
    private var isBirthValid: Boolean = true        // 생년월일 정규식 조건 만족 유효성
    private var isPhoneValid: Boolean = true        // 이메일 정규식 조건 만족 유효성
    private var isJobValid: Boolean = true          // 기타 직업명 정규식 조건 만족 유효성
    private var isJobEtcValid: Boolean = true       // 기타 직업명 정규식 조건 만족 유효성


    private var isPasswordChanged: Boolean = false  // 비밀번호 변경 여부
    private var isEmailChanged: Boolean = false     // 이메일 변경 여부
    private var isBirthChanged: Boolean = false     // 생년월일 변경 여부
    private var isPhoneChanged: Boolean = false     // 전화번호 변경 여부
    private var isGenderChanged: Boolean = false    // 성별 변경 여부
    private var isMarriedChanged: Boolean = false   // 결혼 여부 변경 여부
    private var isJobChanged: Boolean = false       // 직업 변경 여부
    private var isJobEtcChanged: Boolean = false    // 직업 변경 여부

    private var isJobRule: Boolean = false

    private lateinit var job_list: Array<String>
    private lateinit var adapter: ArrayAdapter<String>

    private var id: String = ""
    private var pw: String = ""
    private var email: String = ""
    private var name: String = ""
    private var birth: Int = 0
    private var phone: String = ""
    private var gender: String = ""
    private var married: Boolean = false
    private var job: String = ""

    private var new_job: String = ""                // 변경할 직업 값


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.d_page_profile_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_back = findViewById(R.id.backButton)        // 뒤로가기 버튼 (ImageButton)
        btn_edit = findViewById(R.id.editButton)        // 편집(완료) 버튼 (Button)
        btn_cancel = findViewById(R.id.cancelButton)    // 편집 취소 버튼 (Button)

        user_id = findViewById(R.id.user_id)            // 유저 아이디 (TextView)
        user_name = findViewById(R.id.user_name)        // 유저 이름 (TextView)
        user_gender = findViewById(R.id.user_gender)    // 유저 성별 (TextView)
        user_married = findViewById(R.id.user_married)  // 유저 결혼 여부 (TextView)
        user_job = findViewById(R.id.user_job)          // 유저 직업 (TextView)
        pw_rule = findViewById(R.id.user_pw_rule)       // 비밀번호 안내 문구 (TextView)
        email_rule = findViewById(R.id.user_email_rule) // 이메일 안내 문구 (TextView)
        birth_rule = findViewById(R.id.user_birth_rule) // 생년월일 안내 문구 (TextView)
        phone_rule = findViewById(R.id.user_phone_rule) // 전화번호 안내 문구(TextView)
        job_rule = findViewById(R.id.user_job_rule)     // 직업 안내 문구(TextView)

        pw_field = findViewById(R.id.pw_Field)          // 비밀번호 레이아웃 (ConstraintLayout)
        line = findViewById(R.id.line1_2)               // 비밀번호 분리선 (View)

        user_pw = findViewById(R.id.user_pw)            // 비밀번호 편집 (EditText)
        user_email = findViewById(R.id.user_email)      // 이메일 편집 (EditText)
        user_birth = findViewById(R.id.user_birth)      // 생년월일 편집 (EditText)
        user_phone = findViewById(R.id.user_phone)      // 전화번호 편집 (EditText)
        job_etc = findViewById(R.id.job_etc)            // 기타 직업 입력란 (EditText)

        group_gender = findViewById(R.id.gender_Group)  // 성별 그룹 (RadioGroup)
        group_married = findViewById(R.id.married_Group)// 결혼 여부 그룹 (RadioGroup)

        radio_male = findViewById(R.id.radioMale)       // 남성 체크 박스 (RadioButton)
        radio_female = findViewById(R.id.radioFemale)   // 여성 체크 박스 (RadioButton)
        radio_single = findViewById(R.id.radioSingle)   // 미혼 체크 박스 (RadioGroup)
        radio_married = findViewById(R.id.radioMarried) // 기혼 체크 박스 (RadioGroup)

        job_spinner = findViewById(R.id.job_spinner)    // 직업 선택 박스 (Spinner)

        job_list = resources.getStringArray(R.array.jobs)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, job_list)
        job_spinner.adapter = adapter

        loadData()              // 프로필 값 불러오기

        click_BackButton()      // 뒤로가기 버튼 클릭 이벤트
        click_EditButton()      // 편집/완료 버튼 클릭 이벤트
        click_CancelButton()    // 취소 버튼 클릭 이벤트

        edit_password()         // 비밀번호 변경 이벤트
        edit_email()            // 이메일 변경 이벤트
        edit_birth()            // 생년월일 변경 이벤트
        edit_phone()            // 전화번호 변경 이벤트
        edit_gender()           // 성별 체크 이벤트
        edit_married()          // 결혼 여부 체크 이벤트
        edit_job()              // 직업 선택 이벤트

    }

    // 내부 DB 데이터 불러오기
    fun loadData() {
        lifecycleScope.launch {
            val dao = MyDatabase.getDatabase(this@ProfileView).getMyDao()
            val user = dao.getLoggedInUser()

            user?.let {
                id = it.userId
                pw = it.password
                email = it.email
                name = it.name
                birth = it.birthDate.toIntOrNull() ?: 0
                phone = it.phoneNumber
                gender = it.gender
                married = it.isMarried
                job = it.job

                // 데이터 로드 후 UI 반영
                init_Profile()          // 초기 프로필 상태 불러오기
                Log.d("처음", "it.isMarried: " + it.isMarried)
                Log.d("처음", "married: " + it.isMarried)
            }
        }
    }

    // 내부 DB로 데이터 저장하기
    fun storeData() {
        lifecycleScope.launch {
            saveUserInfo(
                context = this@ProfileView,
                password = pw,
                email = email,
                birthDate = birth.toString(),
                phoneNumber = phone.replace("-", ""),
                gender = gender,
                isMarried = married,
                job = job
            )
        }
    }

    // 초기 프로필 데이터 적용 함수
    private fun init_Profile() {
        user_id.setText(id)
        user_pw.setText(pw)
        user_email.setText(email)
        user_name.setText(name)
        user_birth.setText(birth.toString())
        user_phone.setText(phone)
        user_gender.setText(gender)

        if (gender == "남성")
            radio_male.isChecked = true
        else
            radio_female.isChecked = true

        if (married) {
            user_married.setText("기혼")
            radio_married.isChecked = true
        } else {
            user_married.setText("미혼")
            radio_single.isChecked = true
        }

        user_job.setText(job)

        toViewMode()    // 읽기 뷰 모드로 뷰 설정
    }

    // 읽기 전용 상태 뷰 구성 함수
    private fun toViewMode() {

        btn_back.visibility = View.VISIBLE      // '상단 바' 상태 변경
        btn_cancel.visibility = View.GONE
        btn_edit.text = "편집"

        user_email.isEnabled = false            // '로그인 정보' 상태 초기화
        pw_field.visibility = View.GONE
        email_rule.visibility = View.GONE
        line.visibility = View.GONE

        user_email.setBackgroundColor("#FFFFFF".toColorInt())

        user_birth.isEnabled = false            // '사용자 정보' 상태 초기화
        user_phone.isEnabled = false
        job_etc.isEnabled = false
        birth_rule.visibility = View.GONE
        phone_rule.visibility = View.GONE
        user_gender.visibility = View.VISIBLE
        user_married.visibility = View.VISIBLE
        user_job.visibility = View.VISIBLE
        group_gender.visibility = View.GONE
        group_married.visibility = View.GONE
        job_spinner.visibility = View.GONE
        job_etc.visibility = View.GONE
        job_rule.visibility = View.GONE

        user_birth.setBackgroundColor("#FFFFFF".toColorInt())
        user_phone.setBackgroundColor("#FFFFFF".toColorInt())

        // 건강상태
    }

    // 편집 전용 상태 뷰 구성 함수
    private fun toEditMode() {

        btn_back.visibility = View.GONE         // '상단 바' 상태 변경
        btn_cancel.visibility = View.VISIBLE
        btn_edit.text = "완료"

        user_email.isEnabled = true             // '로그인 정보' 상태 초기화
        pw_field.visibility = View.VISIBLE
        pw_rule.visibility = View.GONE

        email_rule.visibility = View.GONE
        line.visibility = View.VISIBLE

        user_email.setBackgroundResource(R.drawable.design_gray_solid)

        user_birth.isEnabled = true            // '사용자 정보' 상태 초기화
        user_phone.isEnabled = true
        job_etc.isEnabled = true
        birth_rule.visibility = View.GONE
        phone_rule.visibility = View.GONE
        user_gender.visibility = View.GONE
        user_married.visibility = View.GONE
        user_job.visibility = View.GONE
        group_gender.visibility = View.VISIBLE
        group_married.visibility = View.VISIBLE
        job_spinner.visibility = View.VISIBLE
        job_rule.visibility = View.GONE

        if (job !in job_list) {
            job_spinner.setSelection(job_list.lastIndex)   // '기타'로 설정
            job_etc.setText(job)
            job_etc.visibility = View.VISIBLE
        }
        else job_etc.visibility = View.GONE

        user_birth.setBackgroundResource(R.drawable.design_gray_solid)
        user_phone.setBackgroundResource(R.drawable.design_gray_solid)

        // '건강 상태' 상태 초기화
    }

    // 프로필 뷰 변경 사항 반영 함수
    private fun changes_Profile() {

        if (isPasswordChanged)                                  // 비밀변호 변경 시: 비밀번호 데이터 변경
            pw = user_pw.text.toString()

        if (isEmailChanged) {                                   // 이메일 변경 시: 이메일 데이터 변경, 변경된 값 EditText에 적용
            email = user_email.text.toString()
            user_email.setText(email)
        }

        if (isBirthChanged) {                                   // 생년월일 변경 시: 생년월일 데이터 변경, 변경된 값 EditText에 적용
            birth = user_birth.text.toString().toInt()
            user_birth.setText(birth.toString())
        }

        if (isPhoneChanged) {                                   // 전화번호 변경 시: 전화번호 데이터 변경, 변경된 값 EditText에 적용
            phone = user_phone.text.toString()
            user_phone.setText(phone)
        }

        if (isGenderChanged) {                                   // 성별 변경 시: 성별 데이터 변경, 변경된 값 EditText에 적용
            if (radio_male.isChecked) {
                gender = "남성"
                user_gender.setText("남성")
            } else {
                gender = "여성"
                user_gender.setText("여성")
            }
        }

        if (isMarriedChanged) {                                  // 결혼 여부 변경 시: 결혼 여부 데이터 변경, 변경된 값 EditText에 적용
            if (radio_single.isChecked) {
                married = false
                user_married.setText("미혼")
            } else {
                married = true
                user_married.setText("기혼")
            }
        }

        if (isJobChanged || isJobEtcChanged) {                   // 직업 변경 시: 직업 데이터 변경, 변경된 값 EditText에 적용
            job = new_job
            user_job.setText(job)
        }

        isPasswordChanged = false;    isEmailChanged = false;    isBirthChanged = false;    isPhoneChanged = false
        isGenderChanged = false;      isMarriedChanged = false;  isJobChanged = false;      isJobEtcChanged = false     // 변경 상태 초기화
    }

    // 편집/완료 버튼 클릭 이벤트
    private fun click_EditButton() {
        btn_edit.setOnClickListener {
            Log.d("클릭", "married: " + married)
            if (edit_state) {       // 편집 버튼 클릭 이벤트 (편집->읽기)
                when {
                    !isPasswordValid -> showErrorDialog(this, "유효하지 않은 비밀번호 형식입니다.")    // 비밀번호 형식 오류
                    !isEmailValid -> showErrorDialog(this, "유효하지 않은 이메일 형식입니다.")         // 이메일 형식 오류
                    !isBirthValid -> showErrorDialog(this, "유효하지 않은 생년월일 형식입니다.")       // 생년월일 형식 오류
                    !isPhoneValid -> showErrorDialog(this, "유효하지 않은 전화번호 형식입니다.")       // 전화번호 형식 오류
                    !isJobValid -> showErrorDialog(this, "직업이 선택되지 않았습니다.")               // 직업 선택 오류
                    !isJobEtcValid -> showErrorDialog(this, "유효하지 않은 직업명 형식입니다.")        // 직업명 형식 오류
                    isPasswordChanged || isEmailChanged || isBirthChanged || isPhoneChanged                      // 데이터 변경 시
                            || isGenderChanged || isMarriedChanged || isJobChanged || isJobEtcChanged
                        -> showConfirmDialog(this, "확인", "변경 사항을 저장하시겠습니까?") { result ->
                        if (result) {               // "예" 버튼 클릭 시
                            edit_state = false      // 상태: 편집 완료
                            toViewMode()            // 읽기 뷰 모드로 뷰 변환
                            changes_Profile()       // 변경 데이터 적용
                            storeData()             // 변경 데이터 내부 DB에 저장
                        }
                    }
                    else -> {               // 변경 데이터 없음
                        edit_state = false
                        toViewMode()
                    }
                }
            } else {                // 완료 버튼 클릭 이벤트 (읽기->편집)
                edit_state = true       // 상태: 편집 중
                toEditMode()            // 편집 뷰 모드로 뷰 변환
            }
            isJobRule = false
        }
    }

    // 뒤로가기 버튼 클릭 이벤트
    private fun click_BackButton() {
        btn_back.setOnClickListener {
            finish()
            navigateTo(
                MainViewActivity::class.java,
                reverseAnimation = true
            )
        }
    }

    // 취소 버튼 클릭 이벤트
    private fun click_CancelButton() {

        // 편집 취소할건지 msgBox 물어보기
        btn_cancel.setOnClickListener {
            // 변경 사항 존재
            if (isPasswordChanged || isEmailChanged || isBirthChanged || isPhoneChanged || isGenderChanged || isMarriedChanged || isJobChanged || isJobEtcChanged) {
                showConfirmDialog(this, "확인", "변경을 취소하시겠습니까?") { result ->
                    if (result) {
                        edit_state = false
                        init_Profile()            // 읽기 뷰 모드로 뷰 변환
                    }
                }
            } else {    // 변경 사항 미존재
                edit_state = false
                init_Profile()
            }

        }
    }

    // 비밀번호 입력 이벤트
    private fun edit_password() {
        val pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,16}$")    // 영문, 숫자 (8-16자리)

        // 비밀번호 입력란 실시간 감지 이벤트
        user_pw.addTextChangedListener(
            createFlexibleTextWatcher (
                targetTextView = pw_rule,
                updateText = { input ->
                    when {
                        user_pw.text.toString() == pw -> "기존 입력과 동일합니다"                  // 기존과 동일
                        user_pw.text.toString().isBlank() -> "8~16자의 영문, 숫자를 사용하세요"     // 공란
                        pattern.matches(user_pw.text.toString()) -> "사용 가능합니다"             // 정규식 만족
                        else -> "8~16자의 영문, 숫자를 사용하세요"                                  // 정규식 불만족
                    }
                },
                updateTextColor = { input ->
                    when {
                        user_pw.text.toString().isBlank() || user_pw.text.toString() == pw -> "#1F70CC".toColorInt()    // 공란 or 기존과 동일 (파란색)
                        pattern.matches(user_pw.text.toString()) -> "#4B9F72".toColorInt()      // 정규식 만족 (초록색)
                        else -> "#FF0000".toColorInt()                                          // 정규식 불만족 (빨간색)
                    }
                },
                validateInput = { input -> pattern.matches(user_pw.text.toString()) },
                onValidStateChanged = { isValid ->
                    when {
                        user_pw.text.toString().isBlank() || user_pw.text.toString() == pw -> { // 공란 or 기존과 동일 (= Skip)
                            isPasswordValid = true                                              // skip 가능
                            isPasswordChanged = false                                           // 변경 사항 미존재
                        }
                        pattern.matches(user_pw.text.toString()) -> {                           // 정규식 만족 (= 본인인증 수행)
                            isPasswordValid = true                                              // skip 가능
                            isPasswordChanged = true                                            // 변경 사항 존재
                        }
                        else -> {                                                               // 정규식 만족 x
                            isPasswordValid = false                                             // skip 불가능
                            isPasswordChanged = true                                            // 변경 사항 존재
                        }
                    }
                }
            )
        )

        // 포커스 및 편집 여부에 따른 안내 텍스트 시각화 결정
        user_pw.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                pw_rule.visibility = View.VISIBLE
            else {
                if (isPasswordChanged)
                    pw_rule.visibility = View.VISIBLE
                else
                    pw_rule.visibility = View.GONE
            }
        }
    }

    // 이메일 입력 이벤트
    private fun edit_email() {
        val pattern = Regex("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}\$")               // '계정@도메인.최상위도메인'

        // 이메일 입력란 실시간 감지 이벤트
        user_email.addTextChangedListener(
            createFlexibleTextWatcher (
                targetTextView = email_rule,
                updateText = { input ->
                    when {
                        user_email.text.toString() == email -> "기존 입력과 동일합니다"             // 기존과 동일
                        user_email.text.toString().isBlank() -> "이메일을 형식에 맞게 입력해주세요"   // 공란
                        pattern.matches(user_email.text.toString()) -> "사용 가능합니다"           // 정규식 만족
                        else -> "사용 불가능한 형식입니다"                                          // 정규식 불만족
                    }
                },
                updateTextColor = { input ->
                    when {
                        user_email.text.toString().isBlank() || user_email.text.toString() == email -> "#1F70CC".toColorInt()   // 공란 or 기존과 동일 (파란색)
                        pattern.matches(user_email.text.toString()) -> "#4B9F72".toColorInt()   // 정규식 만족 (초록색)
                        else -> "#FF0000".toColorInt()                                          // 정규식 불만족 (빨간색)
                    }
                },
                validateInput = { input -> pattern.matches(user_email.text.toString()) },
                onValidStateChanged = { isValid ->
                    when {
                        user_email.text.toString().isBlank() || user_email.text.toString() == email -> {    // 공란 or 기존과 동일 (= Skip)
                            isEmailValid = true                                                 // skip 가능
                            isEmailChanged = false                                              // 변경 사항 미존재
                        }
                        pattern.matches(user_email.text.toString()) -> {                        // 정규식 만족
                            isEmailValid = true                                                 // skip 가능
                            isEmailChanged = true                                               // 변경 사항 존재
                        }
                        else -> {                                                               // 정규식 만족 x
                            isEmailValid = false                                                // skip 불가능
                            isEmailChanged = true                                               // 변경 사항 존재
                        }
                    }
                }
            )
        )

        // 포커스 및 편집 여부에 따른 안내 텍스트 시각화 결정
        user_email.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                email_rule.visibility = View.VISIBLE
            else {
                if (isEmailChanged)
                    email_rule.visibility = View.VISIBLE
                else
                    email_rule.visibility = View.GONE
            }
        }
    }

    // 생년월일 입력 이벤트
    private fun edit_birth() {
        val pattern = Regex("^(19[0-9]{2}|20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])$")    // 생년월일 정규식 (YYYYMMDD)

        // 생년월일 입력란 실시간 감지 이벤트
        user_birth.addTextChangedListener(
            createFlexibleTextWatcher (
                targetTextView = birth_rule,
                updateText = { input ->
                    when {
                        user_birth.text.toString() == birth.toString() -> "기존 입력과 동일합니다"  // 기존과 동일
                        user_birth.text.toString().isBlank() -> "생년월일을 형식에 맞게 입력해주세요" // 공란
                        pattern.matches(user_birth.text.toString()) -> "사용 가능합니다"           // 정규식 만족
                        else -> "사용 불가능합니다"                                                // 정규식 불만족
                    }
                },
                updateTextColor = { input ->
                    when {
                        user_birth.text.toString().isBlank() || user_birth.text.toString() == birth.toString() -> "#1F70CC".toColorInt()   // 공란 or 기존과 동일 (파란색)
                        pattern.matches(user_birth.text.toString()) -> "#4B9F72".toColorInt()   // 정규식 만족 (초록색)
                        else -> "#FF0000".toColorInt()                                          // 정규식 불만족 (빨간색)
                    }
                },
                validateInput = { input -> pattern.matches(user_birth.text.toString()) },
                onValidStateChanged = { isValid ->
                    when {
                        user_birth.text.toString().isBlank() || user_birth.text.toString() == birth.toString() -> {    // 공란 or 기존과 동일 (= Skip)
                            isBirthValid = true                                                 // skip 가능
                            isBirthChanged = false                                              // 변경 사항 미존재
                        }
                        pattern.matches(user_birth.text.toString()) -> {                        // 정규식 만족
                            isBirthValid = true                                                 // skip 가능
                            isBirthChanged = true                                               // 변경 사항 존재
                        }
                        else -> {                                                               // 정규식 만족 x
                            isBirthValid = false                                                // skip 불가능
                            isBirthChanged = true                                               // 변경 사항 존재
                        }
                    }
                }
            )
        )

        // 포커스 및 편집 여부에 따른 안내 텍스트 시각화 결정
        user_birth.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                birth_rule.visibility = View.VISIBLE
            else {
                if (isBirthChanged)
                    birth_rule.visibility = View.VISIBLE
                else
                    birth_rule.visibility = View.GONE
            }
        }
    }

    // 전화번호 입력 이벤트
    private fun edit_phone() {
        val pattern = Regex("^(010-[0-9]{4}-[0-9]{4})$")    // 전화번호 정규식 (010-xxxx-xxxx)

        user_phone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        // 전화번호 입력란 실시간 감지 이벤트
        user_phone.addTextChangedListener(
            createFlexibleTextWatcher (
                targetTextView = phone_rule,
                updateText = { input ->
                    when {
                        user_phone.text.toString() == phone -> "기존 입력과 동일합니다"             // 기존과 동일
                        user_phone.text.toString().isBlank() -> "전화번호를 형식에 맞게 입력해주세요" // 공란
                        pattern.matches(user_phone.text.toString()) -> "사용 가능합니다"           // 정규식 만족
                        else -> "사용 불가능합니다"                                                // 정규식 불만족
                    }
                },
                updateTextColor = { input ->
                    when {
                        user_phone.text.toString().isBlank() || user_phone.text.toString() == phone -> "#1F70CC".toColorInt()   // 공란 or 기존과 동일 (파란색)
                        pattern.matches(user_phone.text.toString()) -> "#4B9F72".toColorInt()   // 정규식 만족 (초록색)
                        else -> "#FF0000".toColorInt()                                          // 정규식 불만족 (빨간색)
                    }
                },
                validateInput = { input -> pattern.matches(user_phone.text.toString()) },
                onValidStateChanged = { isValid ->
                    when {
                        user_phone.text.toString().isBlank() || user_phone.text.toString() == phone -> {    // 공란 or 기존과 동일 (= Skip)
                            isPhoneValid = true                                                 // skip 가능
                            isPhoneChanged = false                                              // 변경 사항 미존재
                        }
                        pattern.matches(user_phone.text.toString()) -> {                        // 정규식 만족
                            isPhoneValid = true                                                 // skip 가능
                            isPhoneChanged = true                                               // 변경 사항 존재
                        }
                        else -> {                                                               // 정규식 만족 x
                            isPhoneValid = false                                                // skip 불가능
                            isPhoneChanged = true                                               // 변경 사항 존재
                        }
                    }
                }
            )
        )

        // 포커스 및 편집 여부에 따른 안내 텍스트 시각화 결정
        user_phone.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                phone_rule.visibility = View.VISIBLE
            else {
                if (isPhoneChanged)
                    phone_rule.visibility = View.VISIBLE
                else
                    phone_rule.visibility = View.GONE
            }
        }
    }

    // 성별 체크 이벤트
    private fun edit_gender() {
        if (gender.trim() == "남성") radio_male.isChecked = true else radio_female.isChecked = true

        group_gender.setOnCheckedChangeListener { _, checkedID ->
            val selectedGender = when (checkedID) {
                R.id.radioMale -> "남성"
                R.id.radioFemale -> "여성"
                else -> ""
            }
            if (selectedGender == gender)
                isGenderChanged = false
            else
                isGenderChanged = true
        }
    }

    // 결혼 여부 체크 이벤트
    private fun edit_married() {
        if (married) radio_married.isChecked = true else radio_single.isChecked = true

        group_married.setOnCheckedChangeListener { _, checkedID ->
            val selectedMarried = when (checkedID) {
                R.id.radioSingle -> false
                R.id.radioMarried -> true
                else -> false
            }
            if (selectedMarried == married)
                isMarriedChanged = false
            else
                isMarriedChanged = true
        }
    }

    // 직업 선택 이벤트
    private fun edit_job() {
        val pattern = Regex("^[\\s가-힣]{2,20}$")             // 직업명 정규식 (한글 2-20자)

        val initialIndex = if (job in job_list) job_list.indexOf(job) else job_list.lastIndex
        job_spinner.setSelection(initialIndex)      // 기존 직업 종류 적용

        // 기존 직업 종류 적용 ('기타' 일 경우)
        if (edit_state) {
            if (initialIndex == job_list.lastIndex) {
                job_etc.setText(job)
                job_etc.visibility = View.VISIBLE
            } else {
                job_etc.setText("")
                job_etc.visibility = View.GONE
            }
        }

        job_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                isJobChanged = (position != initialIndex)   // 선택한 직업의 인데스 변경 시

                when (position) {
                    0 -> {                                  // 첫 번째 항목: 선택 안 한 상태
                        job_etc.visibility = View.GONE
                        job_rule.visibility = View.GONE
                        isJobValid = false
                        new_job = ""
                    }
                    job_list.lastIndex -> {
                        val currentEtcJob = job_etc.text.toString().trim()
                        job_etc.visibility = View.VISIBLE
                        job_rule.visibility = View.GONE
                        new_job = currentEtcJob
                        isJobValid = true
                        isJobEtcChanged = currentEtcJob != job
                        isJobEtcValid = !isJobEtcChanged
                    }
                    else -> {
                        job_etc.setText("")
                        job_etc.visibility = View.GONE
                        job_rule.visibility = View.GONE
                        isJobValid = true
                        isJobEtcValid = true
                        isJobEtcChanged = false
                        new_job = job_list[position]
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {   // 선택하지 않은 경우
                isJobChanged = false
                isJobValid = false
                isJobEtcChanged = false
            }
        }

        if (!isJobRule) {
            job_etc.addTextChangedListener(
                createFlexibleTextWatcher(
                    targetTextView = job_rule,
                    updateText = { input ->
                        if (!isJobRule) {       // 최초 포커스 상태: 고정된 기본 안내 문구
                            "직업명을 정확히 입력해주세요"
                        } else {
                            when {
                                job_etc.text.isBlank() -> "2~20자의 한글을 입력하세요"            // 초기 기본 안내
                                job_etc.text.toString() == job -> "기존 입력과 동일합니다"        // 기존 동일
                                pattern.matches(job_etc.text.toString()) -> "사용 가능합니다"     // 유효 입력
                                else -> "형식에 맞지 않습니다"                                    // 유효하지 않음
                            }
                        }
                    },
                    updateTextColor = { input ->
                        when {
                            !isJobRule -> "#1F70CC".toColorInt()
                            job_etc.text.toString()
                                .isBlank() || job_etc.text.toString() == job -> "#1F70CC".toColorInt()   // 공란 or 기존과 동일 (파란색)
                            pattern.matches(job_etc.text.toString()) -> "#4B9F72".toColorInt()      // 정규식 만족 (초록색)
                            else -> "#FF0000".toColorInt()                                          // 정규식 불만족 (빨간색)
                        }
                    },
                    validateInput = { input -> pattern.matches(job_etc.text.toString()) },
                    onValidStateChanged = { isValid ->
                        val currentEtcJob = job_etc.text.toString().trim()

                        when {
                            currentEtcJob.isBlank() -> {   // 빈칸
                                isJobEtcChanged = true
                                isJobEtcValid = false
                            }
                            currentEtcJob == job -> {           // 기존과 동일
                                isJobEtcChanged = false    // 변경 사항 없음
                                isJobEtcValid = true       // 공란 또는 기존 값이면 검사 skip
                            }
                            pattern.matches(currentEtcJob) -> { // 정규식 만족
                                isJobEtcChanged = true
                                isJobEtcValid = true       // 변경 + 정규식 만족
                            }
                            else -> {                           // 정규식 불만족
                                isJobEtcChanged = true
                                isJobEtcValid = false      // 변경 + 정규식 불만족
                            }
                        }

                        new_job = currentEtcJob   // new_job은 항상 최신 값으로 유지
                    }
                )
            )
        }

        // 포커스 및 편집 여부에 따른 안내 텍스트 시각화 결정
        job_etc.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (!isJobRule)
                    isJobRule = true
                job_rule.visibility = View.VISIBLE
            }
            else {
                if (isJobEtcChanged)
                    job_rule.visibility = View.VISIBLE
                else
                    job_rule.visibility = View.GONE
            }
        }
    }

    // 키보드 숨기기 이벤트 (editText 이외의 영역을 눌렀을 경우, 스크롤 제외)
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        handleTouchOutsideEditText(this, ev)
        return super.dispatchTouchEvent(ev)
    }

}