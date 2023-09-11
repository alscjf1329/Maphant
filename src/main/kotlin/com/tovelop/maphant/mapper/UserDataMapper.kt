package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.UserDataDTO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface UserDataMapper {
    fun findUserByEmail(email: String): UserDataDTO?

    fun updateUserDataByUserId(userId: Int): UserDataDTO?
}