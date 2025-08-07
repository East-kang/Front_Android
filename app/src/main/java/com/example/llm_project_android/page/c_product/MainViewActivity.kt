package com.example.llm_project_android.page.c_product

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.RecentViewedManager
import com.example.llm_project_android.adapter.InsuranceAdapter
import com.example.llm_project_android.adapter.ProductContentAdapter
import com.example.llm_project_android.adapter.ViewPageAdapter
import com.example.llm_project_android.data.model.Product
import com.example.llm_project_android.data.sample.Products_Insurance
import com.example.llm_project_android.databinding.CPageMainViewBinding
import com.example.llm_project_android.db.user.MyDatabase
import com.example.llm_project_android.db.wishList.WishedManager
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.handleTouchOutsideEditText
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.registerExitDialogOnBackPressed
import com.example.llm_project_android.functions.resetUserTable
import com.example.llm_project_android.functions.showConfirmDialog
import com.example.llm_project_android.page.a_intro.InitActivity
import com.example.llm_project_android.page.d_menu.ProfileView
import com.example.llm_project_android.page.d_menu.SubscribedViewActivity
import com.example.llm_project_android.page.d_menu.WishViewActivity
import com.example.llm_project_android.page.e_chat.ChatView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainViewActivity : AppCompatActivity() {
    private lateinit var binding: CPageMainViewBinding
    private val sliderHandler = Handler(Looper.getMainLooper())
    private lateinit var sliderRunnable: Runnable

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var topBar: ConstraintLayout
    private lateinit var btn_search: ImageButton
    private lateinit var menuView: NavigationView
    private lateinit var menus: List<ImageButton>

    private lateinit var btn_menu_white: ImageButton
    private lateinit var name_field: TextView
    private lateinit var logout_field: TextView

    private lateinit var search_area: ConstraintLayout
    private lateinit var btn_back: ImageButton
    private lateinit var search_box: SearchView
    private lateinit var search_list: RecyclerView

    private lateinit var scrollView: NestedScrollView
    private lateinit var categories: List<Button>
    private lateinit var recyclerView: RecyclerView

    private lateinit var btn_chat: FrameLayout
    private var source: String? = null

    lateinit var wishedManager: WishedManager               // 찜 목록 매니저
    private lateinit var recentAdapter: InsuranceAdapter    // 최근 조회 어뎁터

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
        topBar = findViewById<ConstraintLayout>(R.id.topBar)            // 상단 바 영역
        btn_search = findViewById<ImageButton>(R.id.search_icon)        // 검색 버튼
        menuView = findViewById<NavigationView>(R.id.navigationView)    // 사이드 메뉴
        search_area = findViewById<ConstraintLayout>(R.id.search_area)  // 검색 영역
        btn_back = findViewById<ImageButton>(R.id.backButton)           // 검색 취소 버튼
        search_box = findViewById<SearchView>(R.id.search_box)          // 검색창
        search_list = findViewById<RecyclerView>(R.id.search_list)      // 검색 목록
        scrollView = findViewById<NestedScrollView>(R.id.scrollView)    // 스크롤 뷰
        recyclerView = findViewById<RecyclerView>(R.id.item_group)      // 최근 조회 상품 목록
        btn_chat = findViewById(R.id.chatButton)

        val headerView = menuView.getHeaderView(0)
        btn_menu_white = headerView.findViewById<ImageButton>(R.id.menu_icon_white) // 흰색 메뉴 버튼
        name_field = headerView.findViewById<TextView>(R.id.nameView)   // "***님" 필드
        logout_field = headerView.findViewById<TextView>(R.id.logOut)   // 로그아웃 텍스트

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
        val bannerList = listOf(                // 배너 아이템 리스트
            R.drawable.image_birth_icon,        // 배너 아이템 0 (bannerList[0])
            R.drawable.image_sample,            // 배너 아이템 1 (bannerList[1])
            R.drawable.image_name_icon          // 배너 아이템 2 (bannerList[2])
        )

        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        wishedManager = WishedManager(this)                     // 찜 목록 메니저 초기화
        recentAdapter = InsuranceAdapter(ArrayList(arrayListOf()))     // 전역 adapter 초기화
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recentAdapter

        init()                      // 초기 레이아웃 구성

        setupViewPager(bannerList)
        startAutoScroll() // 배너 슬라이딩 기능

        menu_Control()              // 사이드 메뉴 클릭 이벤트
        search_Insurance()          // 상품 검색
        click_Category()            // 카테고리 클릭 이벤트
        click_Items()               // 최근 조회 상품 클릭 이벤트
        recent_Items()              // 최근 조회 상품 목록 보여주기
        click_Menu_item()           // 메뉴 아이템 클릭 이벤트
        goTo_Chat_View()            // 채팅 뷰 이동
        click_logout()              // 로그아웃 클릭 이벤트

        registerExitDialogOnBackPressed()   // 기기 내장 뒤로가기 버튼 클릭 이벤트
    }

    // 초기 구성
    private fun init() {
        topBar.visibility = View.VISIBLE
        scrollView.visibility = View.VISIBLE
        btn_chat.visibility = View.VISIBLE
        search_area.visibility = View.GONE

        lifecycleScope.launch {
            val dao = MyDatabase.getDatabase(this@MainViewActivity).getMyDao()
            val user = dao.getLoggedInUser()

            name_field.text = user?.name+"님"
        }
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
    private fun startAutoScroll() {
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
        recent_Items() // 돌아올 때 항상 복구
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    // 카테고리 클릭 이벤트
    private fun click_Category() {
        for (i in 0 until categories.size) {
            categories[i].setOnClickListener {
                navigateTo(CategoryView::class.java, "category" to categories[i].text.toString().trim())
            }
        }
    }

    // 최근 조회 상품 클릭 이벤트
    private fun click_Items() {
        recentAdapter.itemClick = object : InsuranceAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val selectedItem = recentAdapter.getItem(position)

                navigateTo(
                    ProductDetailActivity::class.java,
                    "source" to "MainViewActivity",
                    "company_icon" to selectedItem.company_icon,
                    "company_name" to selectedItem.company_name,
                    "category" to selectedItem.category,
                    "insurance_name" to selectedItem.name,
                    "recommendation" to selectedItem.recommendation
                )
            }
        }
    }

    // 최근 조회 목록 보여주기 함수
    private fun recent_Items() {
        val recentItems = RecentViewedManager.getRecentItems(3)
        recentAdapter.updateList(recentItems)       // 리스트 갱신
    }

    // 메뉴 기능
    private fun menu_Control() {
        if (source == null)                                 // 이전 화면이 메뉴를 통한 화면이 아닐 경우
            drawerLayout.closeDrawer(GravityCompat.END)
        else                                                // 이전 화면이 메뉴를 통한 화면일 경우
            drawerLayout.openDrawer(GravityCompat.END)

        menus[0].setOnClickListener {       // 메뉴 열기
            if (!drawerLayout.isDrawerOpen(GravityCompat.END))
                drawerLayout.openDrawer(GravityCompat.END)
        }

        menus[1].setOnClickListener {       // 메뉴 닫기
            if (drawerLayout.isDrawerOpen(GravityCompat.END))
                drawerLayout.closeDrawer(GravityCompat.END)
        }
    }

    // 검색 기능
    private fun search_Insurance() {
        // 보험 데이터 → Product 변환
        val insuranceProducts = Products_Insurance.productList.map { Product(it.name) }

        // 어뎁터 초기화
        val searchAdapter = ProductContentAdapter(insuranceProducts)
        search_list.layoutManager = LinearLayoutManager(this)
        search_list.adapter = searchAdapter
        search_list.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        click_searchButton()    // 검색 버튼 클릭
        click_backButton()      // 뒤로가기 버튼 클릭

        // 검색어 입력 리스너
        search_box.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchAdapter.filter.filter(query) // 검색 실행
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 입력 시 필터링
                searchAdapter.filter.filter(newText)
                return true
            }
        })

        // 초기 데이터 표시 (검색어 없을 때에는 전체 리스트 대신 emptyList 방지)
        searchAdapter.filter.filter("")
        search_box.clearFocus() // 클릭 시 검색창 포커스 해제

        // 아이템 클릭 이벤트
        searchAdapter.setOnItemClickListener { product ->
            val selectedInsurance = Products_Insurance.productList.find { it.name.trim() == product.title.trim() }

            if (selectedInsurance != null) {
                Log.d("","navigateTo")
                navigateTo(
                    ProductDetailActivity::class.java,
                    "source" to "MainViewActivity",
                    "company_icon" to selectedInsurance.company_icon,
                    "company_name" to selectedInsurance.company_name,
                    "category" to selectedInsurance.category,
                    "insurance_name" to selectedInsurance.name,
                    "recommendation" to selectedInsurance.recommendation
                )
            } else
                Log.d("", "null")
        }
    }
    
    // 검색 버튼 클릭 이벤트
    private fun click_searchButton() {
        btn_search.setOnClickListener {
            topBar.visibility = View.GONE
            scrollView.visibility = View.GONE
            btn_chat.visibility = View.GONE
            search_area.visibility = View.VISIBLE   // UI 반영
        }
    }

    // 뒤로가기 버튼 클릭 이벤트
    private fun click_backButton() {
        btn_back.setOnClickListener {
            init()  // UI 반영
            search_box.setQuery("", false)  // SearchView 내용 초기화
        }
    }

    // 메뉴 아이템 클릭 이벤트
    private fun click_Menu_item() {
        menuView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {           // '내 프로필' 버튼 클릭 이벤트
                    navigateTo(ProfileView::class.java, "source" to "MainViewActivity")
                    true    // 이벤트 종료
                }
                R.id.wishList -> {          // '찜 목록' 버튼 클릭 이벤트
                    navigateTo(WishViewActivity::class.java, "source" to "MainViewActivity")
                    true
                }
                R.id.joined_insurance -> {  // '가입한 내 보험' 버튼 클릭 이벤트
                    navigateTo(SubscribedViewActivity::class.java, "source" to "MainViewActivity")
                    true
                }
                else -> false
            }
        }
    }

    // 채팅 페이지 전환
    private fun goTo_Chat_View() {
        btn_chat.setOnClickListener{
            navigateTo(ChatView::class.java, "source" to "MainView")
        }
    }

    // 로그아웃 클릭 이벤트
    private fun click_logout() {
        logout_field.setOnClickListener {
            showConfirmDialog(this, "로그아웃", "정말 로그아웃 하시겠습니까?") { result ->
                if (result) {
                    lifecycleScope.launch {
                        resetUserTable(this@MainViewActivity)   // 사용자 정보 테이블 초기화
                        wishedManager.clearAllWishes()                 // 찜 목록 초기화
                        // 가입 상품 목록 초기화
                    }
                    navigateTo(InitActivity::class.java, reverseAnimation = true)
                }
            }

        }
    }

    // 키보드 숨기기 이벤트 (editText 이외의 영역을 눌렀을 경우, 스크롤 제외)
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        handleTouchOutsideEditText(this, ev)
        return super.dispatchTouchEvent(ev)
    }
}