// Room DB 객체 생성과 싱글톤 관리. DB와 DAO 연결 역할

package com.example.llm_project_android.db.Users

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.llm_project_android.db.Converter.Converters
import com.example.llm_project_android.db.WishList.UserWishList
import com.example.llm_project_android.db.WishList.UserWishListDao

//Room database의 기본틀
@Database(entities = [User::class, UserWishList::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {    // RoomDatabase 상속
    abstract fun getMyDao() : MyDAO                     // 유저 DAO
    abstract fun getUserWishListDao(): UserWishListDao  // 찜 목록 DAO

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            val migration2to3 = object : Migration(2, 3) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // user_wish_list 테이블 없으면 생성
                    database.execSQL("""
                        CREATE TABLE IF NOT EXISTS user_wish_list (
                            wishListId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            userId INTEGER NOT NULL,
                            productName TEXT NOT NULL,
                            wishedDate INTEGER NOT NULL,
                            FOREIGN KEY(userId) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """.trimIndent())
                }
            }

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "my_database"
                )
                    .addMigrations(migration2to3) // 마이그레이션 적용
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}