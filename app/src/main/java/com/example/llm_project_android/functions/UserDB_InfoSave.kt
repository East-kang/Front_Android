// 사용자 정보 저장하기 공통 함수

package com.example.llm_project_android.functions

import android.content.Context
import com.example.llm_project_android.db.MyDatabase
import com.example.llm_project_android.db.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun saveUserInfo(
    context: Context,
    userId: String? = null,
    password: String? = null,
    email: String? = null,
    name: String? = null,
    phoneNumber: String? = null,
    birthDate: String? = null,
    gender: String? = null,
    isMarried: Boolean? = null,
    job: String? = null,
    diseases: List<String>? = null,
    subscriptions: List<String>? = null,
    createdAt: String? = null,
    modifiedAt: String? = null,
    isLogin: Boolean? = null,
    isDeleted: Boolean? = null
) {
    withContext(Dispatchers.IO) {
        val dao = MyDatabase.getDatabase(context).getMyDao()
        val currentUser = dao.getLoggedInUser() ?: User.empty()

        val updatedUser = currentUser.copy(
            userId = userId ?: currentUser.userId,
            password = password ?: currentUser.password,
            email = email ?: currentUser.email,
            name = name ?: currentUser.name,
            phoneNumber = phoneNumber ?: currentUser.phoneNumber,
            birthDate = birthDate ?: currentUser.birthDate,
            gender = gender ?: currentUser.gender,
            isMarried = isMarried ?: currentUser.isMarried,
            job = job ?: currentUser.job,
            diseases = diseases ?: currentUser.diseases,
            subscriptions = subscriptions ?: currentUser.subscriptions,
            createdAt = createdAt ?: currentUser.createdAt,
            modifiedAt = modifiedAt ?: currentUser.modifiedAt,
            isLogin = isLogin ?: currentUser.isLogin,
            isDeleted = isDeleted ?: currentUser.isDeleted
        )
        dao.insertUser(updatedUser)
    }
}
