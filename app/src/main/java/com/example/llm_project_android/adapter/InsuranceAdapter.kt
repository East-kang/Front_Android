package com.example.llm_project_android.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.graphics.alpha
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.databinding.ZDesignShapeInsuranceBinding
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import com.example.llm_project_android.functions.RecentViewedManager
import com.example.llm_project_android.data.model.Insurance

// MainViewActivity.kt  : 최근 조회 목록
// CategoryView.kt      : 상품 목록
class InsuranceAdapter(productList: ArrayList<Insurance>) : RecyclerView.Adapter<InsuranceAdapter.Holder>() {

    private val originalList: List<Insurance> = productList.toList()                    // 원본 데이터 보관용 상품 리스트
    private val insuranceList: MutableList<Insurance> = productList.toMutableList()     // 화면에 표시될 상품 리스트
    private val list1: MutableList<Insurance> = productList.toMutableList()             // 임시 저장 리스트 1
    private val list2: MutableList<Insurance> = mutableListOf<Insurance>()              // 임시 저장 리스트 2
    var itemClick : ItemClick? = null                   // 클릭 이벤트 변수
    private var aiRecommendKey: String = "AI 추천"       // AI 추천 여부
    private var enrolledIds = mutableSetOf<String>()    // 가입 상품 리스트
    private var showingWork: Boolean = false            // 보여줄 리스트 (false: list1 / true: list2)
    private var deleteMode: Boolean = false             // 삭제 모드

    fun applyFilters(
        filter1: String?,           // 카테고리
        filter2: List<String>?,     // 회사
        filter3: String?            // 정렬
    ) {
        var filtered = originalList // 원본 리스트로 초기화

        // 1번 필터링
        if (!filter1.isNullOrEmpty())
            filtered = filtered.filter { it.category == filter1 }

        // 2번 필터링
        if (!filter2.isNullOrEmpty())
            filtered = filtered.filter { filter2.contains(it.company_name) }

        // 3번 필터링
        filtered = when (filter3) {
            "추천순" -> filtered.sortedWith (
                compareByDescending<Insurance> { it.recommendation }    // 1순위: 추천 여부 true 먼저
                    .thenBy { it.name }                                 // 2순위: 이름 오름차순 정렬
            )
            "이름순" -> filtered.sortedBy { it.name }
            else -> filtered
        }

        updateList(filtered)    // 리스트 업데이트
    }

