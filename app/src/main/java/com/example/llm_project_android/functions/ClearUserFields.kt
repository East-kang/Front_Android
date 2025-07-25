package com.example.llm_project_android.functions

import android.content.Context
import com.example.llm_project_android.db.MyDatabase
import com.example.llm_project_android.db.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
