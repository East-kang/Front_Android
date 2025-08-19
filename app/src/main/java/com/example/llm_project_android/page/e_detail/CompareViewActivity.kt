package com.example.llm_project_android.page.e_detail

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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.navigateTo

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
    private lateinit var item0_value: List<TextView>    // 기존 상품 소개 값 리스트
    private lateinit var item1_value: List<TextView>    // 비교 상품 소개 값 리스트
    private lateinit var ai_View: TextView              // AI 분석 결과

    private lateinit var data: Map<String, Any?>        // 이전 화면 소스

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
        item0_value = listOf(
            findViewById(R.id.value00), findViewById(R.id.value10),
            findViewById(R.id.value20), findViewById(R.id.value30),
            findViewById(R.id.value40), findViewById(R.id.value50)
        )
        
        item1_value = listOf(
            findViewById(R.id.value01), findViewById(R.id.value11),
            findViewById(R.id.value21), findViewById(R.id.value31),
            findViewById(R.id.value41), findViewById(R.id.value51)
        )

        ai_View = findViewById(R.id.ai_View)

        data = getPassedExtras(
            listOf(
                "source" to String::class.java,
                "icon" to Int::class.java,
                "company" to String::class.java,
                "category" to String::class.java,
                "name" to String::class.java
            )
        )

        setupView()
        click_Buttons()
    }


    /* 뷰 구성 */
    private fun setupView() {
        if (data["source"] == "ProductDetailView") {        // 아이템 선택 전
            init_View.visibility = View.VISIBLE
            item_View.visibility = View.GONE
            field.visibility = View.GONE

            icon0.setBackgroundResource(data["icon"] as Int)
            company0.text = data["company"] as String
            category0.text = data["category"] as String
            recommendation1.visibility = View.GONE
            name0.text = data["name"] as String             // 기존 아이템 뷰 구성
        }
        else if (data["source"] == "ItemSelectView") {      // 아이템 선택 후
            init_View.visibility = View.GONE
            item_View.visibility = View.VISIBLE
            field.visibility = View.VISIBLE

            input_values()
            design_values()
        }
    }

    /* 아이템 정보 반영 */
    private fun input_values() {

    }

    /* 아이템 추천 디자인 반영 */
    private fun design_values() {

    }

    /* 버튼 클릭 이벤트 */
    private fun click_Buttons() {

        /* 뒤로 가기 버튼 클릭 이벤트 */
        btn_back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        /* 기기 내장 뒤로가기 버튼 클릭 */
        onBackPressedDispatcher.addCallback(this) {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        /* 상품 선택 클릭 이벤트 */
        init_View.setOnClickListener {
            navigateTo(CompareListViewActivity::class.java,
                "category" to category0.text.toString(),
                "item" to name0.text.toString())
        }
        
        /* 비교 상품 변경 버튼 클릭 이벤트 */
        btn_change.setOnClickListener {
            navigateTo(CompareListViewActivity::class.java,
                "category" to category0.text.toString(),
                "item" to name0.text.toString())
        }
    }
}