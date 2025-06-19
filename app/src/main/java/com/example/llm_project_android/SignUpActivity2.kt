package com.example.llm_project_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_up_view2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_back = findViewById<ImageButton>(R.id.backButton)       // 뒤로가기 버튼
        val btn_next = findViewById<Button>(R.id.next_Button)           // 다음 버튼
        val name = findViewById<EditText>(R.id.name_editText)           // 이름 입력란
        val birth = findViewById<EditText>(R.id.birth_editText)         // 생년월일 입력란
        val phone = findViewById<EditText>(R.id.phone_number_editText)  // 전화번호 입력란
        val gender_M = findViewById<RadioButton>(R.id.radioMale)        // 성별 (남)
        val gender_F = findViewById<RadioButton>(R.id.radioFemale)      // 성별 (여)
        val married_N = findViewById<RadioButton>(R.id.radioSingle)     // 결혼여부 (미혼)
        val married_Y = findViewById<RadioButton>(R.id.radioMarried)    // 결혼여부 (기혼)
        val job = findViewById<Spinner>(R.id.job_spinner)               // 직업 드롭다운

        // 이전 화면에서 받아온 데이터
        val id = intent.getStringExtra("id") ?: ""
        val pw = intent.getStringExtra("pw") ?: ""
        val email = intent.getStringExtra("email") ?: ""
    }
}