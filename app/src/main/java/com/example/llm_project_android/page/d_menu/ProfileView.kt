package com.example.llm_project_android.page.d_menu

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.llm_project_android.R
import com.example.llm_project_android.db.user.MyDatabase
import com.example.llm_project_android.functions.createFlexibleTextWatcher
import com.example.llm_project_android.functions.handleTouchOutsideEditText
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.saveUserInfo
import com.example.llm_project_android.functions.showConfirmDialog
import com.example.llm_project_android.functions.showErrorDialog
import com.example.llm_project_android.page.c_product.MainViewActivity
import com.google.android.flexbox.FlexboxLayout
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class ProfileView : AppCompatActivity() {

    private lateinit var btn_back: ImageButton      // 뒤로 가기 버튼
    private lateinit var btn_edit: Button           // 편집,완료 버튼
    private lateinit var btn_cancel: Button         // 취소 버튼

    private lateinit var user_id: TextView          // 유저 아이디 필드
    private lateinit var user_name: TextView        // 유저 이름 필드
    private lateinit var user_gender: TextView      // 유저 성별 필드
    private lateinit var user_married: TextView     // 유저 결혼 여부 필드
    private lateinit var user_job: TextView         // 유저 직업 필드
    private lateinit var pw_rule: TextView          // 비밀 번호 입력 안내 문구 필드
    private lateinit var email_rule: TextView       // 이메일 입력 안내 문구 필드
    private lateinit var birth_rule: TextView       // 생년 월일 입력 안내 문구 필드
    private lateinit var phone_rule: TextView       // 전화 번호 입력 안내 문구 필드
    private lateinit var job_rule: TextView         // 직업명 입력 안내 문구 필드

    private lateinit var pw_field: ConstraintLayout // 비밀 번호 레이아웃
    private lateinit var line: View                 // 비밀 번호 분리선

    private lateinit var user_pw: EditText          // 사용자 비밀 번호 필드
    private lateinit var user_email: EditText       // 사용자 이메일 필드
    private lateinit var user_birth: EditText       // 사용자 생년 월일 필드
    private lateinit var user_phone: EditText       // 사용자 전화 번호 필드
    private lateinit var job_etc: EditText          // 사용자 기타 직업명 필드

    private lateinit var group_gender: RadioGroup   // 성별 라디오 그룹
    private lateinit var group_married: RadioGroup  // 결혼 여부 라디오 그룹

    private lateinit var radio_male: RadioButton    // 성별(남자) 버튼
    private lateinit var radio_female: RadioButton  // 성별(여자) 버튼
    private lateinit var radio_single: RadioButton  // 결혼 여부(미혼) 버튼
    private lateinit var radio_married: RadioButton // 결혼 여부(기혼) 버튼

    private lateinit var job_spinner: Spinner       // 직업 토글

    private lateinit var disease_Field: FlexboxLayout       // 질병 상태 필드
    private lateinit var disease_check_Field: LinearLayout  // 질병 체크 필드
    private lateinit var item0: CheckBox                    // 질병 없음 체크 박스
    private lateinit var item1: CheckBox                    // 암 체크 박스
    private lateinit var item2: CheckBox                    // 심혈관계 질환 체크 박스
    private lateinit var item3: CheckBox                    // 뇌혈관 질환 체크 박스
    private lateinit var item4: CheckBox                    // 당뇨병 체크 박스
    private lateinit var item5: CheckBox                    // 간질환 체크 박스
    private lateinit var item6: CheckBox                    // 폐질환 체크 박스
    private lateinit var item7: CheckBox                    // 근골격계/척추질환 체크 박스
    private lateinit var item8: CheckBox                    // 정신질환 체크 박스
    private lateinit var item9: CheckBox                    // 기타 만성질환 체크 박스
    private lateinit var checkBoxList: List<CheckBox>       // 체크 박스 모음 리스트

    private var edit_state: Boolean = false                 // 편집 여부 상태 변수 (true: 편집 중, false: 편집x)

    private var isPasswordValid: Boolean = true             // 비밀 번호 정규식 조건 만족 유효성 (true: 정규식 만족, false: 정규식 불만족)
    private var isEmailValid: Boolean = true                // 이메일 정규식 조건 만족 유효성
    private var isBirthValid: Boolean = true                // 생년 월일 정규식 조건 만족 유효성
    private var isPhoneValid: Boolean = true                // 이메일 정규식 조건 만족 유효성
    private var isJobValid: Boolean = true                  // 기타 직업명 정규식 조건 만족 유효성
    private var isJobEtcValid: Boolean = true               // 기타 직업명 정규식 조건 만족 유효성
    private var isDiseaseValid: Boolean = true              // 질병 체크 박스 체크 유효성

    private var isPasswordChanged: Boolean = false          // 비밀 번호 변경 여부
    private var isEmailChanged: Boolean = false             // 이메일 변경 여부
    private var isBirthChanged: Boolean = false             // 생년 월일 변경 여부
    private var isPhoneChanged: Boolean = false             // 전화 번호 변경 여부
    private var isGenderChanged: Boolean = false            // 성별 변경 여부
    private var isMarriedChanged: Boolean = false           // 결혼 여부 변경 여부
    private var isJobChanged: Boolean = false               // 직업 변경 여부
    private var isJobEtcChanged: Boolean = false            // 기타 직업명 변경 여부
    private var isDiseaseChanged: Boolean = false           // 질병 체크 변경 여부
    private var isJobRule: Boolean = false                  // 초기 안내 문구 표현을 위한 상태 변수(true: 실시간 검증, false: 기본 안내 문구)

    private lateinit var job_list: Array<String>            // 직업 리스트
    private lateinit var adapter: ArrayAdapter<String>      // 직업 어뎁터

    private var update_List: MutableList<String> = mutableListOf()  // 변경 리스트

    private lateinit var executor: Executor                 // 실행
    private lateinit var biometricPrompt: BiometricPrompt   // 생체 신호 프롬프트
    private lateinit var promptInfo: BiometricPrompt.PromptInfo // 프롬프트 정보

    private var id: String = ""                         // 아이디 저장 변수
    private var pw: String = ""                         // 비밀 번호 저장 변수
    private var email: String = ""                      // 이메일 저장 변수
    private var name: String = ""                       // 이름 저장 변수
    private var birth: Int = 0                          // 생년 월일 저장 변수
    private var phone: String = ""                      // 전화 번호 저장 변수
    private var gender: String = ""                     // 성별 저장 변수
    private var married: Boolean = false                // 결혼 여부 저장 변수
    private var job: String = ""                        // 직업 저장 변수
    private var diseases: List<String> = emptyList()    // 질병 리스트  저장 변수
    private var new_job: String = ""                    // 변경할 직업 값

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.d_page_profile_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_back = findViewById(R.id.backButton)
        btn_edit = findViewById(R.id.editButton)
        btn_cancel = findViewById(R.id.cancelButton)

        user_id = findViewById(R.id.user_id)
        user_name = findViewById(R.id.user_name)
        user_gender = findViewById(R.id.user_gender)
        user_married = findViewById(R.id.user_married)
        user_job = findViewById(R.id.user_job)
        pw_rule = findViewById(R.id.user_pw_rule)
        email_rule = findViewById(R.id.user_email_rule)
        birth_rule = findViewById(R.id.user_birth_rule)
        phone_rule = findViewById(R.id.user_phone_rule)
        job_rule = findViewById(R.id.user_job_rule)

        pw_field = findViewById(R.id.pw_Field)
        line = findViewById(R.id.line1_2)

        user_pw = findViewById(R.id.user_pw)
        user_email = findViewById(R.id.user_email)
        user_birth = findViewById(R.id.user_birth)
        user_phone = findViewById(R.id.user_phone)
        job_etc = findViewById(R.id.job_etc)

        group_gender = findViewById(R.id.gender_Group)
        group_married = findViewById(R.id.married_Group)

        radio_male = findViewById(R.id.radioMale)
        radio_female = findViewById(R.id.radioFemale)
        radio_single = findViewById(R.id.radioSingle)
        radio_married = findViewById(R.id.radioMarried)

        job_spinner = findViewById(R.id.job_spinner)

        disease_Field = findViewById(R.id.disease_Field)
        disease_check_Field = findViewById(R.id.disease_check_Field)

        item0 = findViewById(R.id.disease0)
        item1 = findViewById(R.id.disease1)
        item2 = findViewById(R.id.disease2)
        item3 = findViewById(R.id.disease3)
        item4 = findViewById(R.id.disease4)
        item5 = findViewById(R.id.disease5)
        item6 = findViewById(R.id.disease6)
        item7 = findViewById(R.id.disease7)
        item8 = findViewById(R.id.disease8)
        item9 = findViewById(R.id.disease9)

        checkBoxList = listOf(
            item0, item1, item2, item3, item4,
            item5, item6, item7, item8, item9
        )

        job_list = resources.getStringArray(R.array.jobs)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, job_list)
        job_spinner.adapter = adapter

        loadData()              // 프로필 값 불러 오기
        click_Buttons()         // 버튼 클릭 이벤트

        edit_Password()         // 비밀 번호 변경 이벤트
        edit_Email()            // 이메일 변경 이벤트
        edit_Birth()            // 생년 월일 변경 이벤트
        edit_Phone()            // 전화 번호 변경 이벤트
        edit_Gender()           // 성별 체크 이벤트
        edit_Married()          // 결혼 여부 체크 이벤트
        edit_Job()              // 직업 선택 이벤트
        items_Check()           // item 체크 이벤트

    }

    /* 내부 DB 데이터 불러 오기 */
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
                diseases = it.diseases

                // 데이터 로드 후 UI 반영
                init_Profile()          // 초기 프로필 상태 불러오기
            }
        }
    }

    /* 내부 DB로 데이터 저장 하기 */
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
                job = job,
                diseases = diseases
            )
        }
    }

    /* 초기 프로필 데이터 적용 함수 */
    private fun init_Profile() {
        user_id.setText(id)
        user_pw.setText("")
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

        view_Mode(edit_state)   // 읽기 뷰 모드로 뷰 설정
    }

    /* 읽기 전용 상태 뷰 구성 함수 */
    private fun view_Mode(isEdit: Boolean) {
        /* 편집 모드 뷰 */
        if (isEdit) {
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
            disease_Field.visibility = View.GONE
            disease_check_Field.visibility = View.VISIBLE

            if (job !in job_list) {
                job_spinner.setSelection(job_list.lastIndex)   // '기타'로 설정
                job_etc.setText(job)
                job_etc.visibility = View.VISIBLE
            } else job_etc.visibility = View.GONE

            check_Box() // 질병 박스 체크 이벤트

            user_birth.setBackgroundResource(R.drawable.design_gray_solid)
            user_phone.setBackgroundResource(R.drawable.design_gray_solid)
        }

        /* 읽기 모드 뷰 */
        else {
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
            disease_Field.visibility = View.VISIBLE
            disease_check_Field.visibility = View.GONE

            add_Tag(diseases)

            user_birth.setBackgroundColor("#FFFFFF".toColorInt())
            user_phone.setBackgroundColor("#FFFFFF".toColorInt())
        }
    }
    
    /* 프로필 뷰 변경 사항 반영 함수 */
    private fun changes_Profile() {
        if (isPasswordChanged) {                                // 비밀변호 변경 시: 비밀번호 데이터 변경
            pw = user_pw.text.toString()
            user_pw.setText("")
        }

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

        if (isDiseaseChanged) {                                 // 질병 종류 변경 시: 질병 데이터 변경
            diseases = mutableListOf()
            diseases = update_List
            update_List = mutableListOf()
        }

        // 유효성 초기화
        isPasswordValid = true; isEmailValid = true; isBirthValid = true
        isPhoneValid = true; isJobValid = true; isJobEtcValid = true; isDiseaseValid = true

        // 변경 상태 초기화
        isPasswordChanged = false;  isEmailChanged = false;     isBirthChanged = false; isPhoneChanged = false
        isGenderChanged = false;    isMarriedChanged = false;   isJobChanged = false;   isJobEtcChanged = false
        isDiseaseChanged = false
    }

    /* 버튼 클릭 이벤트 */
    private fun click_Buttons() {
        /* 편집,완료 버튼 클릭 이벤트 */
        btn_edit.setOnClickListener {
            edit_state = !edit_state
            
            if (edit_state) {       // 완료 버튼 클릭 이벤트 (읽기->편집)
                edit_state = true       // 상태: 편집 중
                view_Mode(edit_state)   // 편집 뷰 모드로 전환
            } else {                // 편집 버튼 클릭 이벤트 (편집->읽기)
                confirmed_CheckBox()    // 질병 체크 박스 확인

                when {
                    !isPasswordValid -> showErrorDialog(this, "유효하지 않은 비밀번호 형식입니다.")    // 비밀번호 형식 오류
                    !isEmailValid -> showErrorDialog(this, "유효하지 않은 이메일 형식입니다.")         // 이메일 형식 오류
                    !isBirthValid -> showErrorDialog(this, "유효하지 않은 생년월일 형식입니다.")       // 생년월일 형식 오류
                    !isPhoneValid -> showErrorDialog(this, "유효하지 않은 전화번호 형식입니다.")       // 전화번호 형식 오류
                    !isJobValid -> showErrorDialog(this, "직업이 선택되지 않았습니다.")               // 직업 선택 오류
                    !isJobEtcValid -> showErrorDialog(this, "유효하지 않은 직업명 형식입니다.")        // 직업명 형식 오류
                    !isDiseaseValid -> showErrorDialog(this, "선택된 질병이 없습니다.")               // 질병 체크 오류
                    isPasswordChanged -> {                                                                       // 비밀 번호 변경 시
                        fingerPrint(
                            onSuccess = {
                                edit_state = false      // 상태: 편집 완료
                                view_Mode(edit_state)   // 읽기 뷰 모드로 뷰 변환
                                changes_Profile()       // 변경 데이터 적용
                                storeData()             // 변경 데이터 내부 DB에 저장
                            },
                            onFailure = {},
                            onCancel = {}
                        )
                    }
                    isEmailChanged || isBirthChanged || isPhoneChanged || isGenderChanged                        // 데이터 변경 시
                            || isMarriedChanged || isJobChanged || isJobEtcChanged || isDiseaseChanged
                        -> showConfirmDialog(this, "확인", "변경 사항을 저장하시겠습니까?") { result ->
                        if (result) {               // "예" 버튼 클릭 시
                            edit_state = false      // 상태: 편집 완료
                            changes_Profile()       // 변경 데이터 적용
                            storeData()             // 변경 데이터 내부 DB에 저장
                            view_Mode(edit_state)   // 읽기 뷰 모드로 뷰 변환
                        }
                    }
                    else -> {               // 변경 데이터 없음
                        edit_state = false
                        view_Mode(edit_state)
                    }
                }
            }
            isJobRule = false
        }

        /* 취소 버튼 클릭 */
        btn_cancel.setOnClickListener { click_Cancel() }
        
        /* 뒤로 가기 버튼 클릭 */
        btn_back.setOnClickListener { click_Back() }

        /* 기기 내장 뒤로 가기 버튼 클릭 */
        onBackPressedDispatcher.addCallback(this) { click_Back() }
    }
    
    /* 뒤로 가기 클릭 이벤트 */
    private fun click_Back() {
        if (edit_state) {
            // 변경 사항 존재
            click_Cancel()
        } else {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    /* 취소 버튼 클릭 이벤트 */
    private fun click_Cancel() {
        // 변경 사항 존재
        if (isPasswordChanged || isEmailChanged || isBirthChanged || isPhoneChanged || isGenderChanged || isMarriedChanged || isJobChanged || isJobEtcChanged) {
            showConfirmDialog(this, "취소", "변경을 취소하시겠습니까?") { result ->
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

    /* 비밀 번호 입력 이벤트 */
    private fun edit_Password() {
        val pattern = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,16}$")    // 영문, 숫자 (8-16자리)

        // 비밀번호 입력란 실시간 감지 이벤트
        user_pw.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = pw_rule,
                updateText = {
                    when {
                        user_pw.text.toString() == pw -> "기존 입력과 동일합니다"                  // 기존과 동일
                        user_pw.text.toString().isBlank() -> "8~16자의 영문, 숫자를 사용하세요"     // 공란
                        pattern.matches(user_pw.text.toString()) -> "사용 가능합니다"             // 정규식 만족
                        else -> "8~16자의 영문, 숫자를 사용하세요"                                  // 정규식 불만족
                    }
                },
                updateTextColor = {
                    when {
                        user_pw.text.toString()
                            .isBlank() || user_pw.text.toString() == pw -> "#1F70CC".toColorInt()    // 공란 or 기존과 동일 (파란색)
                        pattern.matches(user_pw.text.toString()) -> "#4B9F72".toColorInt()      // 정규식 만족 (초록색)
                        else -> "#FF0000".toColorInt()                                          // 정규식 불만족 (빨간색)
                    }
                },
                validateInput = { pattern.matches(user_pw.text.toString()) },
                onValidStateChanged = {
                    when {
                        user_pw.text.toString()
                            .isBlank() || user_pw.text.toString() == pw -> { // 공란 or 기존과 동일 (= Skip)
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

    /* 이메일 입력 이벤트 */
    private fun edit_Email() {
        val pattern =
            Regex("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}\$")               // '계정@도메인.최상위도메인'

        // 이메일 입력란 실시간 감지 이벤트
        user_email.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = email_rule,
                updateText = {
                    when {
                        user_email.text.toString() == email -> "기존 입력과 동일합니다"             // 기존과 동일
                        user_email.text.toString().isBlank() -> "이메일을 형식에 맞게 입력해주세요"   // 공란
                        pattern.matches(user_email.text.toString()) -> "사용 가능합니다"           // 정규식 만족
                        else -> "사용 불가능한 형식입니다"                                          // 정규식 불만족
                    }
                },
                updateTextColor = {
                    when {
                        user_email.text.toString()
                            .isBlank() || user_email.text.toString() == email -> "#1F70CC".toColorInt()   // 공란 or 기존과 동일 (파란색)
                        pattern.matches(user_email.text.toString()) -> "#4B9F72".toColorInt()   // 정규식 만족 (초록색)
                        else -> "#FF0000".toColorInt()                                          // 정규식 불만족 (빨간색)
                    }
                },
                validateInput = { pattern.matches(user_email.text.toString()) },
                onValidStateChanged = {
                    when {
                        user_email.text.toString()
                            .isBlank() || user_email.text.toString() == email -> {    // 공란 or 기존과 동일 (= Skip)
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

    /* 생년 월일 입력 이벤트 */
    private fun edit_Birth() {
        val pattern =
            Regex("^(19[0-9]{2}|20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])$")    // 생년월일 정규식 (YYYYMMDD)

        // 생년월일 입력란 실시간 감지 이벤트
        user_birth.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = birth_rule,
                updateText = {
                    when {
                        user_birth.text.toString() == birth.toString() -> "기존 입력과 동일합니다"  // 기존과 동일
                        user_birth.text.toString().isBlank() -> "생년월일을 형식에 맞게 입력해주세요" // 공란
                        pattern.matches(user_birth.text.toString()) -> "사용 가능합니다"           // 정규식 만족
                        else -> "사용 불가능합니다"                                                // 정규식 불만족
                    }
                },
                updateTextColor = {
                    when {
                        user_birth.text.toString()
                            .isBlank() || user_birth.text.toString() == birth.toString() -> "#1F70CC".toColorInt()   // 공란 or 기존과 동일 (파란색)
                        pattern.matches(user_birth.text.toString()) -> "#4B9F72".toColorInt()   // 정규식 만족 (초록색)
                        else -> "#FF0000".toColorInt()                                          // 정규식 불만족 (빨간색)
                    }
                },
                validateInput = { pattern.matches(user_birth.text.toString()) },
                onValidStateChanged = {
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

    /* 전화 번호 입력 이벤트 */
    private fun edit_Phone() {
        val pattern = Regex("^(010-[0-9]{4}-[0-9]{4})$")    // 전화번호 정규식 (010-xxxx-xxxx)

        user_phone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        // 전화번호 입력란 실시간 감지 이벤트
        user_phone.addTextChangedListener(
            createFlexibleTextWatcher(
                targetTextView = phone_rule,
                updateText = {
                    when {
                        user_phone.text.toString() == phone -> "기존 입력과 동일합니다"             // 기존과 동일
                        user_phone.text.toString().isBlank() -> "전화번호를 형식에 맞게 입력해주세요" // 공란
                        pattern.matches(user_phone.text.toString()) -> "사용 가능합니다"           // 정규식 만족
                        else -> "사용 불가능합니다"                                                // 정규식 불만족
                    }
                },
                updateTextColor = {
                    when {
                        user_phone.text.toString()
                            .isBlank() || user_phone.text.toString() == phone -> "#1F70CC".toColorInt()   // 공란 or 기존과 동일 (파란색)
                        pattern.matches(user_phone.text.toString()) -> "#4B9F72".toColorInt()   // 정규식 만족 (초록색)
                        else -> "#FF0000".toColorInt()                                          // 정규식 불만족 (빨간색)
                    }
                },
                validateInput = { pattern.matches(user_phone.text.toString()) },
                onValidStateChanged = {
                    when {
                        user_phone.text.toString()
                            .isBlank() || user_phone.text.toString() == phone -> {    // 공란 or 기존과 동일 (= Skip)
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

    /* 성별 체크 이벤트 */
    private fun edit_Gender() {
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

    /* 결혼 여부 체크 이벤트 */
    private fun edit_Married() {
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

    /* 직업 선택 이벤트 */
    private fun edit_Job() {
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

        // 아이템 선택 이벤트
        job_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                id: Long
            ) {
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

        // 기타 직업명 입력란 실시간 반영
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
            } else {
                if (isJobEtcChanged)
                    job_rule.visibility = View.VISIBLE
                else
                    job_rule.visibility = View.GONE
            }
        }
    }

    /* 질병 아이템 태그 추가 */
    private fun add_Tag(diseaseList: List<String>) {
        val inflater = layoutInflater
        disease_Field.removeAllViews()  // 질병 상태 필드 초기화

        for (disease in diseaseList) {
            val tagView = inflater.inflate(R.layout.z_design_disease_text_view, disease_Field, false)
            val textView = tagView.findViewById<TextView>(R.id.disease_tag)
            textView.text = disease
            disease_Field.addView(tagView)
        }
    }

    /* 질병 체크 박스 체크 이벤트 */
    private fun check_Box() {
        // 초기 체크 상태 표현
        for (box in checkBoxList)
            box.isChecked = if (box.text.toString() in diseases) true else false
    }

    /* item 체크 */
    private fun items_Check() {
        // '질병 없음' 항목 클릭 이벤트
        item0.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {                                // 클릭o -> 다른 항목 전부 체크 해제
                checkBoxList.drop(1).forEach { it.isChecked = false }
                item0.isChecked = true
            }
        }

        checkBoxList.drop(1).forEach { box ->
            box.setOnCheckedChangeListener { _, _ ->
                // 다른 질병 체크 시, item0이 체크되어 있으면 해제
                if (item0.isChecked) {
                    item0.isChecked = false
                }
            }
        }
    }

    /* 체크 박스 체크 확인 */
    private fun confirmed_CheckBox() {
        // 질병 리스트 업데이트
        for (box in checkBoxList)
            if (box.isChecked) update_List.add(box.text.toString())

        if (update_List.isEmpty()) {                            // 체크한 박스가 없을 경우
            isDiseaseValid = false
            isDiseaseChanged = true
        } else if (update_List.toSet() == diseases.toSet()) {
            isDiseaseValid = true
            isDiseaseChanged = false
        } else {
            isDiseaseValid = true
            isDiseaseChanged = true
        }
    }

    /* 키보드 숨기기 이벤트 (editText 이외의 영역을 눌렀을 경우, 스크롤 제외) */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        handleTouchOutsideEditText(this, ev)
        return super.dispatchTouchEvent(ev)
    }

    /* 지문 인식 기능 */
    private fun fingerPrint(onSuccess: () -> Unit, onFailure: () -> Unit, onCancel: () -> Unit) {
        executor = ContextCompat.getMainExecutor(this@ProfileView)
        biometricPrompt = BiometricPrompt(
            this@ProfileView, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    val toast = Toast.makeText(applicationContext, "프로필 수정 완료", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.BOTTOM, 0, 100)
                    toast.show()
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailure()
                }

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                        errorCode == BiometricPrompt.ERROR_CANCELED
                    ) {
                        onCancel()  // 인증 취소
                    } else
                        onFailure() // 그 외 오류 -> 실패 처리
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("지문 인증")
            .setSubtitle("기기에 등록된 지문을 이용하여 지문을 인증해주세요")
            .setNegativeButtonText("취소")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}