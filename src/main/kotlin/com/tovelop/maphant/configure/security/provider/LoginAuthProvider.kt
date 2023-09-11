package com.tovelop.maphant.configure.security.provider

import com.tovelop.maphant.configure.security.TokenRepository
import com.tovelop.maphant.configure.security.UserDataService
import com.tovelop.maphant.configure.security.token.LoginAuthToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class LoginAuthProvider(
    val userDataService: UserDataService,
    val tokenRepository: TokenRepository,
    @Autowired val encoder: PasswordEncoder,
)
    : AuthenticationProvider{
    override fun authenticate(authentication: Authentication?): Authentication {
        val usernamePasswordAuthenticationToken = authentication as LoginAuthToken
        val userData = userDataService.loadUserByUsername(usernamePasswordAuthenticationToken.name)

        if(!encoder.matches(usernamePasswordAuthenticationToken.credentials, userData.password)) {
            throw BadCredentialsException("Invalid password")
        }

        val state = userData.getUserData().state

        if(state == 2) throw BadCredentialsException("제제된 사용자입니다.")
        else if(state == 3) throw BadCredentialsException("탈퇴된 사용자입니다.")

        val auth = tokenRepository.generateToken(userData)
        return LoginAuthToken(auth.first, auth.second, userData)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        if(authentication == null) return false

        return authentication == LoginAuthToken::class.java
    }

}