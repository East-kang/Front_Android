// 유저 데이터를 고수준에서 관리하는 중간 레이어

package com.example.llm_project_android.db.user

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserManager(context: Context) {

    private val db = MyDatabase.getDatabase(context)
    private val dao = db.getMyDao()

    // 사용자 저장 (로그인 시)
    suspend fun saveUser(user: User) {
        withContext(Dispatchers.IO) {
            dao.insertUser(user)
        }
    }

    // 사용자 가져오기 (조회 시)
    suspend fun getUser(): User? {
        return withContext(Dispatchers.IO) {
            dao.getLoggedInUser()
        }
    }

    // 로그아웃: 유저 전체 삭제
    suspend fun clearUser() {
        withContext(Dispatchers.IO) {
            dao.deleteAllUsers()
        }
    }
}
