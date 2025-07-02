package com.example.llm_project_android

import android.os.Bundle
import android.view.Gravity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.databinding.MainViewBinding
import android.view.View
import android.widget.ImageButton
import androidx.annotation.GravityInt
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.navigation.NavigationView

class MainViewActivity : AppCompatActivity() {
    private lateinit var binding: MainViewBinding
    private val sliderHandler = android.os.Handler(android.os.Looper.getMainLooper())
    private lateinit var sliderRunnable: Runnable

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btn_search: ImageButton
    private lateinit var btn_menu_black: ImageButton
    private lateinit var btn_menu_white: ImageButton
    private lateinit var menuView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 바인딩 초기화
        binding = MainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        btn_search = findViewById<ImageButton>(R.id.search_icon)
        btn_menu_black = findViewById<ImageButton>(R.id.menu_icon_black)
        menuView = findViewById<NavigationView>(R.id.navigationView)

        val headerView = menuView.getHeaderView(0)
        btn_menu_white = headerView.findViewById<ImageButton>(R.id.menu_icon_white)

        // 배너 아이템 리스트
        val bannerList = listOf(
            R.drawable.image_birth_icon,
            R.drawable.sample_image,
            R.drawable.image_name_icon
        )

        // 배너 슬라이딩
        setupViewPager(bannerList)
        startAutoScroll(bannerList)

        // 메뉴 클릭 이벤트
        menu_control()
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

    // 메뉴 기능
    private fun menu_control() {
        // 메뉴 열기
        btn_menu_black.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.END))
                drawerLayout.openDrawer(GravityCompat.END)
        }
        
        // 메뉴 닫기
        btn_menu_white.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END))
                drawerLayout.closeDrawer(GravityCompat.END)
        }
    }

}