// 유저 데이터를 고수준에서 관리하는 중간 레이어

package com.example.llm_project_android.db.wishList

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WishedManager(context: Context) {

    private val db = WishedDatabase.getWishedDatabase(context)
    private val dao = db.getWishedDao()

    // 상품 찜하기 (isWished == true)
    suspend fun addWish(insuranceName: String) {
        withContext(Dispatchers.IO) {
            val existingWish = dao.getWishByName(insuranceName)
            if (existingWish == null) {     // 기존에 찜하지 않았을 경우
                dao.insertWished(
                    WishList(
                        insuranceName = insuranceName,
                        wishedTimeStamp = System.currentTimeMillis(),
                        isWished = true
                    )
                )
            } else {                       // 기존에 찜이 되어 있을 경우
                dao.insertWished(
                    existingWish.copy(
                        isWished = true,
                        wishedTimeStamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    // 특정 상품 찜 해제
    suspend fun removeWish(insuranceName: String) {
        withContext(Dispatchers.IO) {
            dao.deleteWishByName(insuranceName)
            }
        }

    // 특정 상품 찜 여부 확인
    suspend fun isWished(insuranceName: String) : Boolean {
        return withContext(Dispatchers.IO) {
            dao.getWishByName(insuranceName) != null
        }
    }

    // 찜 목록 가져오기 (시간순 정렬)
    suspend fun getAllWishes() : List<WishList> {
        return withContext(Dispatchers.IO) {
            dao.getAllWishes()
        }
    }

    // 전체 찜 삭제
    suspend fun clearAllWishes() {
        withContext(Dispatchers.IO) {
            dao.deleteAllWishes()
            dao.resetAutoIncrement()
        }
    }
}