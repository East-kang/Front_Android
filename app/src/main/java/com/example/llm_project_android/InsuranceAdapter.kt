package com.example.llm_project_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.databinding.DesignShapeInsuranceBinding


class InsuranceAdapter(val insuranceList: ArrayList<Insurance>) : RecyclerView.Adapter<InsuranceAdapter.Holder>() {
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
        holder.C_icon.setImageResource(insuranceList[position].company_icon)
        holder.C_name.text = insuranceList[position].company_name
        holder.P_name.text = insuranceList[position].name
        holder.P_description.text = insuranceList[position].description
        holder.P_payment.text = insuranceList[position].payment.toString()
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
    }
}