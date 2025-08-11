package com.example.llm_project_android.page.d_menu

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.adapter.InsuranceAdapter
import com.example.llm_project_android.adapter.ProductContentAdapter
import com.example.llm_project_android.data.model.Insurance
import com.example.llm_project_android.data.model.Product
import com.example.llm_project_android.data.sample.Products_Insurance
import com.example.llm_project_android.db.user.MyDAO
import com.example.llm_project_android.db.user.MyDatabase
import com.example.llm_project_android.db.user.User
import com.example.llm_project_android.functions.handleTouchOutsideEditText
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.showConfirmDialog
import com.example.llm_project_android.page.c_product.MainViewActivity
import com.example.llm_project_android.page.c_product.ProductDetailActivity
import kotlinx.coroutines.launch

class EnrolledViewActivity : AppCompatActivity() {

    private lateinit var btn_back: ImageButton              // 뒤로 가기 버튼
    private lateinit var btn_cancel: Button                 // 취소 버튼
    private lateinit var btn_edit: Button                   // 편집,완료 버튼
    private lateinit var guideText: TextView                // "아이템 없음" 텍스트 뷰
    private lateinit var search_box: SearchView             // 검색란
    private lateinit var searchList: RecyclerView           // 검색 결과 리스트
    private lateinit var linearLayout: LinearLayout         // 아이템 표시 필드
    private lateinit var recyclerView: RecyclerView         // 아이템 추가 뷰

    private lateinit var dao: MyDAO
    private var user: User ?= null
    private lateinit var adapter: InsuranceAdapter
    private var enrolledList: Set<String> = emptySet()          // 가입 상품 문자열 리스트
    private var allProduct: List<Insurance> = emptyList()       // 전체 보험 상품 리스트
    private var enrolledProducts: List<Insurance> = emptyList() // 가입 여부 필터링 된 상품 리스트
    private var itemAnimatorBackup: RecyclerView.ItemAnimator? = null   // 투명화 유지를 위한 변수

    private lateinit var searchAdapter: ProductContentAdapter

    private var edit_state: Boolean = false                 // 편집 여부 변수 (true: 편집 모드, false: 읽기 모드)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.d_page_enrolled_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_back = findViewById(R.id.backButton)
        btn_cancel = findViewById(R.id.cancelButton)
        btn_edit = findViewById(R.id.editButton)
        guideText = findViewById(R.id.guide_text)
        search_box = findViewById(R.id.search_box)
        searchList = findViewById(R.id.search_list)
        linearLayout = findViewById(R.id.linearLayout)
        recyclerView = findViewById(R.id.item_group)

        dao = MyDatabase.getDatabase(this@EnrolledViewActivity).getMyDao()
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InsuranceAdapter(Products_Insurance.productList)
        recyclerView.adapter = adapter

