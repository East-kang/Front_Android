package com.example.llm_project_android.page.e_detail

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.llm_project_android.R
import com.example.llm_project_android.data.model.Insurance
import com.example.llm_project_android.data.sample.Products_Insurance
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.page.c_product.ProductDetailActivity
import kotlin.math.max

class CompareViewActivity : AppCompatActivity() {

    private lateinit var btn_back: ImageButton          // 뒤로 가기 버튼

    private lateinit var item_Layout0: ConstraintLayout // 기존 상품 뷰
    private lateinit var icon0: ImageView               // 기존 상품 기업 아이콘
    private lateinit var company0: TextView             // 기존 상품 기업명
    private lateinit var category0: TextView            // 기존 상품 카테고리
    private lateinit var recommendation0: ImageView     // 기존 상품 추천 북마크
    private lateinit var name0: TextView                // 기존 상품 이름

    private lateinit var item_Layout1: ConstraintLayout // 비교 상품 뷰
    private lateinit var init_View: ConstraintLayout    // 상품 추가 뷰
    private lateinit var item_View: ConstraintLayout    // 상품 뷰
    private lateinit var icon1: ImageView               // 비교 상품 기업 아이콘
    private lateinit var company1: TextView             // 비교 상품 기업명
    private lateinit var category1: TextView            // 비교 상품 카테고리
    private lateinit var recommendation1: ImageView     // 비교 상품 추천 북마크
    private lateinit var name1: TextView                // 비교 상품 이름
    private lateinit var btn_change: Button             // 비교 상품 변경 버튼
    
    private lateinit var field: LinearLayout            // 결과 뷰
    private lateinit var ai_View: TextView              // AI 분석 결과

    private var selectedItem0: Insurance? = null        // 기존 상품
    private var selectedItem1: Insurance? = null        // 비교 상품

    private lateinit var data: Map<String, Any?>        // 기존, 비교? 상품 정보
    private lateinit var value0: List<TextView>         // 기존 상품 상세 정보
    private lateinit var value1: List<TextView>         // 비교 상품 상세 정보

    private var count0 = 0  // 기존 상품이 더 좋은 항목 수
    private var count1 = 0  // 비교 상품이 더 좋은 항목 수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.e_page_compare_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_back = findViewById(R.id.backButton)

        item_Layout0 = findViewById(R.id.itemLayout0)
        icon0 = findViewById(R.id.icon0)
        company0 = findViewById(R.id.company0)
        category0 = findViewById(R.id.category0)
        recommendation0 = findViewById(R.id.recommendation0)
        name0 = findViewById(R.id.name0)

        item_Layout1 = findViewById(R.id.itemLayout1)
        init_View = findViewById(R.id.init_View)
        item_View = findViewById(R.id.item_View)
        icon1 = findViewById(R.id.icon1)
        company1 = findViewById(R.id.company1)
        category1 = findViewById(R.id.category1)
        recommendation1 = findViewById(R.id.recommendation1)
        name1 = findViewById(R.id.name1)
        btn_change = findViewById(R.id.changeButton)

        field = findViewById(R.id.field)
        ai_View = findViewById(R.id.ai_View)

        value0 = listOf(
            findViewById(R.id.value00), findViewById(R.id.value10), findViewById(R.id.value20), findViewById(R.id.value30),
            findViewById(R.id.value40), findViewById(R.id.value50), findViewById(R.id.value60), findViewById(R.id.value70)
        )

        value1 = listOf(
            findViewById(R.id.value01), findViewById(R.id.value11), findViewById(R.id.value21), findViewById(R.id.value31),
            findViewById(R.id.value41), findViewById(R.id.value51), findViewById(R.id.value61), findViewById(R.id.value71)
        )

        ai_View = findViewById(R.id.ai_View)




