package com.example.llm_project_android.page.d_menu

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.page.c_product.MainViewActivity

class SubscribedViewActivity : AppCompatActivity() {

    private lateinit var btn_back: ImageButton
    private lateinit var guideText: TextView
    private lateinit var linearLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.d_page_subscribed_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_back = findViewById(R.id.backButton)
        guideText = findViewById(R.id.guide_text)
        linearLayout = findViewById(R.id.linearLayout)
        recyclerView = findViewById(R.id.item_group)

        click_BackButton()
    }

    // 초기 화면 (가입 상품 목록 존재 여부에 따른 뷰 구성)
    private fun init(isEmpty: Boolean) {

        // 최근 상품 조회 목록 존재에 따른 뷰 구성
        if (isEmpty) {
            guideText.visibility = View.VISIBLE
            linearLayout.visibility = View.GONE
        } else {
            guideText.visibility = View.GONE
            linearLayout.visibility = View.VISIBLE
        }
    }

//    // 최근 조회 상품 클릭 이벤트
//    private fun click_Items() {
//        recentAdapter.itemClick = object : InsuranceAdapter.ItemClick {
//            override fun onClick(view: View, position: Int) {
//                val selectedItem = recentAdapter.getItem(position)
//
//                navigateTo(
//                    ProductDetailActivity::class.java,
//                    "source" to "RecentListView",
//                    "company_icon" to selectedItem.company_icon,
//                    "company_name" to selectedItem.company_name,
//                    "category" to selectedItem.category,
//                    "insurance_name" to selectedItem.name,
//                    "recommendation" to selectedItem.recommendation
//                )
//            }
//        }
//    }
//
//    // 최근 조회 목록 보여주기 함수
//    private fun recent_Items() {
//        val recentItems = RecentViewedManager.getRecentItems(10)
//        recentAdapter.updateList(recentItems)       // 리스트 갱신
//    }

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
                "source" to "SubscribedView",
                reverseAnimation = true
            )
        }
    }
}