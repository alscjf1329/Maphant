package com.tovelop.maphant.configure.security.token

import com.tovelop.maphant.configure.security.UserData
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class LoginAuthToken(
    private val username: String,
    private val password: String,
    private val userData: UserData? = null,
    authorities: MutableCollection<out GrantedAuthority>? = null,
): AbstractAuthenticationToken(authorities) {
    override fun getCredentials() = password

    override fun getPrincipal() = username

    override fun isAuthenticated() = userData != null

    fun getUserId() = userData?.getUserID()
}