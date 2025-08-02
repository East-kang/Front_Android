// Room DB 객체 생성과 싱글톤 관리. DB와 DAO 연결 역할

package com.example.llm_project_android.db.wishList

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.llm_project_android.db.converter.Converters

//Room database의 기본틀
@Database(entities = [WishList::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WishedDatabase : RoomDatabase() {    // RoomDatabase 상속
    abstract fun getWishedDao() : WishedDAO

    companion object {
        @Volatile
        private var INSTANCE: WishedDatabase? = null

        fun getWishedDatabase(context: Context) : WishedDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WishedDatabase::class.java,
                    "wished_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}