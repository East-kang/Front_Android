// Room DB 객체 생성과 싱글톤 관리. DB와 DAO 연결 역할

package com.example.llm_project_android.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.llm_project_android.db.Converter.Converters
import com.example.llm_project_android.db.Users.User

//Room database의 기본틀
@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {    // RoomDatabase 상속
    abstract fun getMyDao() : MyDAO

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context) : MyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "users"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}