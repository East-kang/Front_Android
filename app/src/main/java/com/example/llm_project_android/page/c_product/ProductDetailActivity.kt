package com.example.llm_project_android.page.c_product

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.navigateTo

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var btn_back: ImageButton
    private lateinit var btn_wish: ImageButton
    var isChecked_wish: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.page_activity_product_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_back = findViewById<ImageButton>(R.id.backButton)
        btn_wish = findViewById<ImageButton>(R.id.wishList_button)

        val source = getPassedExtras("source", String::class.java)["source"] as? String ?: "" // 이전 화면 소스


        // 찜 버튼 클릭 이벤트
        click_WishButton()
        
        // 뒤로가기 버튼 클릭 이벤트
        clickBackButton(btn_back, source, MainViewActivity::class.java,  CategoryView::class.java)
    }

    // 찜 목록 버튼 클릭 이벤트 정의 함수
    fun click_WishButton() {
        btn_wish.setOnClickListener { 
            if (isChecked_wish) {
                btn_wish.setBackgroundResource(R.drawable.vector_image_ic_wish_off)
                isChecked_wish = false
            } else {
                btn_wish.setBackgroundResource(R.drawable.vector_image_ic_wish_on)
                isChecked_wish = true
            }
        }
    }

    // '뒤로가기' 버튼 클릭 이벤트 정의 함수
    fun AppCompatActivity.clickBackButton(backButton: View, source: String, targetActivity1: Class<out AppCompatActivity>, targetActivity2: Class<out AppCompatActivity>) {
        backButton.setOnClickListener {
            when (source) {
                "MainViewActivity" -> navigateTo(targetActivity1, reverseAnimation = true)
                "CategoryView" -> navigateTo(targetActivity2, reverseAnimation = true)
            }
        }
    }
}