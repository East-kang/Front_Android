// User <-> UserWishList 관계 클래스 정의

package com.example.llm_project_android.db.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.llm_project_android.db.Users.User
import com.example.llm_project_android.db.WishList.UserWishList

data class UserWithWIshList (
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",            // User 테이블의 PK
        entityColumn = "userId"         // UserWishList의 FK
    )
    val wishList: List<UserWishList>    // 해당 유저의 찜 목록

)