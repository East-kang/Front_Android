package com.example.llm_project_android.page.c_product

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
import com.example.llm_project_android.adapter.InsuranceAdapter
import com.example.llm_project_android.data.model.Product
import com.example.llm_project_android.data.sample.Products_Insurance
import com.example.llm_project_android.db.user.MyDatabase
import com.example.llm_project_android.db.wishList.WishedManager
import com.example.llm_project_android.functions.RecentViewedManager
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.page.d_menu.EnrolledViewActivity
import com.example.llm_project_android.page.d_menu.WishViewActivity
import com.example.llm_project_android.page.e_detail.CompareViewActivity
import com.example.llm_project_android.page.e_detail.PdfView
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var btn_back: ImageButton          // 뒤로 가기 버튼
    private lateinit var btn_wish: ImageButton          // 찜 버튼
    private lateinit var btn_compare: Button            // 비교 버튼
    private lateinit var btn_pdf: Button                // pdf 버튼
    private lateinit var icon: ImageView                // 기업 아이콘
    private lateinit var company_name: TextView         // 기업명
    private lateinit var category: TextView             // 상품 카테고리
    private lateinit var bookmark: TextView             // 추천 북마크
    private lateinit var insurance_name: TextView       // 상품명
    private lateinit var enroll: TextView               // 가입 여부

    private lateinit var data: Map<String, Any?>        // 상품 데이터 맵

    private var source: String = ""                     // 이전 화면 소스
    private var data_icon: Int = 0                      // 기업 아이콘 소스
    private var data_company: String = ""               // 기업명 변수
    private var data_category:String = ""               // 상품 카테고리 변수
    private var data_name: String = ""                  // 상품명 변수
    private var data_recommendation: Boolean = false    // AI 추천 여부 변수
    private var data_isWished: Boolean = false          // 찜 여부 변수

    lateinit var wishedManager: WishedManager           // 찜 관리 매니저


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
        wishedManager = WishedManager(this)

        btn_back = findViewById(R.id.backButton)
        btn_wish = findViewById(R.id.wishList_button)
        btn_compare = findViewById(R.id.compare_button)
        btn_pdf = findViewById(R.id.pdf_button)
        icon = findViewById(R.id.company_icon)
        company_name = findViewById(R.id.company_name)
        category = findViewById(R.id.category)
        bookmark = findViewById(R.id.bookmark)
        insurance_name = findViewById(R.id.insurance_name)
        enroll = findViewById(R.id.enroll)

        // 이전 화면에서 받아온 데이터
        data = getPassedExtras(
            listOf(
                "source" to String::class.java,
                "name" to String::class.java
            )
        )

        RecentViewedManager.init(this)  // 최근 조회 기능 초기화 (SharedPreferences 사용 준비)

        init()              // 초기 진입 반영 반영
        click_Buttons()     // 버튼 클릭 이벤트

    }

    /* 초기 진입 반영 */
    fun init() {
        source = data["source"] as String
        data_name = data["name"] as String
        val insurance = Products_Insurance.productList.find { it.name == data_name }

        if (insurance != null) {
            data_icon = insurance.company_icon
            data_company = insurance.company_name
            data_category = insurance.category
            data_recommendation = insurance.recommendation
        }

        // 아이템 디자인
        icon.setBackgroundResource(data_icon)
        company_name.text = data_company
        category.text = data_category
        insurance_name.text = data_name
        bookmark.visibility = if (data_recommendation) View.VISIBLE else View.GONE

        // 찜 여부
        lifecycleScope.launch {
            // 찜 여부
            data_isWished = wishedManager.isWished(data_name)     // 아이템 값 저장
            updateWishButtonUI() // UI 반영

            // 가입 여부
            val dao = MyDatabase.getDatabase(this@ProductDetailActivity).getMyDao()
            val user = dao.getLoggedInUser()

            user?.let {
                enroll.visibility = if (data_name in it.subscriptions) View.VISIBLE else View.GONE
            }
        }

        // 최근 목록에 저장
        insurance?.let { RecentViewedManager.addItem(it) }
    }

    /* 찜 버튼 UI 업데이트 */
    private fun updateWishButtonUI() {
        if (data_isWished)
            btn_wish.setImageResource(R.drawable.vector_image_ic_wish_on)
        else
            btn_wish.setImageResource(R.drawable.vector_image_ic_wish_off)
    }

    /* 버튼 클릭 이벤트 모음 */
    private fun click_Buttons() {
        click_WishButton()      // 찜 버튼 클릭 이벤트
        click_CompareButton()   // 비교하기 버튼 클릭 이벤트
        click_PdfButton()       // pdf 버튼 클릭 이벤트
        clickBackButton()       // 뒤로가기 버튼 클릭 이벤트
    }

    /* 찜 목록 버튼 클릭 이벤트 정의 함수 */
    fun click_WishButton() {
        btn_wish.setOnClickListener {
            lifecycleScope.launch {
                if (data_isWished) {    // 찜 → 해제
                    wishedManager.removeWish(data_name)
                    data_isWished = false
                } else {                // 해제 -> 찜
                    wishedManager.addWish(data_name)
                    data_isWished = true
                }
                // UI 반영
                updateWishButtonUI()
            }
        }
    }

    /* 비교하기 버튼 클릭 이벤트 정의 함수 */
    fun click_CompareButton() {
        btn_compare.setOnClickListener {
            navigateTo(
                CompareViewActivity::class.java,
                "source" to source,
                "source1" to "ProductDetailView",
                "name" to data_name)
        }

    }

    /* 상품 pdf 파일 열기 버튼 클릭 이벤트 정의 함수 */
    fun click_PdfButton() {
        btn_pdf.setOnClickListener { navigateTo(PdfView::class.java, "pdf" to "sample") }
    }

    /* 뒤로 가기 이벤트 정의 함수 */
    fun AppCompatActivity.clickBackButton() {
        /* 뒤로가기 버튼 클릭 */
        btn_back.setOnClickListener {
            when (source) {
                "MainViewActivity" -> navigateTo(MainViewActivity::class.java, reverseAnimation = true)
                "CategoryView" -> navigateTo(CategoryView::class.java, "category" to data["category"], reverseAnimation = true)
                "WishListView" -> navigateTo(WishViewActivity::class.java, "source" to "ProductDetailView", reverseAnimation = true)
                "EnrolledView" -> navigateTo(EnrolledViewActivity::class.java, "source" to "ProductDetailView", reverseAnimation = true)
            }
        }

        /* 기기 내장 뒤로 가기 버튼 클릭 */
        onBackPressedDispatcher.addCallback(this) {
            when (source) {
                "MainViewActivity" -> navigateTo(MainViewActivity::class.java, reverseAnimation = true)
                "CategoryView" -> navigateTo(CategoryView::class.java, "category" to data["category"], reverseAnimation = true)
                "WishListView" -> navigateTo(WishViewActivity::class.java, "source" to "ProductDetailView", reverseAnimation = true)
                "EnrolledView" -> navigateTo(EnrolledViewActivity::class.java, "source" to "ProductDetailView", reverseAnimation = true)
            }
        }
    }
}