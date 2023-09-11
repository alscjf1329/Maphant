package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.UserDataService
import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.EmailAuthDTO
import com.tovelop.maphant.service.UserService
import com.tovelop.maphant.type.response.Response
import com.tovelop.maphant.type.response.ResponseUnit
import com.tovelop.maphant.utils.SecurityHelper.Companion.isLogged
import com.tovelop.maphant.utils.SendGrid
import com.tovelop.maphant.utils.ValidationHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/email")
class EmailAuthController(@Autowired val sendGrid: SendGrid,
                          @Autowired val userService: UserService,
                          @Autowired val userDataService: UserDataService,
) {
    @PostMapping("/sendsignup")
    fun sendAuthCode(@RequestBody emailAuthDTO: EmailAuthDTO): ResponseEntity<ResponseUnit> {
        if (!ValidationHelper.isUniversityEmail(emailAuthDTO.email))
            return ResponseEntity.badRequest().body(Response.error("형식에 맞지 않는 이메일입니다."))

        sendGrid.sendSignUp(emailAuthDTO.email)

        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @PostMapping("/confirmEmail")
    fun confirmEmail(@RequestBody emailAuthDTO: EmailAuthDTO): ResponseEntity<ResponseUnit> {
        if (emailAuthDTO.authCode.isNullOrEmpty()) return ResponseEntity.badRequest()
            .body(Response.error("인증코드를 입력해 주세요."))

        if (!sendGrid.confirmEmailToken(emailAuthDTO.email, emailAuthDTO.authCode)) return ResponseEntity.badRequest()
            .body(Response.error("인증코드가 일치하지 않습니다."))

        userService.updateUserState(emailAuthDTO.email, 1)

        val auth = SecurityContextHolder.getContext().authentication
        if (auth.isLogged()) {
            userDataService.updateUserDataByUserId((auth as TokenAuthToken).getUserId())
        }

        return ResponseEntity.ok(Response.stateOnly(true))
    }
}
