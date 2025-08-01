// Room DB 객체 생성과 싱글톤 관리. DB와 DAO 연결 역할

package com.example.llm_project_android.db.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
<<<<<<<< HEAD:app/src/main/java/com/example/llm_project_android/db/Users/MyDataBase.kt
import com.example.llm_project_android.db.Converter.Converters
import com.example.llm_project_android.db.Users.User
========
import com.example.llm_project_android.db.converter.Converters
>>>>>>>> c21d2fdee798c3bd8221462e4bf9e3886670ad93:app/src/main/java/com/example/llm_project_android/db/user/MyDataBase.kt

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