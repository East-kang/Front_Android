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
import android.widget.Button
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
    private lateinit var menuView: NavigationView
    private lateinit var menus: List<ImageButton>
    private lateinit var categories: List<Button>


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

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)    // 루트 Drawer 레이아웃 (네비게이션 메뉴용)
        btn_search = findViewById<ImageButton>(R.id.search_icon)        // 검색 버튼
        menuView = findViewById<NavigationView>(R.id.navigationView)    // 사이드 메뉴

        menus = listOf(                              // 메뉴 버튼 리스트 (0: 열기 버튼 / 1: 닫힘 버튼
            findViewById(R.id.menu_icon_black), // 메뉴 열기 버튼 (menus[0])
            findViewById(R.id.menu_icon_white)  // 메뉴 닫기 버튼 (menus[1])
        )
        categories= listOf(                          // 카테고리 버튼 리스트
            findViewById(R.id.category0),       // categories[0]
            findViewById(R.id.category1),       // categories[1]
            findViewById(R.id.category2),       // categories[2]
            findViewById(R.id.category3),       // categories[3]
            findViewById(R.id.category4),       // categories[4]
            findViewById(R.id.category5)        // categories[5]
        )
        val bannerList = listOf(                    // 배너 아이템 리스트
            R.drawable.image_birth_icon,            // 배너 아이템 0 (bannerList[0])
            R.drawable.sample_image,                // 배너 아이템 1 (bannerList[1])
            R.drawable.image_name_icon              // 배너 아이템 2 (bannerList[2])
        )

        // 배너 슬라이딩 기능
        setupViewPager(bannerList)
        startAutoScroll(bannerList)

        // 사이드 메뉴 클릭 이벤트
        menu_control()

        // 상품 검색
        search_Insurance()

        // 프로필 뷰 이동
        goTo_Profile_View()

        // 찜목록 뷰 이동
        goTo_WishList_View()

        // 가입한 보험 뷰 이동
        goTo_JoinedInsurance_View()

        // 채팅 뷰 이동
        goTo_Chat_View()

        // 기기 내장 뒤로가기 버튼 클릭 이벤트
        registerExitDialogOnBackPressed()
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
        menus[0].setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.END))
                drawerLayout.openDrawer(GravityCompat.END)
        }
        
        // 메뉴 닫기
        menus[1].setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END))
                drawerLayout.closeDrawer(GravityCompat.END)
        }
    }

    // 검색 기능
    fun search_Insurance() {}

    // 프로필 페이지 전환
    fun goTo_Profile_View() {}

    // 찜목록 페이지 전환
    fun goTo_WishList_View() {}

    // 가입한 보험 페이지 전환
    fun goTo_JoinedInsurance_View() {}

    // 채팅 페이지 전환
    fun goTo_Chat_View() {}
}