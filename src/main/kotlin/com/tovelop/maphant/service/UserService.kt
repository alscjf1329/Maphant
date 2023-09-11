package com.tovelop.maphant.service

import com.tovelop.maphant.dto.UserDTO
import com.tovelop.maphant.mapper.ProfileMapper
import com.tovelop.maphant.mapper.UserMapper
import com.tovelop.maphant.utils.ValidationHelper
import org.springframework.stereotype.Service


@Service
class UserService(val mapper: UserMapper, val profileMapper: ProfileMapper) {
    fun insertCategoryMajorByEmail(email: String, category: String, major: String) {
        mapper.insertCategoryIdMajorIdByUserId(
            mapper.findUserIdByUserEmail(email),
            mapper.findCategoryIdByCategoryName(category),
            mapper.findMajorIdByMajorName(major)
        )
    }

    fun updateUserStateByUserId(userId: Int, state: Int) {
        mapper.updateUserStateByUserId(userId, state)
    }

    fun getAllCategories() = mapper.getAllCategories()
    fun getAllMajors() = mapper.getAllMajors()
    fun getAllUnivNames() = mapper.getAllUnivNames()
    fun isValidatedEmail(userId: Int) = mapper.findStateByUserId(userId) == 1
    fun findPasswordByEmail(email: String) = mapper.findPasswordByEmail(email)
    fun findNicknameByEmail(email: String) = mapper.findNicknameByEmail(email)
    fun updateUserState(email: String, state: Int) {
        mapper.updateUserState(email, state)
    }

    fun searchUserByNickname(nickname: String) = mapper.searchUserByNickname(nickname)

    fun updateUserPasswordByEmail(email: String, newPassword: String) {
        mapper.updateUserPasswordByEmail(email, newPassword)
    }

    fun updateUserNicknameByEmail(email: String, newNickname: String) {
        mapper.updateUserNicknameByEmail(email, newNickname)
    }

    fun updateUserPhoneNumByEmail(email: String, newPhoneNum: String) {
        mapper.updateUserPhoneNumByEmail(email, newPhoneNum)
    }

    fun insertUserCategoryMajorByEmail(email: String, newCategoryId: Int, newMajorId: Int) {
        val userId = mapper.findUserIdByUserEmail(email)
        mapper.insertCategoryIdMajorIdByUserId(userId, newCategoryId, newMajorId)
    }

    fun deleteCategoryIdMajorIdByUserId(email: String, categoryId: Int, majorId: Int) {
        val userId = mapper.findUserIdByUserEmail(email)
        mapper.deleteCategoryIdMajorIdByUserId(userId, categoryId, majorId)
    }

    fun findCategoryIdByEmail(email: String): List<Int> {
        return mapper.findCategoryIdByEmail(email)
    }

    fun findCategoryIdByName(name: String): Int {
        return mapper.findCategoryIdByName(name)
    }

    fun findMajorIdByEmail(email: String): List<Int> {
        return mapper.findMajorIdByEmail(email)
    }

    fun findMajorIdByName(name: String): Int {
        return mapper.findMajorIdByName(name)
    }

    fun signUp(user: UserDTO): Boolean {
        insertUser(user)
        return true
    }

    fun login(email: String, password: String): String? {
        // 로그인 로직
        val user = getUser(email)
        if (user != null && user.password == password) {
            return user.id.toString()
        }
        return null
    }

    fun matchEmail(email: String, univId: Int?): Boolean {
        val universityName = this.extractFromEmail(email)
        return universityName == mapper.findUniversityUrlBy(univId)
    }

    fun getUser(email: String): UserDTO {
        // 사용자 조회 로직
        return mapper.findUserByEmail(listOf(email))[0]
    }

    fun insertUser(user: UserDTO) {
        // 사용자 추가 로직
        mapper.insertUser(user)
    }

    fun findUniversityIdBy(univName: String): Int? {
        return mapper.findUniversityIdBy(univName)
    }

    fun isPasswordValid(password: String): Boolean {
        return ValidationHelper.isValidPassword(password)
    }

    fun isEmailValid(email: String): Boolean {
        return ValidationHelper.isUniversityEmail(email)
    }

    fun findEmailBysNo(sNo: String, phoneNum: String): String? {
        return mapper.findEmailBysNo(sNo, phoneNum)
    }

    fun isNicknameValid(nickname: String): Boolean {
        return ValidationHelper.isAlphaNumericKorean(nickname)
    }

    fun isUniversityValid(univId: Int?): Boolean {
        if (univId == null) {
            return true
        }
        return mapper.isUniversityExist(univId)
    }

    fun isDuplicateEmail(email: String): Boolean {
        return mapper.countSameEmails(email) > 0
    }

    fun isDuplicateNickname(nickname: String): Boolean {
        return mapper.countSameNickName(nickname) > 0
    }

    fun isDuplicatePhoneNum(phoneNum: String): Boolean {
        return mapper.countSamePhoneNum(phoneNum) > 0
    }

    fun extractFromEmail(email: String): String? {
        val pattern = Regex("""(?<=@)[^.]+\.(ac\.kr|edu)""")
        val matchResult = pattern.find(email)
        return matchResult?.value
    }

    fun updateUserRole(role: String, id: Int) {
        mapper.updateUserRole(role, id)
    }

    fun withDrawUser(email: String) {
        mapper.withDrawUser(email)
    }

    fun updateWithDrawUser(email: String) {
        mapper.updateWithDrawUser(email)
    }

    fun findUserIdByUserEmail(email: String): Int{
        return mapper.findUserIdByUserEmail(email)
    }
}
