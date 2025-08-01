package com.example.llm_project_android.page.c_product

import UserManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.llm_project_android.R
import com.example.llm_project_android.data.sample.Products_Insurance
import com.example.llm_project_android.db.user.MyDatabase
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.navigateTo
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var btn_back: ImageButton
    private lateinit var btn_wish: ImageButton
    private lateinit var btn_compare: Button
    private lateinit var btn_details: Button
    private lateinit var icon: ImageView
    private lateinit var company_name: TextView
    private lateinit var category: TextView
    private lateinit var bookmark: TextView
    private lateinit var insurance_name: TextView

    private lateinit var data: Map<String, Any?>
    private lateinit var userManager: UserManager

    private var source: String = ""
    private var data_icon: Int = 0
    private var data_company: String = ""
    private var data_category:String = ""
    private var data_name: String = ""
    private var data_recommendation: Boolean = false
    private var data_isWished: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.c_page_product_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 초기화
        userManager = UserManager(this)

        btn_back = findViewById<ImageButton>(R.id.backButton)
        btn_wish = findViewById<ImageButton>(R.id.wishList_button)
        btn_compare = findViewById<Button>(R.id.compare_button)
        btn_details = findViewById<Button>(R.id.details_button)
        icon = findViewById<ImageView>(R.id.company_icon)
        company_name = findViewById<TextView>(R.id.company_name)
        category = findViewById<TextView>(R.id.category)
        bookmark = findViewById<TextView>(R.id.bookmark)
        insurance_name = findViewById<TextView>(R.id.insurance_name)


        // 이전 화면에서 받아온 데이터
        data = getPassedExtras(
            listOf(
                "source" to String::class.java,
                "company_icon" to Int::class.java,
                "company_name" to String::class.java,
                "category" to String::class.java,
                "insurance_name" to String::class.java,
                "recommendation" to Boolean::class.java,
                "isWished" to Boolean::class.java
            )
        )
        
        // 초기 진입 반영 반영
        init()

        // 찜 버튼 클릭 이벤트
        click_WishButton()
        
        // 뒤로가기 이벤트
        clickBackButton()
    }

    // 초기 진입 반영
    fun init() {
        source = data["source"] as String
        data_icon = data["company_icon"] as Int
        data_company = data["company_name"] as String
        data_category = data["category"] as String
        data_name = data["insurance_name"] as String
        data_recommendation = data["recommendation"] as Boolean     // 아이템 값 저장


        // 아이템 디자인
        icon.setBackgroundResource(data_icon)
        company_name.text = data_company
        category.text = data_category
        insurance_name.text = data_name
        bookmark.visibility = if (data_recommendation) View.VISIBLE else View.GONE
    }

    // 찜 버튼 UI 업데이트
    private fun updateWishButtonUI() {
        if (data_isWished)
            btn_wish.setImageResource(R.drawable.vector_image_ic_wish_on)
        else
            btn_wish.setImageResource(R.drawable.vector_image_ic_wish_off)
    }

    // 찜 목록 버튼 클릭 이벤트 정의 함수
    fun click_WishButton() {
        btn_wish.setOnClickListener {
            lifecycleScope.launch {
                val user = userManager.getUser() ?: return@launch
                val db = MyDatabase.getDatabase(this@ProductDetailActivity)


                if (data_isWished != null) {
                    // 이미 찜 → 삭제

                    data_isWished = false
                    btn_wish.setImageResource(R.drawable.vector_image_ic_wish_off)
                } else {
                    // 새로운 찜 → 추가
                    data_isWished = true
                    btn_wish.setImageResource(R.drawable.vector_image_ic_wish_on)
                }

                // UI 반영
                updateWishButtonUI()

                // 메모리 내 상품 리스트 상태 동기화
                Products_Insurance.productList.find { it.name == data_name }?.apply {
                    isWished = data_isWished
                    wishedTimeStamp = if (data_isWished) System.currentTimeMillis() else 0L
                }
            }
        }
    }

    // 비교하기 버튼 클릭 이벤트 정의 함수
    fun click_ComapareButton() {

    }

    fun click_DetailsButton() {

    }

    // 뒤로가기 이벤트 정의 함수
    fun AppCompatActivity.clickBackButton() {
        // 뒤로가기 버튼 클릭
        btn_back.setOnClickListener {
            when (source) {
                "MainViewActivity" -> navigateTo(MainViewActivity::class.java, reverseAnimation = true)
                "CategoryView" -> navigateTo(
                    CategoryView::class.java,
                    "category" to data["category"],
                    reverseAnimation = true)
            }
        }

        // 기기 내장 뒤로가기 버튼 클릭
        onBackPressedDispatcher.addCallback(this) {
            when (source) {
                "MainViewActivity" -> navigateTo(MainViewActivity::class.java, reverseAnimation = true)
                "CategoryView" -> navigateTo(
                    CategoryView::class.java,
                    "category" to data["category"],
                    reverseAnimation = true)
            }
        }
    }
}