// 사용자 정보 저장하기 공통 함수

package com.example.llm_project_android.functions

import android.content.Context
import com.example.llm_project_android.db.MyDatabase
import com.example.llm_project_android.db.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun SaveUserInfo(
    context: Context,
    userId: String,
    password: String,
    email: String,
    name: String = "",
    phoneNumber: String = "",
    birthDate: String = "",
    gender: String = "",
    isMarried: Boolean = false,
    job: String = "",
    diseases: List<String> = emptyList(),
    subscriptions: List<String> = emptyList(),
    createdAt: String = "",
    modifiedAt: String = "",
    isLogin: Boolean = true,
    isDeleted: Boolean = false
) {
    withContext(Dispatchers.IO) {
        val dao = MyDatabase.getDatabase(context).getMyDao()
        val user = User(
            userId = userId,
            password = password,
            email = email,
            name = name,
            phoneNumber = phoneNumber,
            birthDate = birthDate,
            gender = gender,
            isMarried = isMarried,
            job = job,
            diseases = diseases,
            subscriptions = subscriptions,
            createdAt = createdAt,
            modifiedAt = modifiedAt,
            isLogin = isLogin,
            isDeleted = isDeleted
        )
        dao.insertUser(user)
    }
}