        get_Info()
        setup_View()
        click_Buttons()
    }

    /* 기존, 비교 상품 상단 정보 불러오기 */
    private fun get_Info() {
        val base = getPassedExtras(                 // 기존 상품 정보
            listOf(
                "source" to String::class.java,
                "source1" to String::class.java,
                "name" to String::class.java
            )
        )

        data = if ((base["source1"] as? String) == "LottieView") {
            val extra = getPassedExtras("name1", String::class.java)["name1"] as? String    // 비교 상품 정보
            if (extra != null) base + ("name1" to extra) else base
        } else base
    }

    /* 뷰 구성 */
    private fun setup_View() {
        if (data["source1"] == "ProductDetailView") {   // 아이템 선택 전
            init_View.visibility = View.VISIBLE
            item_View.visibility = View.GONE
            field.visibility = View.GONE

            input_Values(isSelected = false)     // 상품 상단 정보 적용
        }
        else if (data["source1"] == "LottieView") {     // 아이템 선택 후
            init_View.visibility = View.GONE
            item_View.visibility = View.VISIBLE
            field.visibility = View.VISIBLE

            input_Values(isSelected = true)     // 상품 상단, 세부 정보 적용
        }
    }

    /* 아이템 정보 반영 */
    private fun input_Values(isSelected: Boolean) {
        selectedItem0 = Products_Insurance.productList.find { it.name == data["name"] }
        selectedItem1 = Products_Insurance.productList.find { it.name == data["name1"] }
        var originalValue: List<Any>
        var compareValue: List<Any>

        selectedItem0?.let {                                    // 기존 상품 상단 뷰 구성
            icon0.setBackgroundResource(it.company_icon)
            company0.text = it.company_name
            category0.text = it.category
        }
        recommendation0.visibility = View.GONE
        name0.text = data["name"] as String

        if (isSelected) {           // 비교 상품 선택 했을 경우
            selectedItem0?.let {    // 기존 상품 세부 정보 값 대입
                value0[0].text = "월 " + it.payment + "만원"   // 월 보험료
                value0[1].text = "" // 납입 기간
                value0[2].text = "" // 보장 범위
                value0[3].text = "만원"   // 최대 보장 금액
                value0[4].text = ""   // 환급률
                value0[5].text = ""   // 특약 가능 여부
                value0[6].text = ""   // 가입 연령
                value0[7].text = ""   // 갱신 여부
            }
            selectedItem1?.let {    // 비교 상품 상단, 세부 정보 값 대입
                icon1.setBackgroundResource(it.company_icon)
                company1.text = it.company_name
                category1.text = it.category

                value1[0].text = "월 " + it.payment + "만원"   // 월 보험료
                value1[1].text = "" // 납입 기간
                value1[2].text = "" // 보장 범위
                value1[3].text = "만원"   // 최대 보장 금액
                value1[4].text = ""   // 환급률
                value1[5].text = ""   // 특약 가능 여부
                value1[6].text = ""   // 가입 연령
                value1[7].text = ""   // 갱신 여부
            }
            recommendation1.visibility = View.GONE
            name1.text = data["name1"] as String
        }
    }

    /* 아이템 추천 디자인 반영 */
    private fun design_Values(value0: TextView, value1: TextView, state: Int) {
        when (state) {
            0 -> {
                value0.setTextColor("#4D914C".toColorInt());   value1.setTextColor("#5E5E5E".toColorInt())
                value0.setTypeface(null, Typeface.BOLD);    value1.setTypeface(null, Typeface.NORMAL);
            }
            1 -> {
                value0.setTextColor("#5E5E5E".toColorInt());   value1.setTextColor("#4D914C".toColorInt())
                value0.setTypeface(null, Typeface.NORMAL);  value1.setTypeface(null, Typeface.BOLD);
            }
            else -> {
                value0.setTextColor("#4D914C".toColorInt());   value1.setTextColor("#4D914C".toColorInt())
                value0.setTypeface(null, Typeface.BOLD);    value1.setTypeface(null, Typeface.BOLD);
            }
        }
    }

    /* 상품 비교 */
    private fun compare_items(): String {


        // 텍스트 색상, 스타일 변경 로직 추가 예정

        return if (count0 > count1) data["name"] as String else data["name1"] as String
    }

    /* 상품 세부 사항 비교 */
    private fun compare_Details(item0: Any, item1: Any) {

    }


    /* 버튼 클릭 이벤트 */
    private fun click_Buttons() {

        /* 뒤로 가기 버튼 클릭 이벤트 */
        btn_back.setOnClickListener {
            navigateTo(
                ProductDetailActivity::class.java,
                "source" to data["source"],
                "name" to data["name"],
                reverseAnimation = true
            )
        }

        /* 기기 내장 뒤로가기 버튼 클릭 */
        onBackPressedDispatcher.addCallback(this) {
            navigateTo(
                ProductDetailActivity::class.java,
                "source" to data["source"],
                "name" to data["name"],
                reverseAnimation = true
            )
        }

        /* 상품 선택 클릭 이벤트 */
        init_View.setOnClickListener {
            selectedItem0?.let { item ->
                navigateTo(
                    CompareListViewActivity::class.java,
                    "source" to data["source"],
                    "category" to item.category,
                    "name" to data["name"]
                )
            }
        }
        /* 비교 상품 변경 버튼 클릭 이벤트 */
        btn_change.setOnClickListener {
            selectedItem0?.let { item ->
                navigateTo(
                    CompareListViewActivity::class.java,
                    "source" to data["source"],
                    "category" to item.category,
                    "name" to data["name"]
                )
            }
        }
    }
}