    // 상품 리스트 업데이트
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Insurance>) {
        insuranceList.clear()
        insuranceList.addAll(newList)
        notifyDataSetChanged()
    }

    // 'AI 추천' 여부 받아오기
    fun setAIRecommendKey(key: String) {
        aiRecommendKey = key
        notifyDataSetChanged()  // 조건이 바뀌었으니 다시 그리게 함
    }

    // 가입 여부 받아오기
    fun setEnrolls(ids: List<String>) {
        enrolledIds.clear()
        enrolledIds.addAll(ids)
        notifyDataSetChanged()
    }



    // viewHolder 객체 생성, 반환된 뷰 홀더 객체는 자동으로 onBindViewHolder() 함수의 매개변수로 전달
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val binding = ZDesignShapeInsuranceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    // 매개변수로 전달된 뷰 홀더 객체의 뷰에 데이터를 출력하거나 필요한 이벤트 등록
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = insuranceList[position]
        
        holder.C_icon.setImageResource(item.company_icon)   // 기업 로고
        holder.C_name.text = item.company_name              // 기업명
        holder.P_name.text = item.name                      // 상품명
        holder.P_description.text = item.description        // 상품 설명
        holder.P_category.text = item.category              // 상품 카테고리

        val payment = item.payment.toString()               // 상품 월납입금
        val styledText = SpannableStringBuilder()

        styledText.append("월 ") // 납입금 앞 문구
        val start = styledText.length
        styledText.append(payment)
        val end = styledText.length

        styledText.setSpan(     // 납입금 textColor 변경
            ForegroundColorSpan("#5480F0".toColorInt()),
            start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        styledText.setSpan(     // 납입금 textStyle 변경
            StyleSpan(Typeface.BOLD),
            start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        styledText.setSpan(     // 납입금 textSize 변경
            RelativeSizeSpan(1.3f),
            start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        styledText.append("만원")  // 뒤 문구

        holder.P_payment.text = styledText  // 납입금 문구 저장

        // 'AI 추천' 문구 표시
        holder.AI_recommendation.visibility = if (item.recommendation) View.VISIBLE else View.GONE

        // 가입 여부 태그 표시
        holder.enrolled.visibility = if (enrolledIds.contains(item.name)) View.VISIBLE else View.GONE
        
        // 아이템 투명화 설정
        if (deleteMode) {
            holder.item.apply {
                alpha = 0.7f                // 아이템 투명화 o
                isEnabled = false           // 클릭 불가능
            }
            holder.deleteButton.apply {
                visibility = View.VISIBLE   // 시각화 o
                alpha = 1.0f                // 투명화 x
                isEnabled = true            // 클릭 가능
                bringToFront()              // 가장 앞으로 배치
            }
        } else {
            holder.item.apply {
                alpha = 1.0f                // 아이템 투명화 x
                isEnabled = true            // 클릭 가능
            }
            holder.deleteButton.apply {
                visibility = View.GONE      // 시각화 x
                alpha = 1.0f                // 투명화 x
                isEnabled = false           // 클릭 불가능
            }
        }

        holder.itemView.setOnClickListener {
            // 평소 클릭 동작만
            if (!deleteMode) {
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) itemClick?.onClick(it, pos)
            }
        }

        holder.deleteButton.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION)
                removeAtWorking(pos)
        }
    }

    // RecyclerView에 몇 가지의 아이템이 떠야되는지 알려주는 메서드, 반환한 숫자만큼 onBindViewHoler() 함수가 호출되어 항목 만듦.
    override fun getItemCount(): Int {
        return insuranceList.size
    }

    // ViewHolder 정의
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    inner class Holder(val binding: ZDesignShapeInsuranceBinding) : RecyclerView.ViewHolder(binding.root) {
        // 데이터 바인딩
        val C_icon = binding.companyIcon                    // 기업 아이콘
        val C_name = binding.companyName                    // 기업명
        val P_name = binding.insuranceName                  // 상품명
        val P_description = binding.insuranceDescription    // 상품 설명
        val P_payment = binding.insurancePayment            // 월 납입금
        val P_category = binding.category                   // 상품 카테고리
        var AI_recommendation = binding.recommendation      // AI 추천 여부
        var enrolled = binding.enroll                       // 상품 가입 여부
        var item = binding.insuranceDesign                  // 전체 아이템 뷰
        var deleteButton = binding.deleteButton             // 삭제 버튼

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClick?.onClick(it, position)

                    // 최근 본 상품 저장
                    val item = insuranceList[position]
                    RecentViewedManager.addItem(item)
                }
            }
        }
    }

    // 아이템 클릭 이벤트
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    // 선택한 아이템 반환
    fun getItem(position: Int): Insurance {
        return insuranceList[position]
    }


    // 현재 표시 중인 리스트 헬퍼
    private fun cur() = if (showingWork) list2 else list1

    // 화면에 보일 리스트를 동기화하여 즉시 반영
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshDisplay() {
        insuranceList.clear()
        insuranceList.addAll(if (showingWork) list2 else list1)
        notifyDataSetChanged()
    }

    // 아이템 삭제 모드 진입: list2에 원래 리스트 복사하고 list2 보여주기
    fun enterDeleteMode(original: List<Insurance>) {
        list1.clear();  list2.clear();
        list1.addAll(original)
        list2.addAll(list1)

        showingWork = true
        deleteMode = true
        refreshDisplay()
    }

    // 삭제 취소: list1 다시 보여 주기 (list2 초기화)
    fun cancelDeleteMode() {
        list2.clear()
        showingWork = false
        deleteMode = false
        refreshDisplay()
    }

    // 삭제 완료: list2를 list1으로 덮어 쓰고 list1을 보여줌
    fun confirmDeleteMode(
        onConfirmed: ((removed: List<Insurance>, kept: List<Insurance>) -> Unit)? = null
    ) {
        val removed = list1.filter { it !in list2 } // 이번에 삭제된 아이템들 (DB에서 지워야 할 것)
        val kept = list2.toList()                   // 삭제 후 남은 아이템들 (DB에 유지할 것)

        list1.clear()
        list1.addAll(list2)

        showingWork = false
        deleteMode = false
        refreshDisplay()

        onConfirmed?.invoke(removed, kept)  // DB 동기화에 사용
    }

    // 작업본 (list2)에서 삭제 (시각적으로 즉시 제거)
    fun removeAtWorking(position: Int) {
        if (!deleteMode || !showingWork) return
        if (position in 0 until list2.size) {
            list2.removeAt(position)
            insuranceList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, insuranceList.size - position)
        }
    }

    // 외부에서 확정 본의 교체/필터 결과 반영할 때
    fun replaceCommitted(newData: List<Insurance>) {
        list1.clear()
        list1.addAll(newData)
        if (!showingWork) refreshDisplay()
    }

    // 리스트 비교 (true: 변경o / false: 변경x)
    fun compare_List(): Boolean {
        return !(list1.toSet() == list2.toSet())
    }
}