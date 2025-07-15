package com.example.llm_project_android.data.local.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromList(list: List<String>?): String? = Gson().toJson(list)

    @TypeConverter
    fun toList(data: String?): List<String>? {
        return Gson().fromJson(data, object : TypeToken<List<String>>() {}.type)
    }
}
