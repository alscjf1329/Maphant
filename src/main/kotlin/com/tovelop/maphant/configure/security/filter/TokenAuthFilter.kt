package com.tovelop.maphant.configure.security.filter

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.utils.ResponseJsonWriter.Companion.writeJSON
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

class TokenAuthFilter(authenticationManager: AuthenticationManager?)
    : AbstractAuthenticationProcessingFilter("/**", authenticationManager) {

    init {
        this.authenticationManager = authenticationManager
        this.setAuthenticationFailureHandler { request, response, exception ->
            run {
                val output = mutableMapOf<String, Any>(
                    "status" to false,
                    "message" to (exception.message ?: "unexpected error")
                )

                response.status = 401
                response.writeJSON(output)
            }
        }
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication? {
        // pubKey
        val headerAuth = request?.getHeader("x-auth") ?: throw BadCredentialsException("No auth header")

        // epoch time stamp
        val headerTS = request.getHeader("x-timestamp") ?: throw BadCredentialsException("No timestamp header")

        // privKey * timestamp
        val headerSign = request.getHeader("x-sign") ?: throw BadCredentialsException("No sign header")

        if(headerAuth != "maphant@pubKey" && headerTS.toInt() != -1) {
            val epoch = System.currentTimeMillis() / 1000
            if(epoch - headerTS.toInt() > 60) {
                throw BadCredentialsException("Timestamp expired")
            }
        }

        val authReq = TokenAuthToken(headerAuth, headerTS.toInt(), headerSign, request = request)

        return this.authenticationManager.authenticate(authReq)
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        if(httpRequest.getHeader("x-auth") != null) {
            super.doFilter(request, response, chain)
        } else {
            chain.doFilter(request, response)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        SecurityContextHolder.getContext().authentication = authResult
        chain.doFilter(request, response)
    }
}