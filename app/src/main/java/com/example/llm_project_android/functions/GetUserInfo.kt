// 사용자 정보 불러오기 공통 함수

package com.example.llm_project_android.functions

import android.content.Context
import com.example.llm_project_android.db.MyDatabase
import com.example.llm_project_android.db.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun GetUserInfo(context: Context): User? {
    return withContext(Dispatchers.IO) {
        val dao = MyDatabase.getDatabase(context).getMyDao()
        // 현재 로그인 상태인 사용자 한 명 조회
        dao.getLoggedInUser()
    }
}
