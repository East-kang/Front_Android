package com.example.llm_project_android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.llm_project_android.data.local.entity.RecentInsuranceEntity

@Dao
interface RecentInsuranceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RecentInsuranceEntity)

    @Query("SELECT * FROM recent_insurance ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentItems(limit: Int): List<RecentInsuranceEntity>

    @Query("DELETE FROM recent_insurance")
    suspend fun clearAll()
}
