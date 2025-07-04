package com.example.llm_project_android

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
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
    private var category_num: Int = 0       // 현재 선택된 상품 카테고리 인덱스
    private var company_num: Int = -1       // 현재 선택된 회사 카테고리 인덱스 (-1: 미선택)

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
        select_product_category()

        // 뒤로가기 버튼 클릭 이벤트 (to MainViewActivity)
        clickBackButton(btn_back, Page_MainViewActivity::class.java)

    }

    // 상품 카테고리 속성 변경 함수 (비활성화 할 버튼, 뷰 / 활성화할 버튼, 뷰)
    fun change_types_product(toInactive_button: Button, toInactive_view: View, toActive_button: Button, toActive_view: View) {
        // 활성화 버튼 (TextStyle: 굵게, TextColor: 검정)
        toActive_button.setTypeface(null, Typeface.BOLD); toActive_button.setTextColor(Color.BLACK)

        // 활성화 뷰 (Background: 검정색)
        toActive_view.setBackgroundColor(Color.BLACK)

        // 비활성화 버튼 (TextStyle: 일반, TextColor: 회색)
        toInactive_button.setTypeface(null, Typeface.NORMAL); toInactive_button.setTextColor("#8D8D92".toColorInt())

        // 비활성화 뷰 (Background: 흰색)
        toInactive_view.setBackgroundColor(Color.WHITE)
    }

    // 상품 카테고리 선택 함수 (현재 버튼 활성화 이벤트만 구현) (+ 아이템 뷰 전환 이벤트도 구현해야함)
    fun select_product_category() {
        for (i in 0 until categoryList.size) {
            categoryList[i].setOnClickListener {
                change_types_product(categoryList[category_num], underline[category_num], categoryList[i], underline[i])
                category_num = i
            }
        }
    }

    // 회사 카테고리 속성 변경 함수 (비활성화 할 버튼 / 활성화할 버튼)
    fun change_types_company(toInactive_button: Button, toActive_button: Button) {
        if (toInactive_button != toActive_button) {
            // 활성화할 버튼 활성화
            toActive_button.setBackgroundColor("#507CE8".toColorInt())
            toActive_button.setTextColor(Color.WHITE)
        }

        // 비활성화할 버튼 비활성화
        toInactive_button.setBackgroundColor(Color.WHITE)
        toInactive_button.setTextColor("666666".toColorInt())

    }

    // 회사 카테고리 필터링 함수
    fun filtering_company() {
        for (i in 0 until companyList.size) {
            companyList[i].setOnClickListener {
                if (i == company_num) {
                    change_types_company(companyList[company_num], companyList[i])
                    company_num = -1
                } else {
                    change_types_company(companyList[company_num], companyList[i])
                    company_num = i
                }
            }
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