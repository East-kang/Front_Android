package com.example.llm_project_android

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
    var is_Checked_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }        // 체크 완료 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_up_view3)
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
        val data = getPassedStrings("id", "pw", "email", "source", "name", "birth", "phone", "gender", "married", "job")

        // 초기 설정 (버튼 비활성화)
        updateNextButton()

        // 화면 전환 간 데이터 유지 (SignUpActivity4.kt -> SignUpActivity3.kt)
        restorePassedData()

        // item 체크
        items_check()

        // 뒤로가기 버튼 클릭 이벤트 (to SignUpActivity1)
        clickBackButton(btn_back, data, SignUpActivity2::class.java)

        // 다음 버튼 클릭 이벤트 (to SignUpActivity4)
        clickNextButton(btn_next, data, SignUpActivity4::class.java)
    }


    fun restorePassedData() {
        val data = getPassedBooleans(
            "disease0", "disease1", "disease2", "disease3", "disease4",
            "disease5", "disease6", "disease7", "disease8", "disease9"
        )

        if (data["disease0"] == true)                                   // item0만 체크, 나머지는 해제
            for (i in checkBoxList.indices)
                checkBoxList[i].isChecked = (i == 0)
        else {
            for (i in 1 until checkBoxList.size)
                checkBoxList[i].isChecked = (data["disease$i"] == true) // item1 ~ item9 중 true인 항목만 체크
            checkBoxList[0].isChecked = false                           // item0은 해제
        }
    }

    // item 체크
    fun items_check() {
        if (item0.isChecked) {
            for (i in 1 until checkBoxList.size) {
                checkBoxList[i].isChecked = false
                checkBoxList[i].isEnabled = false
            }
        } else {
            for (i in 1 until checkBoxList.size) {
                checkBoxList[i].isEnabled = true
            }
        }
    }

    // '다음' 버튼 활성화 함수
    fun updateNextButton() {
        if (is_Checked_Confirmed) {
            btn_next.isEnabled = true
            btn_next.setBackgroundResource(R.drawable.enabled_button)
        } else {
            btn_next.isEnabled = false
            btn_next.setBackgroundResource(R.drawable.disabled_button)
        }
    }

    // '뒤로가기' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickBackButton(backButton: View, data: Map<String, String>, targetActivity: Class<out AppCompatActivity>) {
        backButton.setOnClickListener {
            navigateTo(
                targetActivity,
                *data.mapValues { it.value }.toList().toTypedArray(),
                reverseAnimation = true
            )
        }
    }

    // '다음' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickNextButton(nextButton: View, data: Map<String, String>, targetActivity: Class<out AppCompatActivity>) {
        nextButton.setOnClickListener {
            navigateTo(
                targetActivity,
                *data.mapValues { it.value }.toList().toTypedArray(),
                "disease0" to item0.isChecked,
                "disease1" to item1.isChecked,
                "disease2" to item2.isChecked,
                "disease3" to item3.isChecked,
                "disease4" to item4.isChecked,
                "disease5" to item5.isChecked,
                "disease6" to item6.isChecked,
                "disease7" to item7.isChecked,
                "disease8" to item8.isChecked,
                "disease9" to item9.isChecked
            )
        }
    }
}