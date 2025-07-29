package com.example.llm_project_android.page.c_product

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.FrameStats
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.RecentViewedManager
import com.example.llm_project_android.adapter.InsuranceAdapter
import com.example.llm_project_android.adapter.ViewPageAdapter
import com.example.llm_project_android.databinding.CPageMainViewBinding
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.registerExitDialogOnBackPressed
import com.example.llm_project_android.page.d_menu.ProfileView
import com.example.llm_project_android.page.e_chat.ChatView
import com.google.android.material.navigation.NavigationView
import kotlin.math.abs

class MainViewActivity : AppCompatActivity() {
    private lateinit var binding: CPageMainViewBinding
    private val sliderHandler = Handler(Looper.getMainLooper())
    private lateinit var sliderRunnable: Runnable

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btn_search: ImageButton
    private lateinit var menuView: NavigationView
    private lateinit var menus: List<ImageButton>
    private lateinit var categories: List<Button>
    private lateinit var recyclerView: RecyclerView

    private lateinit var btn_chat: FrameLayout
    private var source: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        RecentViewedManager.init(this)  // RecentViewedManager 초기화

        // 바인딩 초기화
        binding = CPageMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        source = getPassedExtras("source", String::class.java)["source"] as? String

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)    // 루트 Drawer 레이아웃 (네비게이션 메뉴용)
        btn_search = findViewById<ImageButton>(R.id.search_icon)        // 검색 버튼
        menuView = findViewById<NavigationView>(R.id.navigationView)    // 사이드 메뉴
        recyclerView = findViewById<RecyclerView>(R.id.item_group)      // 최근 조회 상품 목록

        val headerView = menuView.getHeaderView(0)
        val btn_menu_white = headerView.findViewById<ImageButton>(R.id.menu_icon_white)


        menus = listOf(                         // 메뉴 버튼 리스트 (0: 열기 버튼 / 1: 닫힘 버튼
            findViewById(R.id.menu_icon_black), // 메뉴 열기 버튼 (menus[0])
            btn_menu_white                      // 메뉴 닫기 버튼 (menus[1])
        )
        categories= listOf(                     // 상품 카테고리 버튼 리스트
            findViewById(R.id.category0),       // categories[0] (암)
            findViewById(R.id.category1),       // categories[1] (건강)
            findViewById(R.id.category2),       // categories[2] (사망)
            findViewById(R.id.category3),       // categories[3] (저축/연금)
            findViewById(R.id.category4),       // categories[4] (유아)
            findViewById(R.id.category5)        // categories[5] (기타)
        )
        val bannerList = listOf(                    // 배너 아이템 리스트
            R.drawable.image_birth_icon,            // 배너 아이템 0 (bannerList[0])
            R.drawable.image_sample,                // 배너 아이템 1 (bannerList[1])
            R.drawable.image_name_icon              // 배너 아이템 2 (bannerList[2])
        )

        btn_chat = findViewById(R.id.chatButton)

        // 배너 슬라이딩 기능
        setupViewPager(bannerList)
        startAutoScroll(bannerList)

        // 사이드 메뉴 클릭 이벤트
        menu_control()

        // 상품 검색
        search_Insurance()

        // 카테고리 클릭 이벤트
        clickCategory()

        // 최근 조회 상품 목록 보여주기
        recentItems()

        // 메뉴 아이템 클릭 이벤트
        click_Menu_item()

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
            val r = 1 - abs(position)
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

    override fun onResume() {
        super.onResume()
        recentItems() // 돌아올 때 항상 복구
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    // 카테고리 클릭 이벤트
    private fun clickCategory() {
        for (i in 0 until categories.size) {
            categories[i].setOnClickListener {
                navigateTo(
                    CategoryView::class.java,
                    "category" to categories[i].text.toString().trim()
                )
            }
        }
    }

    // 최근 조회 목록 보여주기 함수
    private fun recentItems() {
        val recentItems = RecentViewedManager.getRecentItems()
        val recentAdapter = InsuranceAdapter(ArrayList(recentItems))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recentAdapter
    }

    // 메뉴 기능
    private fun menu_control() {

        if (source == null)                                 // 이전 화면이 메뉴를 통한 화면이 아닐 경우
            drawerLayout.closeDrawer(GravityCompat.END)
        else                                                // 이전 화면이 메뉴를 통한 화면일 경우
            drawerLayout.openDrawer(GravityCompat.END)

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

    // 메뉴 아이템 클릭 이벤트
    fun click_Menu_item() {
        menuView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {           // '내 프로필' 버튼 클릭 이벤트
                    navigateTo(ProfileView::class.java, "source" to "MainViewActivity")
                    true    // 이벤트 종료
                }
                R.id.wishList -> {          // '찜 목록' 버튼 클릭 이벤트
                    
                    true
                }
                R.id.joined_insurance -> {  // '가입한 내 보험' 버튼 클릭 이벤트
                    
                    true
                }
                else -> false
            }
        }
    }

    // 채팅 페이지 전환
    fun goTo_Chat_View() {
        btn_chat.setOnClickListener{
            navigateTo(
                ChatView::class.java,
                "source" to "MainView"
            )
        }
    }
}