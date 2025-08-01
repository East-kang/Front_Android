package com.example.llm_project_android.page.b_signup

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.clearUserDiseases
import com.example.llm_project_android.functions.clearUserFields
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.getUserInfo
import com.example.llm_project_android.functions.handleTouchOutsideEditText
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.saveUserInfo
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class SignUpActivity3 : AppCompatActivity() {

    private lateinit var item0: CheckBox
    private lateinit var item1: CheckBox
    private lateinit var item2: CheckBox
    private lateinit var item3: CheckBox
    private lateinit var item4: CheckBox
    private lateinit var item5: CheckBox
    private lateinit var item6: CheckBox
    private lateinit var item7: CheckBox
    private lateinit var item8: CheckBox
    private lateinit var item9: CheckBox
    private lateinit var btn_back: ImageButton
    private lateinit var btn_next: Button
    private lateinit var checkBoxList: List<CheckBox>

    var source: String = ""
    private var is_Checked_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }        // 체크 완료 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.b_page_sign_up_view3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        item0 = findViewById<CheckBox>(R.id.disease0)       // 질병 없음 체크 박스
        item1 = findViewById<CheckBox>(R.id.disease1)       // 암 체크 박스
        item2 = findViewById<CheckBox>(R.id.disease2)       // 심혈관계 질환 체크 박스
        item3 = findViewById<CheckBox>(R.id.disease3)       // 뇌혈관 질환 체크 박스
        item4 = findViewById<CheckBox>(R.id.disease4)       // 당뇨병 체크 박스
        item5 = findViewById<CheckBox>(R.id.disease5)       // 간질환 체크 박스
        item6 = findViewById<CheckBox>(R.id.disease6)       // 폐질환 체크 박스
        item7 = findViewById<CheckBox>(R.id.disease7)       // 근골격계/척추질환 체크 박스
        item8 = findViewById<CheckBox>(R.id.disease8)       // 정신질환 체크 박스
        item9 = findViewById<CheckBox>(R.id.disease9)       // 기타 만성질환 체크 박스

        btn_back = findViewById<ImageButton>(R.id.backButton)    // 뒤로가기 버튼
        btn_next = findViewById<Button>(R.id.next_Button)   // 다음 버튼

        checkBoxList = listOf(
            item0, item1, item2, item3, item4,
            item5, item6, item7, item8, item9
        )

        // 이전 화면에서 데이터 받아오기
        val extras = getPassedExtras("source", String::class.java)
        source = extras["source"] as? String ?: ""

        // 초기 설정 (버튼 비활성화)
        updateNextButton()

        // item 체크
        items_check({ is_Checked_Confirmed }, { is_Checked_Confirmed = it })

        // 뒤로가기 버튼 클릭 이벤트 (to SignUpActivity2)
        clickBackButton()
        click_backpressdKey()

        // 다음 버튼 클릭 이벤트 (to SignUpActivity4)
        clickNextButton(SignUpActivity4::class.java)

        // 화면 전환 간 데이터 유지 (SignUpActivity4.kt -> SignUpActivity3.kt)
        restorePassedData()
    }

    // 화면 전환간 데이터 수신 및 적용
    fun restorePassedData() {
        lifecycleScope.launch {
            val user = getUserInfo(applicationContext)

            // 데이터 필드가 모두 유효할 경우에만 복원
            if (user != null && user.diseases.isNotEmpty()) {
                if (user.diseases[0] == item0.text.toString())  // "질병 없음"
                    for (i in checkBoxList.indices) {
                        checkBoxList[i].isChecked = (i == 0)    // item0만 체크
                        checkBoxList[i].isEnabled = (i == 0)    // item0만 활성화
                    }
                else
                    for (i in checkBoxList.indices)
                        checkBoxList[i].isChecked = (user.diseases[i].isNotEmpty())     // item1 ~ item9 중 true인 항목만 체크

                is_Checked_Confirmed = true
            }
        }
    }

    // item 체크
    fun items_check(getCheckConfirmed: () -> Boolean, setCheckedConfirmed: (Boolean) -> Unit) {

        // '질병 없음' 항목 클릭 이벤트
        item0.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {                                // 클릭o -> 다른 항목 전부 비활성화 및 체크 해제 (+ 다음 버튼 활성화)
                for (i in 1 until checkBoxList.size) {
                    checkBoxList[i].isChecked = false
                    checkBoxList[i].isEnabled = false
                }
                setCheckedConfirmed(true)
            }
            else {                                          // 클릭x -> 다른 항목 전부 활성화 (+ 다음 버튼 비활성화)
                for (i in 1 until checkBoxList.size)
                    checkBoxList[i].isEnabled = true
                setCheckedConfirmed(false)
            }
        }

        for (box in checkBoxList.filter { it != item0 }) {  // 첫 항목 제외 클릭 시, 다음 버튼 활성화
            box.setOnCheckedChangeListener { checkBox, isChecked ->
                if (box.isChecked)  setCheckedConfirmed(true)
                else                setCheckedConfirmed(false)

            }
            if (is_Checked_Confirmed)
                break
        }
    }

    // '다음' 버튼 활성화 함수
    fun updateNextButton() {
        if (is_Checked_Confirmed) {
            btn_next.isEnabled = true
            btn_next.setBackgroundResource(R.drawable.design_enabled_button)
        } else {
            btn_next.isEnabled = false
            btn_next.setBackgroundResource(R.drawable.design_disabled_button)
        }
    }

    // '뒤로가기' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickBackButton() {
        btn_back.setOnClickListener {
            lifecycleScope.launch {
                clearUserDiseases(this@SignUpActivity3)
            }
            navigateTo(
                SignUpActivity2::class.java,
                "source" to source,
                reverseAnimation = true
            )
        }
    }

    // 기기 내장 뒤로가기 버튼 클릭 이벤트
    private fun click_backpressdKey() {
        onBackPressedDispatcher.addCallback(this) {
            lifecycleScope.launch {
                clearUserDiseases(this@SignUpActivity3)
            }
            navigateTo(
                SignUpActivity2::class.java,
                "source" to source,
                reverseAnimation = true
            )
        }
    }

    // '다음' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickNextButton(targetActivity: Class<out AppCompatActivity>) {
        btn_next.setOnClickListener {

            val selectedDiseases = checkBoxList
                .filter { it.isChecked }
                .map { it.text.toString() }

            lifecycleScope.launch {
                saveUserInfo(
                    context = this@SignUpActivity3,
                    diseases = selectedDiseases
                )
            }

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