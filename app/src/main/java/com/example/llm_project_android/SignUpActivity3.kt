package com.example.llm_project_android

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
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
    private lateinit var btn_back: Button
    private lateinit var btn_next: Button
    //var is_Name_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateNextButton() }        // 체크 완료 여부

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

        btn_back = findViewById<Button>(R.id.backButton)    // 뒤로가기 버튼
        btn_next = findViewById<Button>(R.id.next_Button)   // 다음 버튼

        // 이전 화면에서 받아온 데이터
        val data = getPassedStrings("id", "pw", "email", "source", "name", "birth", "phone", "gender", "married", "job")

        // 화면 전환 간 데이터 유지 (SignUpAcitivity4.kt -> SignUpAcitivity3.kt)
        restorePassedData()


    }

    // 화면 전환간 데이터 수신 및 적용
    fun restorePassedData() {
        val data = getPassedBooleans(
            "disease0", "disease1", "disease2", "disease3", "disease4",
            "disease5", "disease6", "disease7", "disease8", "disease9"
        )

        val checkBoxList = listOf(
            item0, item1, item2, item3, item4,
            item5, item6, item7, item8, item9
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

    // Boolean으로 받은 Intent 값 받는 함수
    fun AppCompatActivity.getPassedBooleans(vararg keys: String): Map<String, Boolean> {
        return keys.associateWith { intent.getBooleanExtra(it, false) }
    }

    // item 체크

}