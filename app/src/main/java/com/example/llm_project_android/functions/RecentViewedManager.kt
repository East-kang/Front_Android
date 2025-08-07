package com.example.llm_project_android.functions

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.llm_project_android.data.model.Insurance
import java.util.LinkedList

object RecentViewedManager{
    private const val PREF_NAME = "recent_pref"
    private const val KEY_RECENT = "recent_items"

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private val recentList: LinkedList<Insurance> = LinkedList()

    // 앱에서 반드시 초기화 필요 (Application 또는 Activity에서 1번만)
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // 최근 조회 목록 추가
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun addItem(item: Insurance) {
        val current = getRecentItems().toMutableList()

        // 중복 제거
        current.removeAll { it.name == item.name && it.company_name == item.company_name }

        // 앞에 추가
        current.add(0, item)

        // 3개 초과 시 제거
        if (current.size > 3) {
            current.removeAt(current.size - 1)
        }

        // 저장
        val json = gson.toJson(current)
        sharedPreferences.edit().putString(KEY_RECENT, json).apply()
    }

    fun getRecentItems(limit: Int = 3): List<Insurance> {
        val json = sharedPreferences.getString(KEY_RECENT, null)
        return if (json != null) {
            val type = object : TypeToken<List<Insurance>>() {}.type
            val fullList: List<Insurance> = gson.fromJson(json, type)
            fullList.take(limit)  // 원하는 개수만 반환
        } else {
            emptyList()
        }
    }

    fun clear() {
        sharedPreferences.edit().remove(KEY_RECENT).apply()
    }
}