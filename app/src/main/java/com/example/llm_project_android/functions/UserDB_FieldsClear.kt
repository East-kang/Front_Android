// 앱 내에서 사용자(User) 정보 중 특정 필드 또는 전체를 초기화할 수 있는 공용 함수 집합

package com.example.llm_project_android.functions

import android.content.Context
import com.example.llm_project_android.db.Users.MyDatabase
import com.example.llm_project_android.db.Users.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// 일부 데이터 초기화
suspend fun clearUserFields(context: Context, fieldsToClear: List<String>) {
    withContext(Dispatchers.IO) {
        val dao = MyDatabase.getDatabase(context).getMyDao()
        val currentUser = dao.getLoggedInUser()

        currentUser?.let { user ->
            val updatedUser = user.copy(
                userId = if ("userId" in fieldsToClear) "" else user.userId,
                password = if ("password" in fieldsToClear) "" else user.password,
                email = if ("email" in fieldsToClear) "" else user.email,
                name = if ("name" in fieldsToClear) "" else user.name,
                phoneNumber = if ("phoneNumber" in fieldsToClear) "" else user.phoneNumber,
                birthDate = if ("birthDate" in fieldsToClear) "" else user.birthDate,
                gender = if ("gender" in fieldsToClear) "" else user.gender,
                isMarried = if ("isMarried" in fieldsToClear) false else user.isMarried,
                job = if ("job" in fieldsToClear) "" else user.job,
                diseases = if ("diseases" in fieldsToClear) emptyList() else user.diseases,
                subscriptions = if ("subscriptions" in fieldsToClear) emptyList() else user.subscriptions,
                modifiedAt = "" // 변경 시간 초기화 or 유지 필요 시 user.modifiedAt
            )
            dao.updateUser(updatedUser)
        }
    }
}

// 질병 데이터 초기화
suspend fun clearUserDiseases(context: Context) {
    withContext(Dispatchers.IO) {
        val dao = MyDatabase.getDatabase(context).getMyDao()
        val currentUser = dao.getLoggedInUser()

        currentUser?.let {
            val updatedUser = it.copy(diseases = emptyList())
            dao.updateUser(updatedUser)
        }
    }
}

// 전체 데이터 초기화
suspend fun clearAllUserFields(context: Context) {
    withContext(Dispatchers.IO) {
        val dao = MyDatabase.getDatabase(context).getMyDao()
        val currentUser = dao.getLoggedInUser()

        currentUser?.let { user ->
            val clearedUser = user.copy(
                userId = "",
                password = "",
                email = "",
                name = "",
                phoneNumber = "",
                birthDate = "",
                gender = "",
                isMarried = false,
                job = "",
                diseases = emptyList(),
                subscriptions = emptyList(),
                createdAt = "",
                modifiedAt = "",
                isLogin = false,
                isDeleted = false
                // id는 유지됨 (Room autoGenerate primary key)
            )
            dao.updateUser(clearedUser)
        }
    }
}

suspend fun resetUserTable(context: Context) {
    withContext(Dispatchers.IO) {
        val dao = MyDatabase.getDatabase(context).getMyDao()

        // 1. 모든 사용자 삭제
        dao.deleteAllUsers()

        // 2. AUTO_INCREMENT 초기화
        dao.resetAutoIncrement()

        // 3. 새 유저 삽입 (id = 1부터 시작됨)
        val newUser = User(
            userId = "",
            password = "",
            email = "",
            name = "",
            phoneNumber = "",
            birthDate = "",
            gender = "",
            isMarried = false,
            job = "",
            diseases = emptyList(),
            subscriptions = emptyList(),
            createdAt = "",
            modifiedAt = "",
            isLogin = false,
            isDeleted = false
        )

        dao.insertUser(newUser)
    }
}