        set_View_Mode(edit_state)         // 초기 화면 구성 (읽기 모드)
        enrolled_Items()    // 가입 상품 띄우기
        click_Items()       // 아이템 클릭 이벤트
        click_Buttons()     // 버튼 클릭 이벤트
        search_Insurance()  // 상품 검색 이벤트
    }

    /* 가입 상품 목록 보여 주기 함수 */
    private fun enrolled_Items() {
        lifecycleScope.launch {
            user = dao.getLoggedInUser()

            user?.let {
                enrolledList = it.subscriptions.toSet()
                allProduct = Products_Insurance.productList

                // 가입 상품 필터링
                enrolledProducts = allProduct
                    .filter { product -> enrolledList.contains(product.name) }
                    .sortedBy { it.name }

                adapter.setEnrolls(it.subscriptions)
                adapter.updateList(enrolledProducts)
                update_View(enrolledProducts.isEmpty())
            }
        }
    }

    /* 가입 상품 클릭 이벤트 */
    private fun click_Items() {
        adapter.itemClick = object : InsuranceAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val selectedItem = adapter.getItem(position)

                navigateTo(
                    ProductDetailActivity::class.java,
                    "source" to "EnrolledView",
                    "company_icon" to selectedItem.company_icon,
                    "company_name" to selectedItem.company_name,
                    "category" to selectedItem.category,
                    "insurance_name" to selectedItem.name,
                    "recommendation" to selectedItem.recommendation
                )
            }
        }
    }

    /* 뷰 모드 설정 */
    private fun set_View_Mode(isEdit: Boolean) {
        /* 편집 모드 뷰 */
        if (isEdit) {
            btn_back.visibility = View.GONE
            btn_cancel.visibility = View.VISIBLE
            search_box.visibility = View.VISIBLE
            searchList.visibility = View.VISIBLE
            search_box.setQuery("", false)
            btn_edit.text = "완료"
        }

        /* 읽기 모드 뷰 */
        else {
            btn_back.visibility = View.VISIBLE
            btn_cancel.visibility = View.GONE
            btn_edit.text = "편집"
            search_box.visibility = View.GONE
            searchList.visibility = View.GONE
        }
    }

    /* 편집 및 뷰 구성 모드 변경 */
    private fun update_state_view() {
        edit_state = !edit_state    // 편집 여부 변경
        set_View_Mode(edit_state)   // 뷰 구성 모드 전환
    }
    
    /* 편집 버튼 클릭 이벤트 */
    private fun click_Buttons() {
        /* 편집, 완료 버튼 클릭 이벤트 */
        btn_edit.setOnClickListener {
            if (!edit_state) {   // 읽기 모드 -> 편집 모드
                update_state_view()                                     // 편집 및 뷰 구성 모드 변경
                adapter.enterDeleteMode(enrolledProducts)               // 아이템 삭제 모드 진입
                itemAnimatorBackup = recyclerView.itemAnimator
                recyclerView.itemAnimator = null                        // alpha 값 건드리지 않기
                searchAdapter.setExcludedTitles(adapter.currentNames()) // 편집 시작: 현재 화면(작업본) 기준으로 제외
                adapter.onWorkingListChanged = { names ->               // 추가/삭제될 때마다 검색 제외 즉시 갱신
                    searchAdapter.setExcludedTitles(names) }
            } else {            // 편집 모드 -> 읽기 모드
                if (adapter.compare_List()) {
                    showConfirmDialog(this, "확인", "변경 사항을 저장하시겠습니까?", { result ->
                        if (result) {
                            update_state_view()         // 편집 및 뷰 구성 모드 변경

                            adapter.confirmDeleteMode{ removed, kept -> // 아이템 삭제 완료
                                val keptList = kept.map { it.name }.toSet()

                                lifecycleScope.launch {
                                    val tmp = dao.getLoggedInUser() ?: return@launch
                                    val updated = tmp.copy(subscriptions = keptList.toList())

                                    // 1. 내부 DB에서 삭제
                                    dao.updateUser(updated)

                                    // 2. 메모리,뷰 업데이트
                                    enrolledList = keptList
                                    enrolledProducts = kept
                                    adapter.setEnrolls(enrolledList.toList())   // 가입 여부 태그 업데이트
                                    adapter.replaceCommitted(enrolledProducts)  // 확정본 리스트 덮어 씌우기
                                    update_View(enrolledProducts.isEmpty())

                                    searchAdapter.setExcludedTitles(enrolledList)   // 편집 종료: 확정본으로 제외 기준 복귀
                                    adapter.onWorkingListChanged = null

                                    recyclerView.itemAnimator = itemAnimatorBackup  // 복구
                                    itemAnimatorBackup = null
                                }
                            }
                        }
                    })
                }
                else {
                    update_state_view()         // 편집 및 뷰 구성 모드 변경
                    adapter.cancelDeleteMode()  // 편집 모드 취소
                }
            }

            // 아이템 투명화 설정
            lifecycleScope.launch {
                adapter.setEnrolls(enrolledList.toList())  // edit_state = true:설정 , false: 해제
            }
        }

        /* 취소 버튼 클릭 이벤트 */
        btn_cancel.setOnClickListener { cancel_Edit() }

        /* 뒤로 가기 버튼 클릭 이벤트 */
        btn_back.setOnClickListener { function_back() }

        /* 기기 내장 뒤로 가기 버튼 클릭 */
        onBackPressedDispatcher.addCallback(this) { function_back() }
    }

    /* 편집 취소 이벤트 */
    private fun cancel_Edit() {
        if (adapter.compare_List()) {   // 내용 변경 됨
            showConfirmDialog(this, "취소", "변경을 취소하시겠습니까?") { result ->
                if (result) {   // 변경 시키지 않기
                    update_state_view()         // 편집 및 뷰 구성 모드 변경
                    adapter.cancelDeleteMode()  // 아이템 삭제 취소
                    recyclerView.itemAnimator = itemAnimatorBackup
                    itemAnimatorBackup = null

                    searchAdapter.setExcludedTitles(enrolledList)   // 편집 취소: 검색 확정본으로 복귀
                    adapter.onWorkingListChanged = null
                }
            }
        } else {    // 내용 변경 안됨
            update_state_view()         // 편집 및 뷰 구성 모드 변경
            adapter.cancelDeleteMode()  // 아이템 삭제 취소
            recyclerView.itemAnimator = itemAnimatorBackup
            itemAnimatorBackup = null

            searchAdapter.setExcludedTitles(enrolledList)   // 변경 없음으로 종료해도 확정본으로 복귀
            adapter.onWorkingListChanged = null
        }
    }

    /* 뒤로가기 버튼 이벤트 */
    private fun function_back() {
        if (edit_state) cancel_Edit()
        else {
            finish()
            navigateTo(
                MainViewActivity::class.java,
                "source" to "SubscribedView",
                reverseAnimation = true
            )
        }
    }

    /* 가입 상품 목록 존재 여부에 따른 뷰 구성 */
    private fun update_View(isEmpty: Boolean) {
        // 최근 상품 조회 목록 존재에 따른 뷰 구성
        if (isEmpty) {
            guideText.visibility = View.VISIBLE
            linearLayout.visibility = View.GONE
        } else {
            guideText.visibility = View.GONE
            linearLayout.visibility = View.VISIBLE
        }
    }

    /* 상품 검색 이벤트 */
    private fun search_Insurance() {
        // 보험 데이터 -> Product 변환
        val insuranceProducts = Products_Insurance.productList.map { Product(it.name) }

        // 어뎁터 초기화
        searchAdapter = ProductContentAdapter(insuranceProducts)
        searchList.layoutManager = LinearLayoutManager(this)
        searchList.adapter = searchAdapter
        searchList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        
        searchAdapter.setExcludedTitles(enrolledList)   // 가입 목록 검색 제외
        
        // 검색어 입력 리스너
        search_box.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchAdapter.filter.filter(query)  // 검색 실행
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 입력 시 필터링
                searchAdapter.filter.filter(newText)
                return true
            }
        })

        // 초기 데이터 표시 (검색어 없을 때에는 전체 리스트 대신 emptyList)
        searchAdapter.filter.filter("")
        search_box.clearFocus() // 클릭 시 검색창 포커스 해제

        // 아이템 클릭 이벤트
        searchAdapter.setOnItemClickListener { product ->
            val selectedInsurance = Products_Insurance.productList.find() { it.name.trim() == product.title.trim() }

            if (selectedInsurance != null)
                adapter.addItem(selectedInsurance)      // 가입 리스트에 추가
            search_box.setQuery("", false)
        }
    }

    /* 키보드 숨기기 이벤트 (editText 이외의 영역을 눌렀을 경우, 스크롤 제외) */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        handleTouchOutsideEditText(this, ev)
        return super.dispatchTouchEvent(ev)
    }
}