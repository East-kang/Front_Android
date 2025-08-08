package com.example.llm_project_android.page.d_menu

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.adapter.InsuranceAdapter
import com.example.llm_project_android.data.model.Insurance
import com.example.llm_project_android.data.sample.Products_Insurance
import com.example.llm_project_android.db.user.MyDAO
import com.example.llm_project_android.db.user.MyDatabase
import com.example.llm_project_android.db.user.User
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.page.c_product.MainViewActivity
import com.example.llm_project_android.page.c_product.ProductDetailActivity
import kotlinx.coroutines.launch

class EnrolledViewActivity : AppCompatActivity() {

    private lateinit var btn_back: ImageButton              // 뒤로 가기 버튼
    private lateinit var btn_cancle: Button                 // 취소 버튼
    private lateinit var btn_edit: Button                   // 편집,완료 버튼
    private lateinit var btn_add: ImageButton               // 상품 추가 버튼
    private lateinit var guideText: TextView                // "아이템 없음" 텍스트 뷰
    private lateinit var linearLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView         // 아이템 추가 뷰

    private lateinit var dao: MyDAO
    private var user: User ?= null
    private lateinit var adapter: InsuranceAdapter
    private var enrolledList: Set<String> = emptySet()          // 가입 상품 문자열 리스트
    private var allProduct: List<Insurance> = emptyList()       // 전체 보험 상품 리스트
    private var enrolledProducts: List<Insurance> = emptyList() // 가입 조건 필터링 리스트


    private var edit_state: Boolean = false                 // 편집 여부 변수 (true: 편집 모드, false: 읽기 모드)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.d_page_enrolled_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_back = findViewById(R.id.backButton)
        btn_cancle = findViewById(R.id.cancelButton)
        btn_edit = findViewById(R.id.editButton)
        btn_add = findViewById(R.id.addButton)
        guideText = findViewById(R.id.guide_text)
        linearLayout = findViewById(R.id.linearLayout)
        recyclerView = findViewById(R.id.item_group)

        dao = MyDatabase.getDatabase(this@EnrolledViewActivity).getMyDao()
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InsuranceAdapter(Products_Insurance.productList)
        recyclerView.adapter = adapter

        read_View()         // 초기 화면 구성
        enrolled_Items()    // 가입 상품 띄우기
        click_Items()       // 아이템 클릭 이벤트
        click_editBotton()
        click_BackButton()  // 뒤로가기 버튼 클릭 이벤트
    }

    // 가입 상품 목록 보여 주기 함수
    private fun enrolled_Items() {
        lifecycleScope.launch {
            user = dao.getLoggedInUser()

            user?.let {
                enrolledList = it.subscriptions.toSet()
                allProduct = Products_Insurance.productList

                // 가입 상품 필터링
                enrolledProducts = allProduct.filter { product ->
                    enrolledList.contains(product.name)
                }

                adapter.setEnrolls(it.subscriptions)
                adapter.updateList(enrolledProducts)
                update_View(enrolledProducts.isEmpty())
            }
        }
    }

    // 가입 상품 클릭 이벤트
    private fun click_Items() {
        adapter.itemClick = object : InsuranceAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val selectedItem = adapter.getItem(position)

                navigateTo(
                    ProductDetailActivity::class.java,
                    "source" to "EnrolledView",
                    "company_icon" to selectedItem.company_icon,
                    "company_name" to selectedItem.company_name,
                    "category" to selectedItem.category,
                    "insurance_name" to selectedItem.name,
                    "recommendation" to selectedItem.recommendation
                )
            }
        }
    }

    // 읽기 모드 뷰
    private fun read_View() {
        btn_back.visibility = View.VISIBLE
        btn_cancle.visibility = View.GONE
        btn_add.visibility = View.GONE
        btn_edit.text = "편집"
        edit_state = false
    }
    
    // 편집 모드 뷰
    private fun edit_View() {
        btn_back.visibility = View.GONE
        btn_cancle.visibility = View.VISIBLE
        btn_add.visibility = View.VISIBLE
        btn_edit.text = "완료"
        edit_state = true
    }
    
    // 편집 버튼 클릭 이벤트
    private fun click_editBotton() {
        btn_edit.setOnClickListener { 
            if (edit_state) {   // 편집 모드 -> 읽기 모드
                read_View()     // 읽기 모드 뷰로 전환

                lifecycleScope.launch {
                    adapter.setEnrolls(enrolledList.toList(), state = false)     // 아이템 투명화 해제
                }

            } else {            // 읽기 모드 -> 편집 모드
                edit_View()     // 편집 모드 뷰로 전환

                lifecycleScope.launch {
                    adapter.setEnrolls(enrolledList.toList(), state = true)     // 아이템 투명화 설정
                }
            }
        }
    }

    // 가입 상품 목록 존재 여부에 따른 뷰 구성
    private fun update_View(isEmpty: Boolean) {
        // 최근 상품 조회 목록 존재에 따른 뷰 구성
        if (isEmpty) {
            guideText.visibility = View.VISIBLE
            linearLayout.visibility = View.GONE
        } else {
            guideText.visibility = View.GONE
            linearLayout.visibility = View.VISIBLE
        }
    }



    // 뒤로가기 버튼 클릭 이벤트
    private fun click_BackButton() {
        // 뒤로가기 버튼 클릭
        btn_back.setOnClickListener {
            finish()
            navigateTo(
                MainViewActivity::class.java,
                "source" to "SubscribedView",
                reverseAnimation = true
            )
        }

        // 기기 내장 뒤로가기 버튼 클릭
        onBackPressedDispatcher.addCallback(this) {
            finish()
            navigateTo(
                MainViewActivity::class.java,
                "source" to "EnrolledView",
                reverseAnimation = true
            )
        }
    }
}