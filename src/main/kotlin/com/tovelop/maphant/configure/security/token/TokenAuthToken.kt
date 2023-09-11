package com.tovelop.maphant.configure.security.token

import com.tovelop.maphant.configure.security.PasswordEncoderSHA512
import com.tovelop.maphant.configure.security.UserData
import com.tovelop.maphant.dto.UserDataDTO
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.GrantedAuthority

class TokenAuthToken(
    private val headerAuth: String,
    private val headerTS: Int,
    private val headerSign: String,
    private val userData: UserData? = null,
    authorities: MutableCollection<out GrantedAuthority>? = null
) : AbstractAuthenticationToken(authorities) {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return userData!!.authorities
    }

    override fun getCredentials(): Triple<String, Int, String> = Triple(headerAuth, headerTS, headerSign)
    override fun getPrincipal() = headerAuth

    fun isExpired(): Boolean {
        return false
    }

    fun createToken(timestamp: Int, privToken: String): String {
        val encoder = PasswordEncoderSHA512()
        val token = encoder.encode(timestamp.toString() + privToken)
        return if (headerAuth == "maphant@pubKey" || timestamp == -1) privToken else token
    }

    override fun isAuthenticated() = userData != null

    fun getUserData(): UserDataDTO {
        return userData?.getUserData() ?: throw BadCredentialsException("No user")
    }

    fun getUserId(): Int {
        return userData?.getUserID() ?: throw BadCredentialsException("No user")
    }

    fun getUserCategories() = userData?.getUserCategories() ?: throw BadCredentialsException("유저의 계열정보가 존재하지 않습니다.")


    fun getUserRole(): String {
        return userData?.getUserRole() ?: throw BadCredentialsException("No user")
    }

    fun getUserProfileImg(): String? {
        return userData?.getUserData()?.profileImg
    }
}