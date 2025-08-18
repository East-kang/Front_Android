package com.example.llm_project_android.page.c_product

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.llm_project_android.R

class CompareViewActivity : AppCompatActivity() {

    private lateinit var btn_back: ImageButton          // 뒤로 가기 버튼

    private lateinit var item_Layout0: ConstraintLayout // 기존 상품 뷰
    private lateinit var icon0: ImageView               // 기존 상품 기업 아이콘
    private lateinit var company0: TextView             // 기존 상품 기업명
    private lateinit var category0: TextView            // 기존 상품 카테고리
    private lateinit var bookmark0: TextView            // 기존 상품 추천 북마크
    private lateinit var name0: TextView                // 기존 상품 이름

    private lateinit var item_Layout1: ConstraintLayout // 비교 상품 뷰
    private lateinit var init_View: ConstraintLayout    // 상품 추가 뷰
    private lateinit var item_View: ConstraintLayout    // 상품 뷰
    private lateinit var icon1: ImageView               // 비교 상품 기업 아이콘
    private lateinit var company1: TextView             // 비교 상품 기업명
    private lateinit var category1: TextView            // 비교 상품 카테고리
    private lateinit var bookmark1: TextView            // 비교 상품 추천 북마크
    private lateinit var name1: TextView                // 비교 상품 이름
    
    private lateinit var field: LinearLayout            // 결과 뷰
    private lateinit var value_List: List<String>       // 상품 소개 값 리스트


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.c_page_compare_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /* 초기 뷰 구성 */
    private fun init() {

    }
}