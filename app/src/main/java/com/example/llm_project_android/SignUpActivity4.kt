package com.example.llm_project_android

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import kotlin.properties.Delegates

class SignUpActivity4 : AppCompatActivity() {

    private lateinit var btn_back: ImageButton
    private lateinit var btn_clear: ImageButton
    private lateinit var btn_completion: Button
    private lateinit var insurance_layout: ConstraintLayout
    private lateinit var insurance: RadioGroup
    private lateinit var insurance_Y: RadioButton
    private lateinit var insurance_N: RadioButton
    private lateinit var search_insurance: SearchView
    private lateinit var tag_chip: ChipGroup
    private lateinit var recyclerView: RecyclerView

    var ArrayList<SingleItem>

    var is_Check_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateCompletionButton() }        // 체크 완료 여부
    var is_Search_Confirmed: Boolean by Delegates.observable(false) { _, _, _ -> updateCompletionButton() }       // 체크 완료 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_up_view4)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_back = findViewById<ImageButton>(R.id.backButton)
        btn_clear = findViewById<ImageButton>(R.id.text_clear)
        btn_completion = findViewById<Button>(R.id.completion_Button)
        insurance_layout = findViewById<ConstraintLayout>(R.id.insurance_layout)
        insurance = findViewById<RadioGroup>(R.id.radioInsurance)
        insurance_Y = findViewById<RadioButton>(R.id.insuranceYes)
        insurance_N = findViewById<RadioButton>(R.id.insuranceNo)
        search_insurance = findViewById<SearchView>(R.id.search_insurance)
        tag_chip = findViewById<ChipGroup>(R.id.tagChipGroup)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // 이전 화면에서 데이터 받아오기
        val data = getPassedStrings(
            "id", "pw", "email", "source",
            "name", "birth", "phone", "gender", "married", "job",
            "disease0", "disease1", "disease2", "disease3", "disease4", "disease5", "disease6", "disease7", "disease8", "disease9")

        // 초기 설정 (버튼 비활성화)
        updateCompletionButton()

        // 뒤로가기 버튼 클릭 이벤트 (to SignUpActivity3)
        clickBackButton(btn_back, data, SignUpActivity3::class.java)


    }

    // 검색어 입력 함수
    fun search_items(searchView: SearchView) {
        var searchViewTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {

            }
    }

    // '완료' 버튼 활성화 함수
    fun updateCompletionButton() {
        if (true) {
            btn_completion.isEnabled = true
            btn_completion.setBackgroundResource(R.drawable.enabled_button)
        } else {
            btn_completion.isEnabled = false
            btn_completion.setBackgroundResource(R.drawable.disabled_button)
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

    // '완료' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickCompletionButton(nextButton: View, data: Map<String, Any>, targetActivity: Class<out AppCompatActivity>) {
        nextButton.setOnClickListener {
            navigateTo(
                targetActivity,
                *data.mapValues { it.value }.toList().toTypedArray(),
                "" to it
            )
        }
    }
}