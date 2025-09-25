package com.example.llm_project_android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.data.model.Product

// SignUpActivity4.kt   : 검색 상품 필터링
// MainViewActivity.kt  : 상품 검색 기능
class ProductContentAdapter (private var productList: List<Product>)
    : RecyclerView.Adapter<ProductContentAdapter.PostViewHolder>(), Filterable {

    private var filteredList: List<Product> = emptyList()      // 필터링 결과 리스트 (초기에는 전체 리스트와 동일)
    private var itemClickListener: ((Product) -> Unit)? = null // 외부에서 클릭 이벤트를 설정할 수 있도록 콜백 정의
    private var emptyResultCallBack: (() -> Unit)? = null

    private var excludeTitles: Set<String> = emptySet() // 제외할 상품명 모음
    private var currentQuery: String = ""               // 현재 검색어 저장(제외 목록 변경 시 재필터링용)

    // 콜백 외부 설정 함수
    fun setOnItemClickListener(listener: (Product) -> Unit) {
        itemClickListener = listener
    }

    fun setOnEmptyResultCallback(callback: () -> Unit) {
        emptyResultCallBack = callback
    }

    // ViewHolder 정의: 아이템 레이아웃의 View들을 바인딩
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.text_title)
    }

    // ViewHolder 생성 (XML 레이아웃 inflate)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.z_design_insurance_item_search, parent, false)
        return PostViewHolder(view)
    }

    // 각 아이템에 데이터 바인딩
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = filteredList[position]
        holder.title.text = post.title

        // 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(post)
        }
    }

    fun updateList(newList: List<Product>) {
        productList = newList
        filteredList = newList
        notifyDataSetChanged()

    }

    // 아이템 개수 반환
    override fun getItemCount(): Int = filteredList.size

    // 필터링 로직
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result = FilterResults()
                val queryStr = query?.toString()?.trim() ?: ""  // 현재 검색어 저장

                currentQuery = queryStr

                val base = productList.filter { it.title !in excludeTitles }    // 가입 항목 제외

                // 공백이면 emptyList, 아니면 contains 필터
                val filtered = if (queryStr.isBlank()) emptyList()
                               else base.filter { it.title.contains(queryStr, ignoreCase = true) }

                return FilterResults().apply {
                    values = filtered
                    count = filtered.size
                }
            }

            // 필터링 결과를 어댑터에 반영하고 새로고침
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredList = results?.values as? List<Product> ?: emptyList()
                notifyDataSetChanged()

                // 검색 결과 없을 경우 콜백 실행
                if (filteredList.isEmpty()) emptyResultCallBack?.invoke()
            }
        }
    }

    // 제외 목록 주입 & 재필터
    fun setExcludedTitles(titles: Set<String>) {
        excludeTitles = titles
        filter.filter(currentQuery) // 현재 검색어로 다시 필터링
    }
}