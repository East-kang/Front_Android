// Room에서 List<String> ↔ String 변환에 사용되는 TypeConverter 클래스

package com.example.llm_project_android.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(data, type)
    }
}
