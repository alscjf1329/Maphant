package com.tovelop.maphant.configure.security

import com.tovelop.maphant.dto.UserDataDTO
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserData(
    private val email: String,
    private val password: String,
    private var userData: UserDataDTO,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()
        val roleWithPrefix = "ROLE_${userData.role}"
        authorities.add(SimpleGrantedAuthority(roleWithPrefix))
        return authorities
    }

    override fun getPassword() = password

    override fun getUsername() = email

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun getUserData() = this.userData

    fun setUserData(userData: UserDataDTO) {
        this.userData = userData
    }

    fun getUserID() = this.userData.id

    fun getUserCategories() = this.userData.category

    fun getUserRole() = this.userData.role

    fun getUserStudentNo() = this.userData.studentNo

    fun zeroisePassword() {
        this.userData.password = ""
    }
}