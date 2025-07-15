package com.example.llm_project_android.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.TypeConverters
import com.example.llm_project_android.data.local.dao.ChatDao
import com.example.llm_project_android.data.local.dao.RecentInsuranceDao
import com.example.llm_project_android.data.local.entity.ChatEntity
import com.example.llm_project_android.data.local.entity.RecentInsuranceEntity

@Database(
    entities = [RecentInsuranceEntity::class, ChatEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recentInsuranceDao(): RecentInsuranceDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
