package com.example.llm_project_android

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt

class Page_CategoryView : AppCompatActivity() {

    private lateinit var btn_back: ImageButton
    private lateinit var categoryList: List<Button>
    private lateinit var underline: List<View>
    private lateinit var companyList: List<Button>
    private lateinit var filter: Spinner
    private lateinit var itemView: RecyclerView
    private var category_num: Int = 0               // 현재 선택된 상품 카테고리 인덱스
    private var company_num: Int = -1               // 현재 선택된 회사 카테고리 인덱스 (-1: 미선택)
    private var company_isChecked: Boolean = false  // 회사 카테고리 선택 여부 (true: 이미 선택됨, false: 선택된 버튼 없음)
    private var selectedSortType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.page_activity_category_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_back = findViewById<ImageButton>(R.id.backButton)   // 뒤로가기 버튼
        categoryList = listOf(                                       // 상품 카테고리 버튼 리스트
            findViewById<Button>(R.id.category_Entire),
            findViewById<Button>(R.id.category0),
            findViewById<Button>(R.id.category1),
            findViewById<Button>(R.id.category2),
            findViewById<Button>(R.id.category3),
            findViewById<Button>(R.id.category4),
            findViewById<Button>(R.id.category5)
        )
        underline = listOf(                                          // 상품 카테고리 뷰 리스트 (밑줄)
            findViewById<View>(R.id.underline_Entire),
            findViewById<View>(R.id.underline0),
            findViewById<View>(R.id.underline1),
            findViewById<View>(R.id.underline2),
            findViewById<View>(R.id.underline3),
            findViewById<View>(R.id.underline4),
            findViewById<View>(R.id.underline5)
        )
        companyList = listOf(                                        // 회사 카테고리 버튼 리스트
            findViewById<Button>(R.id.company0),
            findViewById<Button>(R.id.company1),
            findViewById<Button>(R.id.company2)
        )
        filter = findViewById<Spinner>(R.id.list_filter)        // 목록 필터 스피너
        itemView = findViewById<RecyclerView>(R.id.item_group)  // 상품 목록 리사이클러 뷰

        // 상품 카테고리 버튼 클릭 이벤트
        select_product_category()

        // 회사 카테고리 버튼 클릭 이벤트
        filtering_company()

        // 상품 정렬 이벤트
        sorting_insurances({ type -> selectedSortType = type})

        // 뒤로가기 버튼 클릭 이벤트 (to MainViewActivity)
        clickBackButton(btn_back, Page_MainViewActivity::class.java)

    }

    // 상품 카테고리 속성 변경 함수 (비활성화 할 버튼, 뷰 / 활성화할 버튼, 뷰)
    fun change_types_product(index: Int, toInactive_button: Button, toInactive_view: View, toActive_button: Button, toActive_view: View) {
        // 선택 카테고리 인덱스 변경
        category_num = index

        // 활성화 버튼 (TextStyle: 굵게, TextColor: 검정)
        toActive_button.setTypeface(null, Typeface.BOLD); toActive_button.setTextColor(Color.BLACK)

        // 활성화 뷰 (Background: 검정색)
        toActive_view.background.setTint(Color.BLACK)

        // 비활성화 버튼 (TextStyle: 일반, TextColor: 회색)
        toInactive_button.setTypeface(null, Typeface.NORMAL); toInactive_button.setTextColor("#8D8D92".toColorInt())

        // 비활성화 뷰 (Background: 흰색)
        toInactive_view.background.setTint(Color.WHITE)
    }

    // 상품 카테고리 선택 함수 (현재 버튼 활성화 이벤트만 구현) (+ 아이템 뷰 전환 이벤트도 구현해야함)
    fun select_product_category() {
        for (i in 0 until categoryList.size) {
            categoryList[i].setOnClickListener {
                change_types_product(i, categoryList[category_num], underline[category_num], categoryList[i], underline[i])
            }
        }
    }

    // 회사 카테고리 속성 변경 함수 (비활성화 할 버튼 / 활성화할 버튼)
    fun change_types_company(index: Int, toInactive_button: Button?, toActive_button: Button) {
        if (company_isChecked) {        // 기존 카테고리 필터링 활성화 상태
            if (company_num != index) {                 // 누른 버튼이 기존에 활성화된 버튼이 아닐 경우
                company_num = index
                company_isChecked = true
                toActive_button.background.setTint("#507CE8".toColorInt())
                toActive_button.setTextColor(Color.WHITE)               // 활성화할 버튼 활성화
                toInactive_button?.background?.setTint(Color.WHITE)
                toInactive_button?.setTextColor("#666666".toColorInt())  // 비활성화할 버튼 비활성화
            } else {                                                    // 활성화된 버튼 누를 경우
                company_isChecked = false
                company_num = -1
                toInactive_button?.background?.setTint(Color.WHITE)
                toInactive_button?.setTextColor("#666666".toColorInt())  // 비활성화할 버튼 비활성화
            }
        } else {                        // 기존 카테고리 필터링 비활성화 상태
            company_isChecked = true
            company_num = index
            toActive_button.background.setTint("#507CE8".toColorInt())
            toActive_button.setTextColor(Color.WHITE)                   // 활성화할 버튼 활성화
        }
    }

    // 회사 카테고리 필터링 함수
    fun filtering_company() {
        for (i in 0 until companyList.size) {
            companyList[i].setOnClickListener {
                change_types_company(i, if (company_isChecked) companyList[company_num] else null, companyList[i])
            }
        }
    }

    // 상품 정렬 함수 (상품 정렬 기능 추가해야함)
    fun sorting_insurances(onSortedSelected: (String) -> Unit) {
        var filterList = resources.getStringArray(R.array.list_filter)
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterList)
        filter.adapter = adapter

        filter.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                val item = filterList[position]     // item에 선택한 정렬 기준 할당
                onSortedSelected(item)              // 선택 항목 저장
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
    }


    // '뒤로가기' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickBackButton(backButton: View, targetActivity: Class<out AppCompatActivity>) {
        backButton.setOnClickListener {
            navigateTo(
                targetActivity,
                reverseAnimation = true
            )
        }
    }
}