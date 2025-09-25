package com.example.llm_project_android.adapter

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
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.data.model.Insurance
import com.example.llm_project_android.databinding.ZDesignShapeInsuranceBinding
import com.example.llm_project_android.functions.RecentViewedManager

class RecentInsuranceAdapter :
    RecyclerView.Adapter<RecentInsuranceAdapter.Holder>() {

    private val items = mutableListOf<Insurance>()          // 표시할 목록 (순서 유지)
    private val enrolledIds = mutableSetOf<String>()        // 가입한 상품명
    var itemClick: ItemClick? = null



    fun updateListKeepOrder(newList: List<Insurance>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun setEnrolls(ids: List<String>) {
        enrolledIds.clear()
        enrolledIds.addAll(ids)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Insurance = items[position]

    override fun getItemCount(): Int = items.size

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ZDesignShapeInsuranceBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]

        holder.C_icon.setImageResource(item.company_icon)
        holder.C_name.text = item.company_name
        holder.P_name.text = item.name
        holder.P_description.text = item.description
        holder.P_category.text = item.category

        // 결제 문구 스타일
        val payment = item.payment.toString()
        val styled = SpannableStringBuilder().apply {
            append("월 ")
            val s = length
            append(payment)
            val e = length
            setSpan(ForegroundColorSpan("#5480F0".toColorInt()), s, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), s, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(RelativeSizeSpan(1.3f), s, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            append("만원")
        }
        holder.P_payment.text = styled

        // 뱃지/버튼 상태
        holder.AI_recommendation.visibility = if (item.recommendation) View.VISIBLE else View.GONE
        holder.enrolled.visibility = if (enrolledIds.contains(item.name)) View.VISIBLE else View.GONE
        holder.deleteButton.visibility = View.GONE              // 최근목록엔 삭제 버튼 사용 안 함
        holder.item.alpha = 1f
        holder.item.isEnabled = true

        holder.itemView.setOnClickListener {
            // 클릭 시 최근목록 갱신(맨 앞으로) — 필요 없다면 이 줄 빼세요.
            RecentViewedManager.addItem(item)
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) itemClick?.onClick(it, pos)
        }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    inner class Holder(val binding: ZDesignShapeInsuranceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val C_icon = binding.companyIcon
        val C_name = binding.companyName
        val P_name = binding.insuranceName
        val P_description = binding.insuranceDescription
        val P_payment = binding.insurancePayment
        val P_category = binding.category
        val AI_recommendation = binding.recommendation
        val enrolled = binding.enroll
        val item = binding.insuranceDesign
        val deleteButton = binding.deleteButton
    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    fun clearItems() {
        items.clear()
    }
}
