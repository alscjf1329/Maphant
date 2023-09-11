package com.tovelop.maphant.configure.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.tovelop.maphant.configure.security.token.LoginAuthToken
import com.tovelop.maphant.dto.LoginDTO
import com.tovelop.maphant.mapper.TokenMapper
import com.tovelop.maphant.service.LogService
import com.tovelop.maphant.utils.ResponseJsonWriter.Companion.writeJSON
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

class LoginAuthFilter(
    authenticationManager: AuthenticationManager?,
    private val logService: LogService,
    private val tokenMapper: TokenMapper
)
    :AbstractAuthenticationProcessingFilter(AntPathRequestMatcher("/user/login", "POST"), authenticationManager) {

    private val objectMapper = ObjectMapper()

    init {
        this.authenticationManager = authenticationManager
        this.setAuthenticationSuccessHandler { request, response, authentication ->
            run {
                val authResult = authentication as LoginAuthToken
                val userIP = (request as HttpServletRequest).getHeader("X-Forwarded-For")

                logService.login(authResult.getUserId()!!, userIP)
                tokenMapper.insertToken(authResult.getUserId()!!, authResult.principal)

                val output = mutableMapOf<String, Any>(
                    "success" to true,
                    "pubKey" to authResult.principal,
                    "privKey" to authResult.credentials,
                )

                response.status = 200
                response.writeJSON(output)
            }
        }
        this.setAuthenticationFailureHandler { request, response, exception ->
            run {
                val output = mutableMapOf<String, Any>(
                    "success" to false,
                    "message" to (exception.message ?: "unexpected error")
                )

                response.status = 401
                response.writeJSON(output)
            }
        }
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication? {
        val body = request?.reader?.readText()
        val loginReq = objectMapper.readValue(body, LoginDTO::class.java)

        val email = loginReq.email ?: throw BadCredentialsException("no email field")
        val password = loginReq.password ?: throw BadCredentialsException("no password field")

        val authReq = LoginAuthToken(email, password)

        return this.authenticationManager.authenticate(authReq)
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val ip = (request as HttpServletRequest).getHeader("X-Forwarded-For")
        if(ip != null) {
            super.doFilter(request, response, chain)
        } else {
            chain.doFilter(request, response)
        }
    }
}