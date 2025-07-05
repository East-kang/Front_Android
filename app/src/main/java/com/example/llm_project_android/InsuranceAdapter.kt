package com.example.llm_project_android

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.databinding.DesignShapeInsuranceBinding
import androidx.core.graphics.toColorInt


class InsuranceAdapter(val insuranceList: ArrayList<Insurance>) : RecyclerView.Adapter<InsuranceAdapter.Holder>() {
    // 아이템 클릭 이벤트
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick : ItemClick? = null // 클릭 이벤트 추가

    // viewHolder 객체 생성, 반환된 뷰 홀더 객체는 자동으로 onBindViewHolder() 함수의 매개변수로 전달
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InsuranceAdapter.Holder {
        val binding = DesignShapeInsuranceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    // 매개변수로 전달된 뷰 홀더 객체의 뷰에 데이터를 출력하거나 필요한 이벤트 등록
    override fun onBindViewHolder(holder: InsuranceAdapter.Holder, position: Int) {
        val item = insuranceList[position]
        
        holder.C_icon.setImageResource(item.company_icon)   // 기업 로고
        holder.C_name.text = item.company_name              // 기업명
        holder.P_name.text = item.name                      // 상품명
        holder.P_description.text = item.description        // 상품 설명
        holder.P_category.text = item.category              // 상품 카테고리

        val payment = item.payment.toString()               // 상품 월납입금
        val styledText = SpannableStringBuilder()

        styledText.append("월 ")
        val start = styledText.length
        styledText.append(payment)
        val end = styledText.length

        styledText.setSpan(
            ForegroundColorSpan("#5480F0".toColorInt()),
            start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        styledText.setSpan(
            StyleSpan(Typeface.BOLD),
            start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        styledText.setSpan(
            RelativeSizeSpan(1.3f),
            start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        styledText.append("만원")  // 뒤 문구

        holder.P_payment.text = styledText
    }

    // RecyclerView에 몇 가지의 아이템이 떠야되는지 알려주는 메서드, 반환한 숫자만큼 onBindViewHoler() 함수가 호출되어 항목 만듦.
    override fun getItemCount(): Int {
        return insuranceList.size
    }

    inner class Holder(val binding: DesignShapeInsuranceBinding) : RecyclerView.ViewHolder(binding.root) {
        val C_icon = binding.companyIcon
        val C_name = binding.companyName
        val P_name = binding.insuranceName
        val P_description = binding.insuranceDescription
        val P_payment = binding.insurancePayment
        val P_category = binding.category

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClick?.onClick(it, position)
                }
            }
        }
    }
}