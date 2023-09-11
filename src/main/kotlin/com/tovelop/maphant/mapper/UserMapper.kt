package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.UserDTO
import com.tovelop.maphant.dto.user.UserNicknameDTO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface UserMapper {
    fun insertUser(user: UserDTO)
    fun updateUserStateByUserId(userId: Int, state: Int)
    fun countSameEmails(email: String): Int
    fun countSameNickName(nickName: String): Int
    fun countSamePhoneNum(phNum: String): Int
    fun findUserByEmail(emails: List<String>): List<UserDTO>
    fun findEmailBysNo(sno: String, phNum: String): String?
    fun updateUserByEmail(id: Int)
    fun findUniversityIdBy(univName: String): Int?
    fun isUniversityExist(univId: Int): Boolean
    fun findUniversityUrlBy(univId: Int?): String
    fun updateUserState(email: String, state: Int)
    fun updateUserPasswordByEmail(email: String, password: String)
    fun updateUserNicknameByEmail(email: String, nickname: String)
    fun updateUserPhoneNumByEmail(email: String, phNum: String)
    fun findPasswordByEmail(email: String): String
    fun findNicknameByEmail(email: String): String
    fun findStateByUserId(userId: Int): Int
    fun getAllCategories(): List<String>
    fun getAllMajors(): List<String>
    fun getAllUnivNames(): List<String>
    fun insertCategoryIdMajorIdByUserId(userId: Int, categoryId: Int, majorId: Int)
    fun findUserIdByUserEmail(email: String): Int
    fun findCategoryIdByCategoryName(categoryName: String): Int
    fun findMajorIdByMajorName(majorName: String): Int

    fun updateUserRole(role: String, id: Int)
    fun findNicknameIdBy(userId: Int): String?


    fun searchUserByNickname(nickname: String): List<UserNicknameDTO>

    fun countById(userId: Int): Int
}
