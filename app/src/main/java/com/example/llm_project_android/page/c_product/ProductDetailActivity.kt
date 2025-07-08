package com.example.llm_project_android.page.c_product

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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
    private lateinit var btn_compare: Button
    private lateinit var btn_details: Button
    private lateinit var icon: ImageView
    private lateinit var company_name: TextView
    private lateinit var category: TextView
    private lateinit var bookmark: TextView
    private lateinit var insurance_name: TextView

    private lateinit var data: Map<String, Any?>

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
                "recommendation" to Boolean::class.java
            )
        )
        
        // 상품 내용 반영
        init()

        // 찜 버튼 클릭 이벤트
        click_WishButton()
        
        // 뒤로가기 버튼 클릭 이벤트
        clickBackButton(btn_back, data["source"] as String, MainViewActivity::class.java,  CategoryView::class.java)
    }

    // 상품 내용 반영
    fun init() {
        icon.setBackgroundResource(data["company_icon"] as Int)
        company_name.text = data["company_name"] as String
        category.text = data["category"] as String
        insurance_name.text = data["insurance_name"] as String

        if (data["recommendation"] as Boolean)
            bookmark.visibility = View.VISIBLE
        else
            bookmark.visibility = View.GONE

        if (isChecked_wish) {
            btn_wish.setBackgroundResource(R.drawable.vector_image_ic_wish_on)
            isChecked_wish = true
        } else {
            btn_wish.setBackgroundResource(R.drawable.vector_image_ic_wish_off)
            isChecked_wish = false
        }
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

    // 비교하기 버튼 클릭 이벤트 정의 함수
    fun click_ComapareButton() {

    }

    fun click_DetailsButton() {

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