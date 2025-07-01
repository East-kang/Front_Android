package com.example.llm_project_android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.databinding.MainViewBinding
import android.view.View
import android.widget.ImageButton
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer

class MainViewActivity : AppCompatActivity() {
    private lateinit var binding: MainViewBinding
    private val sliderHandler = android.os.Handler(android.os.Looper.getMainLooper())
    private lateinit var sliderRunnable: Runnable

    private lateinit var btn_search: ImageButton
    private lateinit var btn_menu: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 바인딩 초기화
        binding = MainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_search = findViewById<ImageButton>(R.id.search_icon)
        btn_menu = findViewById<ImageButton>(R.id.menu_icon)

        val bannerList = listOf(
            R.drawable.image_birth_icon,
            R.drawable.sample_image,
            R.drawable.image_name_icon
        )

        // 배너 슬라이딩
        setupViewPager(bannerList)
        startAutoScroll(bannerList)

    }

    // 배너 양 옆 이미지 노출 함수
    private fun setupViewPager(bannerList: List<Int>) {

        val adapter  = ViewPageAdapter(bannerList)
        binding.bannerViewPager.adapter = adapter

        // 배너 개수에 따라 시작 위치 계산
        val startIndex = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % bannerList.size)
        binding.bannerViewPager.setCurrentItem(startIndex, false)

        binding.bannerViewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
        }

        // ViewPager2 내부 RecyclerView 패딩 설정 (양 옆 이미지 보이게)
        val recyclerView = binding.bannerViewPager.getChildAt(0) as RecyclerView
        recyclerView.setPadding(60, 0, 60, 0)
        recyclerView.clipToPadding = false
        recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER    // 바운스 효과 제거

        // CompositePageTransformer 적용 (더 스무스한 슬라이딩)
        val transformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(30))   // 페이지 간 마진
            addTransformer { page: View, position: Float ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.15f     // 가운데는 크게, 양 옆은 작게
            page.alpha = 0.7f + r * 0.3f        // 자연스러운 페이드 효과
            }
        }
        binding.bannerViewPager.setPageTransformer(transformer)
    }

    // 배너 이미지 슬라이딩 함수
    private fun startAutoScroll(bannerList: List<Int>) {
        sliderRunnable = object : Runnable {
            override fun run() {
                val currentItem = binding.bannerViewPager.currentItem
                val nextItem = currentItem + 1
                binding.bannerViewPager.setCurrentItem(nextItem, true)

                sliderHandler.postDelayed(this, 3000) // 3초마다 슬라이드
            }
        }
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

}