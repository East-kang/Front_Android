package com.example.llm_project_android

import android.os.Bundle
import android.util.Log
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
        val data = getPassedStrings(
            "id", "pw", "email", "source",
            "name", "birth", "phone", "gender", "married", "job")

        // 초기 설정 (버튼 비활성화)
        updateNextButton()

        // item 체크
        items_check({ is_Checked_Confirmed }, { is_Checked_Confirmed = it })

        // 뒤로가기 버튼 클릭 이벤트 (to SignUpActivity2)
        clickBackButton(btn_back, data, SignUpActivity2::class.java)

        // 다음 버튼 클릭 이벤트 (to SignUpActivity4)
        clickNextButton(btn_next, data, SignUpActivity4::class.java)

        // 화면 전환 간 데이터 유지 (SignUpActivity4.kt -> SignUpActivity3.kt)
        restorePassedData()
    }


    fun restorePassedData() {
        val data = getPassedBooleans(
            "disease0", "disease1", "disease2", "disease3", "disease4",
            "disease5", "disease6", "disease7", "disease8", "disease9"
        )
        for (i in 0 until 10)
            Log.d("restore","data$i: "+data["disease$i"])
        if (data["disease0"] == true)                                   // item0만 체크, 나머지는 해제, 비활성화
            for (i in checkBoxList.indices) {
                checkBoxList[i].isChecked = (i == 0)
                checkBoxList[i].isEnabled = (i == 0)
            }
        else {
            for (i in 0 until checkBoxList.size)
                checkBoxList[i].isChecked = (data["disease$i"] == true) // item1 ~ item9 중 true인 항목만 체크
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

            for (i in 0 until 10)
                Log.d("check","data$i: "+checkBoxList[i].isChecked)
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
            btn_next.setBackgroundResource(R.drawable.enabled_button)
        } else {
            btn_next.isEnabled = false
            btn_next.setBackgroundResource(R.drawable.disabled_button)
        }
    }

    // '뒤로가기' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickBackButton(backButton: View, data: Map<String, Any>, targetActivity: Class<out AppCompatActivity>) {
        backButton.setOnClickListener {
            navigateTo(
                targetActivity,
                *data.mapValues { it.value }.toList().toTypedArray(),
                reverseAnimation = true
            )
        }
    }

    // '다음' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickNextButton(nextButton: View, data: Map<String, Any>, targetActivity: Class<out AppCompatActivity>) {
        val diseaseData = (0 until 10).map { i ->
            "disease$i" to checkBoxList[i].isChecked
        }

        nextButton.setOnClickListener {
            navigateTo(
                targetActivity,
                *data.mapValues { it.value }.toList().toTypedArray(),
                *diseaseData.toTypedArray()
            )
        }
    }
}