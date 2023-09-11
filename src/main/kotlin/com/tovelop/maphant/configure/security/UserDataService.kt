package com.tovelop.maphant.configure.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.MockupUserDTO
import com.tovelop.maphant.dto.UserDTO
import com.tovelop.maphant.mapper.TokenMapper
import com.tovelop.maphant.mapper.UserDataMapper
import com.tovelop.maphant.service.RedisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.time.LocalDate

@Configuration
class UserDataService(@Autowired val userDataMapper: UserDataMapper,
                      @Autowired val redis: RedisService,
                      @Autowired val tokenMapper: TokenMapper
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserData {
        val user = userDataMapper.findUserByEmail(username) ?: throw UsernameNotFoundException("Not found")
        return UserData(username, user.password, user)
    }
    fun updateUserData(userId: Int? = null, token: String? = null) {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        val pubKey = token ?: auth.principal
        val userToken = redis.get("LOGIN_AUTH|${pubKey}")

        val userData = userDataMapper.updateUserDataByUserId(userId ?: auth.getUserId())
        userData!!.password = ""

        if(userId == null && token == null) auth.setUserData(userData)

        val objMapper = ObjectMapper()
        val value = objMapper.writeValueAsString(userData)

        redis.set("LOGIN_AUTH|${pubKey}", "${userToken!!.substringBefore('|')}|$value")
    }

    fun updateUserDataByUserId(userId: Int) {
        val tokens = tokenMapper.getTokensByUserId(userId)
        tokens.forEach {
            updateUserData(userId, it)
        }
    }
}