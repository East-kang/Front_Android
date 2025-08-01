package com.example.llm_project_android.page.b_signup

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.properties.Delegates
import androidx.core.view.isNotEmpty
import androidx.lifecycle.lifecycleScope
import com.example.llm_project_android.data.model.Product
import com.example.llm_project_android.adapter.ProductContentAdapter
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.clearUserDiseases
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.handleTouchOutsideEditText
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.saveUserInfo
import com.example.llm_project_android.page.a_intro.LoginActivity
import com.example.llm_project_android.page.c_product.MainViewActivity
import kotlinx.coroutines.launch

class SignUpActivity4 : AppCompatActivity() {

    private lateinit var btn_back: ImageButton
    private lateinit var btn_completion: Button
    private lateinit var insurance: RadioGroup
    private lateinit var insurance_Y: RadioButton
    private lateinit var insurance_N: RadioButton
    private lateinit var search_insurance: SearchView
    private lateinit var tag_chip: ChipGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductContentAdapter
    private var insuranceList: List<Product> = emptyList() // 문자열 배열 -> Post 객체 리스트로 변환
    private val selectedProducts = mutableListOf<Product>()

    var is_Check_Confirmed: Boolean by Delegates.observable(true) { _, _, _ -> updateCompletionButton() }        // 체크 완료 여부
    var source: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.b_page_sign_up_view4)
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

        insuranceList = resources.getStringArray(R.array.insurances).map { Product(it) }

        // 이전 화면에서 받아온 데이터
        val extras = getPassedExtras("source", String::class.java)
        source = extras["source"] as? String ?: ""

        // 초기 설정 (버튼 비활성화)
        updateCompletionButton()

        // 보험 여부 체크 박스 체크 여부
        isChecked_insurance({ is_Check_Confirmed = it })

        // 검색창 클릭 이벤트
        active_searchBar()

        // Chip 존재 여부 실시간 확인
        updateByChipExistence({ is_Check_Confirmed = it })

        // 뒤로가기 이벤트 (to SignUpActivity3)
        clickBackButton()

        // 다음 버튼 클릭 이벤트 (to LoginActivity)
        clickCompletionButton(LoginActivity::class.java)
    }

    // ChipView / RecyclerView 활성화 처리 함수
    fun isActived(chip: Int, recycler: Int) {
        tag_chip.visibility = chip
        recyclerView.visibility = recycler
    }

    // '보험여부' 체크 박스 클릭 이벤트 함수 정의
    fun isChecked_insurance(setInsuranceConfirmed: (Boolean) -> Unit) {
        insurance.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {                          // 보험여부
                R.id.insuranceYes -> {                  // Yes
                    search_insurance.visibility = View.VISIBLE
                    isActived(View.VISIBLE, View.GONE)
                    setInsuranceConfirmed(false)
                }
                R.id.insuranceNo -> {                   // No
                    search_insurance.visibility = View.GONE
                    isActived(View.GONE, View.GONE)
                    setInsuranceConfirmed(true)

                    selectedProducts.clear()                             // Chip 선택 항목 초기화
                    tag_chip.removeAllViews()                            // ChipGroup 내 모든 chip 제거
                    adapter.updateList(emptyList())                      // RecyclerView 목록 초기화
                    search_insurance.setQuery("", false)    // 검색창 텍스트 제거
                }
            }
        }
    }

    // 검색창 클릭 함수
    fun active_searchBar() {
        search_insurance.setOnQueryTextFocusChangeListener { _, hasFocus ->    // 검색창 터치 포커스 여부 함수
            if (hasFocus) {     // 검색창 터치 시
                isActived(View.VISIBLE, View.GONE)
                search_items(search_insurance, insuranceList, { is_Check_Confirmed = it })

            }
        }
    }

    // 상품 검색 함수
    fun search_items(searchView: SearchView, insuranceList: List<Product>, setInsuranceConfirmed: (Boolean) -> Unit) {

        adapter = ProductContentAdapter(insuranceList)                 // 어댑터 설정 및 RecyclerView 연결
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        // 아이템 클릭 이벤트
        adapter.setOnItemClickListener { post ->
            selectedProducts.add(post)
            add_Chip(post.title)
            isActived(View.VISIBLE, View.GONE)
            search_insurance.setQuery("", false)
            search_insurance.clearFocus()
            setInsuranceConfirmed(true)

            val currentQuery = search_insurance.query.toString()
            val filtered = insuranceList
                .filterNot { selectedProducts.contains(it) }
                .filter { it.title.contains(currentQuery, ignoreCase = true)}
            adapter.updateList(filtered)
        }

        adapter.setOnEmptyResultCallback {
            isActived(View.VISIBLE, View.GONE)
        }

        // 실시간 검색어 변경 감지 -> 필터 실행
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank())
                    isActived(View.VISIBLE, View.GONE)
                else
                    isActived(View.GONE, View.VISIBLE)

                val filtered = insuranceList.filterNot { selectedProducts.contains(it) }
                    .filter { it.title.contains(newText ?: "", ignoreCase = true)}
                adapter.updateList(filtered)
                return true
            }
        })
    }

    // 아이템 태그 관리 함수
    fun add_Chip(text: String){
        val chip = Chip(this).apply {
            this.text = text
            isCloseIconEnabled = true
            isClickable = false
            isFocusable = false
            isFocusableInTouchMode = true
        }
            search_insurance.clearFocus()
        chip.setOnCloseIconClickListener {
            tag_chip.removeView(chip)

            val iterator = selectedProducts.iterator()
            while (iterator.hasNext()) {
                if (iterator.next().title == text.toString()) {
                    iterator.remove()
                    break
                }
            }

            val currentQuery = search_insurance.query.toString()
            val filtered = insuranceList.filterNot { selectedProducts.contains(it) }
                .filter { it.title.contains(currentQuery, ignoreCase = true) }
            adapter.updateList(filtered)
        }
        tag_chip.addView(chip)
        chip.bringToFront()
        tag_chip.bringToFront()
    }

    // Chip Group 내 chip 존재 여부 실시간 감지 함수
    fun updateByChipExistence(setInsuranceConfirmed: (Boolean) -> Unit) {
        if (insurance_Y.isChecked) {
            val hasChip = tag_chip.isNotEmpty()
            setInsuranceConfirmed(hasChip)
        }
    }

    // '완료' 버튼 활성화 함수
    fun updateCompletionButton() {
        if (is_Check_Confirmed) {
            btn_completion.isEnabled = true
            btn_completion.setBackgroundResource(R.drawable.design_enabled_button)
        } else {
            btn_completion.isEnabled = false
            btn_completion.setBackgroundResource(R.drawable.design_disabled_button)
        }
    }

    // 뒤로가기 이벤트 정의 함수
    fun AppCompatActivity.clickBackButton() {
        // 뒤로가기 버튼 클릭
        btn_back.setOnClickListener {
            navigateTo(
                SignUpActivity3::class.java,
                "source" to source,
                reverseAnimation = true
            )
        }

        // 기기 내장 뒤로가기 버튼 클릭
        onBackPressedDispatcher.addCallback(this) {
            navigateTo(
                SignUpActivity3::class.java,
                "source" to source,
                reverseAnimation = true
            )
        }
    }

    // '완료' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickCompletionButton(targetActivity: Class<out AppCompatActivity>) {
        btn_completion.setOnClickListener {
            // Chip 텍스트 배열로 추출
            val chipTexts = ArrayList<String>().apply() {
                for (i in 0 until tag_chip.childCount) {
                    val child = tag_chip.getChildAt(i)
                    if (child is Chip) add(child.text.toString())
                }
            }

            lifecycleScope.launch {
                saveUserInfo(
                    context = this@SignUpActivity4,
                    subscriptions = chipTexts
                )
            }

            navigateTo(
                targetActivity
                /* , +중앙 db에 데이터 전송 기능 */)
        }
    }

    // 키보드 숨기기 이벤트 (editText 이외의 영역을 눌렀을 경우, 스크롤 제외)
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        handleTouchOutsideEditText(this, ev)
        return super.dispatchTouchEvent(ev)
    }
}