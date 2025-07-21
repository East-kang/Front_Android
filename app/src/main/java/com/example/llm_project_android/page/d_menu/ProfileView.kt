package com.example.llm_project_android.page.d_menu

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.llm_project_android.R

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

    private var edit_state: Boolean = false  // 편집 여부 상태 변수

    private var id: String = ""
    private var pw: String = ""
    private var email: String = ""
    private var name: String = ""
    private var birth: Int = 0
    private var phone: String = ""
    private var gender: String = ""
    private var married: String = ""
    private var job: String = ""


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

        pw_field = findViewById(R.id.pw_Field)          // 비밀번호 레이아웃 (ConstraintLayout)
        line = findViewById(R.id.line1_2)               // 비밀번호 분리선 (View)

        user_pw = findViewById(R.id.user_pw)            // 비밀번호 편집 (EditText)
        user_email = findViewById(R.id.user_email)      // 이메일 편집 (EditText)
        user_birth = findViewById(R.id.user_birth)      // 생년월일 편집 (EditText)
        user_phone = findViewById(R.id.user_phone)      // 전화번호 편집 (EditText)
        job_etc = findViewById(R.id.job_etc)            // 기타 직업 입력란 (EditText)

        group_gender = findViewById(R.id.gender_Group)  // 성별 그룹 (RadioGroup)
        group_married = findViewById(R.id.married_Group)// 결혼 여부 그룹 (RadioGroup)

        radio_male = findViewById(R.id.radioMale)       // 남자 체크 박스 (RadioButton)
        radio_female = findViewById(R.id.radioFemale)   // 여자 체크 박스 (RadioButton)
        radio_single = findViewById(R.id.radioSingle)   // 미혼 체크 박스 (RadioGroup)
        radio_married = findViewById(R.id.radioMarried) // 기혼 체크 박스 (RadioGroup)

        job_spinner = findViewById(R.id.job_spinner)    // 직업 선택 박스 (Spinner)

        // 초기 프로필 상태 불러오기
        init_Profile()

        // 편집 버튼 클릭 이벤트
        click_EditButton()
        
    }

    // (feature/common 브랜치에서 다시 함수 파일 만들 예정)
    // 내부 DB 데이터 불러오기 (샘플 데이터로 대체)
    fun loadData() {
        id = "gildong2"
        pw = "qwer1234"
        email = "gildong22@naver.com"
        name = "홍길동"
        birth = 19980404
        phone = "010-1234-5678"
        gender = "남자"
        married = "기혼"
        job = "회사원"     // 샘플 데이터
    }

    // (feature/common 브랜치에서 다시 함수 파일 만들 예정)
    // 내부 DB 데이터 저장하기
    fun storeData() {

    }

    // 초기 프로필 상태
    private fun init_Profile() {

        loadData()                          // 프로필 값 불러오기

        btn_back.visibility = View.VISIBLE  // '상단 바' 상태 초기화
        btn_cancel.visibility = View.GONE
        btn_edit.text = "편집"

        user_email.isEnabled = false        // '로그인 정보' 상태 초기화
        pw_field.visibility = View.GONE
        email_rule.visibility = View.GONE

        user_birth.isEnabled = false        // '사용자 정보' 상태 초기화
        user_phone.isEnabled = false
        birth_rule.visibility = View.GONE
        phone_rule.visibility = View.GONE
        user_gender.visibility = View.VISIBLE
        user_married.visibility = View.VISIBLE
        user_job.visibility = View.VISIBLE
        group_gender.visibility = View.GONE
        group_married.visibility = View.GONE
        job_spinner.visibility = View.GONE
        job_etc.visibility = View.GONE
    }

    // 편집 버튼 클릭 이벤트
    private fun click_EditButton() {
        btn_edit.setOnClickListener {
            if (!edit_state) {                      // 편집 상태가 아닐 경우
                edit_state = true                       // 편집 중

                btn_back.visibility = View.GONE         // '상단 바' 상태 변경
                btn_cancel.visibility = View.VISIBLE
                btn_edit.text = "완료"

                user_email.isEnabled = true             // '로그인 정보' 상태 초기화
                pw_field.visibility = View.VISIBLE
                email_rule.visibility = View.VISIBLE

                user_email.setBackgroundColor("#d6d6d6".toColorInt())

                user_birth.isEnabled = true            // '사용자 정보' 상태 초기화
                user_phone.isEnabled = true
                birth_rule.visibility = View.VISIBLE
                phone_rule.visibility = View.VISIBLE
                user_gender.visibility = View.GONE
                user_married.visibility = View.GONE
                user_job.visibility = View.GONE
                group_gender.visibility = View.VISIBLE
                group_married.visibility = View.VISIBLE
                job_spinner.visibility = View.VISIBLE

                if (job == "기타") job_etc.visibility = View.VISIBLE
                else job_etc.visibility = View.GONE

                user_birth.setBackgroundColor("#d6d6d6".toColorInt())
                user_phone.setBackgroundColor("#d6d6d6".toColorInt())

                // '건강 상태' 상태 초기화

            } else {                                // 편집 상태일 경우

                // 입력 조건 모두 만족할 경우 실행하기
                edit_state = false                      // 편집 완료

                storeData()                             // 데이터 내부 DB에 저장
                                                        // 데이터 반영 함수도 구현해야함.
                btn_back.visibility = View.VISIBLE      // '상단 바' 상태 변경
                btn_cancel.visibility = View.GONE
                btn_edit.text = "편집"

                user_email.isEnabled = false            // '로그인 정보' 상태 초기화
                pw_field.visibility = View.GONE
                email_rule.visibility = View.GONE

                user_email.setBackgroundColor("#FFFFFF".toColorInt())

                user_birth.isEnabled = false            // '사용자 정보' 상태 초기화
                user_phone.isEnabled = false
                birth_rule.visibility = View.GONE
                phone_rule.visibility = View.GONE
                user_gender.visibility = View.VISIBLE
                user_married.visibility = View.VISIBLE
                user_job.visibility = View.VISIBLE
                group_gender.visibility = View.GONE
                group_married.visibility = View.GONE
                job_spinner.visibility = View.GONE
                job_etc.visibility = View.GONE

                user_birth.setBackgroundColor("#FFFFFF".toColorInt())
                user_phone.setBackgroundColor("#FFFFFF".toColorInt())
            }
        }
    }

    // 뒤로가기 버튼 클릭 이벤트
    private fun click_BackButton() {

    }

    // 취소 버튼 클릭 이벤트
    private fun click_CancelButton() {

    }

    // 비밀번호 입력 이벤트
    private fun input_pw() {

    }

    // 이메일 입력 이벤트
    private fun input_email() {

    }

    // 생년월일 입력 이벤트
    private fun input_birth() {

    }

    // 전화번호 입력 이벤트
    private fun input_phone() {

    }

    // 성별 체크 이벤트
    private fun check_gender() {

    }

    // 결혼 여부 체크 이벤트
    private fun check_married() {

    }

    // 직업 선택 이벤트
    private fun choice_job() {

    }
}