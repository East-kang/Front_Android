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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.adapter.InsuranceAdapter
import com.example.llm_project_android.data.sample.Products_Insurance
import com.example.llm_project_android.db.wishList.WishedManager
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.page.c_product.MainViewActivity
import com.example.llm_project_android.page.c_product.ProductDetailActivity
import kotlinx.coroutines.launch

class WishViewActivity : AppCompatActivity() {

    private lateinit var btn_back: ImageButton
    private lateinit var guideText: TextView
    private lateinit var linearLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: InsuranceAdapter
    private var source = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.d_page_wish_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_back = findViewById(R.id.backButton)
        guideText = findViewById(R.id.guide_text)
        linearLayout = findViewById(R.id.linearLayout)
        recyclerView = findViewById(R.id.item_group)

        adapter = InsuranceAdapter(ArrayList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        source = getPassedExtras("source", String::class.java)["source"] as String

        get_WishList()      // 화면 최초 진입 시 데이터 불러오기
        click_BackButton()  // 뒤로가기 버튼 클릭 이벤트
    }

    /* 초기 화면 (찜 목록 존재 여부에 따른 뷰 구성) */
    private fun updateView(isEmpty: Boolean) {
        // 찜 목록 존재에 따른 뷰 구성
        if (isEmpty) {
            guideText.visibility = View.VISIBLE
            linearLayout.visibility = View.GONE
        } else {
            guideText.visibility = View.GONE
            linearLayout.visibility = View.VISIBLE
        }
    }

    /* 찜 목록 불러 오기 */
    private fun get_WishList() {
        lifecycleScope.launch {
            val manager = WishedManager(this@WishViewActivity)
            val wishList = manager.getAllWishes()

            // 뷰 상태 갱신
            updateView(wishList.isEmpty())

            if (wishList.isNotEmpty()) {
                val wishedInsuranceList = wishList.mapNotNull { product ->
                    Products_Insurance.productList.find { it.name == product.insuranceName}
                }
                adapter.updateList(wishedInsuranceList)
            } else {
                adapter.updateList(emptyList())
            }

            click_Item()
        }
    }

    /* 아이템 클릭 이벤트 */
    private fun click_Item() {
        adapter.itemClick = object : InsuranceAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val selectedItem = adapter.getItem(position)

                navigateTo(
                    ProductDetailActivity::class.java,
                    "source" to "WishListView",
                    "name" to selectedItem.name
                )
            }
        }
    }

    /* 뒤로가기 버튼 클릭 이벤트 */
    private fun click_BackButton() {
        // 뒤로가기 버튼 클릭
        btn_back.setOnClickListener {
            if (source == "MainViewActivity") {
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            } else
                navigateTo(MainViewActivity::class.java, "source" to "WishListView", reverseAnimation = true)
        }

        // 기기 내장 뒤로가기 버튼 클릭
        onBackPressedDispatcher.addCallback(this) {
            if (source == "MainViewActivity") {
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            } else
                navigateTo(MainViewActivity::class.java, "source" to "WishListView", reverseAnimation = true)
        }
    }
}