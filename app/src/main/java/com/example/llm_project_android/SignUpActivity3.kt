package com.example.llm_project_android

import android.os.Bundle
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
        val id = intent.getStringExtra("id") ?: ""
        val pw = intent.getStringExtra("pw") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val source = intent.getStringExtra("source") ?: ""
        val name = intent.getStringExtra("name") ?: ""
        val birth = intent.getStringExtra("birth") ?: ""
        val phone = intent.getStringExtra("phone") ?: ""
        val gender = intent.getStringExtra("gender") ?: ""
        val married = intent.getStringExtra("married") ?: ""
        val job = intent.getStringExtra("job") ?: ""
        
    }
}