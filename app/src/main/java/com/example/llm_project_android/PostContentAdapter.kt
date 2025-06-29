package com.example.llm_project_android

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostContentAdapter (private var postList: List<Post>)
    : RecyclerView.Adapter<PostContentAdapter.PostViewHolder>(), Filterable {

    private var filteredList: List<Post> = emptyList()      // 필터링 결과 리스트 (초기에는 전체 리스트와 동일)
    private var itemClickListener: ((Post) -> Unit)? = null // 외부에서 클릭 이벤트를 설정할 수 있도록 콜백 정의

    fun setOnItemClickListener(listener: (Post) -> Unit) {
        itemClickListener = listener
    }

    // ViewHolder 정의: 아이템 레이아웃의 View들을 바인딩
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.text_title)
    }

    // ViewHolder 생성 (XML 레이아웃 inflate)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.insurance_item_post, parent, false)
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

    // 아이템 개수 반환
    override fun getItemCount(): Int = filteredList.size

    // 필터링 로직 구현
    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result = FilterResults()
                val queryStr = query?.toString() ?: ""  // 검색어 문자열로 변환

                // 검색어가 비어있으면 전체 리스트 반환 (공백일 경우)
                val filtered = if (queryStr.isBlank()){
                    emptyList<Post>()
                } else {    // 검색어가 포함된 상품 필터링
                    postList.filter { post ->
                        post.title.contains(queryStr, ignoreCase = true)
                    }
                }

                result.values = filtered
                return result
            }

            // 필터링 결과를 어댑터에 반영하고 새로고침
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<Post>
                notifyDataSetChanged()
            }
        }
    }

}