package com.tovelop.maphant.mapper

import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface TokenMapper {
    fun insertToken(userId: Int, token: String)

    fun getTokensByUserId(userId: Int): List<String>
}