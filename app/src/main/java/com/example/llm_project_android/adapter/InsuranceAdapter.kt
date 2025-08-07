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
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.databinding.ZDesignShapeInsuranceBinding
import androidx.core.graphics.toColorInt
import com.example.llm_project_android.functions.RecentViewedManager
import com.example.llm_project_android.data.model.Insurance

// MainViewActivity.kt  : 최근 조회 목록
// CategoryView.kt      : 상품 목록
class InsuranceAdapter(productList: ArrayList<Insurance>) : RecyclerView.Adapter<InsuranceAdapter.Holder>() {

    private val originalList: List<Insurance> = productList.toList()                    // 원본 데이터 보관용 상품 리스트
    private val insuranceList: MutableList<Insurance> = productList.toMutableList()     // 화면에 표시될 상품 리스트
    var itemClick : ItemClick? = null                 // 클릭 이벤트 변수
    private var aiRecommendKey: String = "AI 추천"    // AI 추천 여부

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
    }

    // RecyclerView에 몇 가지의 아이템이 떠야되는지 알려주는 메서드, 반환한 숫자만큼 onBindViewHoler() 함수가 호출되어 항목 만듦.
    override fun getItemCount(): Int {
        return insuranceList.size
    }

    // ViewHolder 정의
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    inner class Holder(val binding: ZDesignShapeInsuranceBinding) : RecyclerView.ViewHolder(binding.root) {
        // 데이터 바인딩
        val C_icon = binding.companyIcon
        val C_name = binding.companyName
        val P_name = binding.insuranceName
        val P_description = binding.insuranceDescription
        val P_payment = binding.insurancePayment
        val P_category = binding.category
        var AI_recommendation = binding.recommendation

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
}