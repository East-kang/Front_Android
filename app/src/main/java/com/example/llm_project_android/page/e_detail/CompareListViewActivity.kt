package com.example.llm_project_android.page.e_detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.adapter.InsuranceAdapter
import com.example.llm_project_android.data.sample.Products_Insurance
import com.example.llm_project_android.db.user.MyDatabase
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.page.c_product.ProductDetailActivity
import kotlinx.coroutines.launch


class CompareListViewActivity : AppCompatActivity() {

    private lateinit var btn_back: ImageButton      // 뒤로 가기 버튼
    private lateinit var title: TextView            // 상품 카테고리
    private lateinit var companyList: List<Button>  // 기업 버튼 리스트
    private lateinit var filter: Spinner            // 정렬 스피너
    private lateinit var recyclerView: RecyclerView // 상품 목록

    private lateinit var data: Map<String, Any?>    // 필터할 카테고리명

    private lateinit var adapter: InsuranceAdapter
    private val selectedCategory: MutableList<String> = mutableListOf()
    private val selectedCompany: MutableSet<String> = mutableSetOf()
    private var selectedSortType: String = ""       // 정렬 타입


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.e_page_compare_list_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_back = findViewById(R.id.backButton)
        title = findViewById(R.id.category)
        companyList = listOf(
            findViewById(R.id.company0),
            findViewById(R.id.company1),
            findViewById(R.id.company2)
        )
        filter = findViewById(R.id.list_filter)
        recyclerView = findViewById(R.id.item_group)

        data = getPassedExtras(
            listOf(
                "category" to String::class.java,
                "item" to String::class.java
            )
        )

        init()              // 초기 뷰 구성
        filtering_Company() // 회사 카테고리 버튼 클릭 이벤트
        sorting_Insurances({ type -> selectedSortType = type})  // 상품 정렬 이벤트
        click_Events()
    }

    /* 초기 뷰 구성 */
    private fun init() {
        title.text = data["category"] as String
        showing_Insurances()
        selectedCategory.clear()
        selectedCategory.add(data["category"] as String)

    }

    /* 상품 띄우기 */
    private fun showing_Insurances() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InsuranceAdapter(Products_Insurance.productList)
        recyclerView.adapter = adapter

        // 가입 여부 태그 띄우기 여부 로직
        lifecycleScope.launch {
            val dao = MyDatabase.getDatabase(this@CompareListViewActivity).getMyDao()
            val user = dao.getLoggedInUser()

            user?.let {
                adapter.setEnrolls(it.subscriptions)
            }
        }

        // 기존 상품을 목록에서 제외
        val excludeName = data["item"] as String
        excludeName.let { adapter.addExcludedName(it) }

        selectedCategory.clear()
        selectedCategory.add(data["category"] as String)
        item_Filter()
    }

    /* 회사 카테고리 필터링 함수 */
    private fun filtering_Company() {
        for (i in 0 until companyList.size) {
            companyList[i].setOnClickListener {
                change_Types_Company(i, companyList[i])
                item_Filter()   // 아이템 필터링
            }
        }
    }

    /* 회사 카테고리 속성 변경 함수 (비활성화 할 버튼 / 활성화할 버튼) */
    private fun change_Types_Company(index: Int, select_button: Button){
        if (select_button.isSelected) {
            select_button.background.setTint(active_Color(-1))
            select_button.setTextColor("#666666".toColorInt())
            select_button.isSelected = false
            selectedCompany.remove(select_button.text.toString())
        } else {
            select_button.background.setTint(active_Color(index))
            select_button.setTextColor(Color.WHITE)
            select_button.isSelected = true
            selectedCompany.add(select_button.text.toString())
        }
    }

    /* 회사 종류에 따른 버튼 색상 부여 함수 */
    private fun active_Color(index: Int): Int{
        return when (index) {
            0 -> "#4885FF".toColorInt()
            1 -> "#FF9500".toColorInt()
            2 -> "#FFCC00".toColorInt()
            else -> Color.WHITE
        }
    }

    /* 상품 정렬 함수 */
    private fun sorting_Insurances(onSortedSelected: (String) -> Unit) {
        val filterList = resources.getStringArray(R.array.list_filter)
        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterList)
        filter.adapter = spinnerAdapter

        filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                val item = filterList[position]     // item에 선택한 정렬 기준 할당
                onSortedSelected(item)              // 선택 항목 저장
                item_Filter()                       // 아이템 필터링
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
    }

    /* 아이템 필터링 */
    fun item_Filter() {
        adapter.applyFilters(
            filter1 = selectedCategory.firstOrNull(),
            selectedCompany.toList(),
            selectedSortType
        )
    }

    private fun click_Events() {
        /* 상품 클릭 이벤트 */
        click_Items()

        /* 뒤로 가기 버튼 클릭 이벤트 */
        btn_back.setOnClickListener { back_Event() }

        /* 기기 내장 뒤로 가기 버튼 클릭 이벤트 */
        onBackPressedDispatcher.addCallback(this) { back_Event() }

    }

    /* 뒤로 가기 이벤트 */
    private fun back_Event() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    /* 상품 클릭 이벤트 */
    private fun click_Items() {
        adapter.itemClick = object : InsuranceAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val selectedItem = adapter.getItem(position)

                navigateTo(
                    LottieView::class.java,
                    "icon" to selectedItem.company_icon,
                    "company" to selectedItem.company_name,
                    "category" to selectedItem.category,
                    "name" to selectedItem.name
                )
            }
        }
    }
}