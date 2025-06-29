package com.example.llm_project_android

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import kotlin.properties.Delegates

class SignUpActivity4 : AppCompatActivity() {

    private lateinit var btn_back: ImageButton
    private lateinit var btn_completion: Button
    private lateinit var insurance: RadioGroup
    private lateinit var insurance_Y: RadioButton
    private lateinit var insurance_N: RadioButton
    private lateinit var search_insurance: SearchView
    private lateinit var tag_chip: ChipGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostContentAdapter

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
        btn_completion = findViewById<Button>(R.id.completion_Button)
        insurance = findViewById<RadioGroup>(R.id.radioInsurance)
        insurance_Y = findViewById<RadioButton>(R.id.insuranceYes)
        insurance_N = findViewById<RadioButton>(R.id.insuranceNo)
        search_insurance = findViewById<SearchView>(R.id.search_insurance)
        tag_chip = findViewById<ChipGroup>(R.id.tagChipGroup)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val insuranceList = resources.getStringArray(R.array.insurances).map { Post(it) }   // 문자열 배열 -> Post 객체 리스트로 변환

        // 이전 화면에서 데이터 받아오기
        val data = getPassedExtras(
            listOf(
                "id" to String::class.java, "pw" to String::class.java,
                "email" to String::class.java, "source" to String::class.java,          // SignUp1

                "name" to String::class.java, "birth" to String::class.java,
                "phone" to String::class.java, "gender" to String::class.java,
                "married" to String::class.java, "job" to String::class.java,           // SignUp2

                "disease0" to Boolean::class.java, "disease1" to Boolean::class.java,
                "disease2" to Boolean::class.java, "disease3" to Boolean::class.java,
                "disease4" to Boolean::class.java, "disease5" to Boolean::class.java,
                "disease6" to Boolean::class.java, "disease7" to Boolean::class.java,
                "disease8" to Boolean::class.java, "disease9" to Boolean::class.java    // SignUp3
            )
        )

        // 초기 설정 (버튼 비활성화)
        updateCompletionButton()

        search_items(search_insurance, insuranceList)

        // 뒤로가기 버튼 클릭 이벤트 (to SignUpActivity3)
        clickBackButton(btn_back, data.filter { it != null } as Map<String, Any>, SignUpActivity3::class.java)
    }

    // 검색어 입력 함수
    fun search_items(searchView: SearchView, insuranceList: List<Post>) {
        // 어댑터 설정 및 RecyclerView 연결
        adapter = PostContentAdapter(insuranceList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // 실시간 검색어 변경 감지 -> 필터 실행
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter?.filter(newText)
                return true
            }
        })
    }

    // 아이템 클릭 이벤트 함수
    fun select_items(){

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