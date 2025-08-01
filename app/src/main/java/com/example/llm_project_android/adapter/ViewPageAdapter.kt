package com.example.llm_project_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.databinding.ZDesignItemBannerBinding

// MainViewActivity.kt  : 배너 기능
class ViewPageAdapter(private val images : List<Int>) :
    RecyclerView.Adapter<ViewPageAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(private val binding : ZDesignItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(item : Int) {
            binding.ivBanner.setImageResource(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ZDesignItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val realPosition = position % images.size
        holder.bind(images[realPosition])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}