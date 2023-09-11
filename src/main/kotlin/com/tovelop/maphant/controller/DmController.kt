package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.DmDto
import com.tovelop.maphant.dto.RequestSendDmDto
import com.tovelop.maphant.dto.user.UserNicknameDTO
import com.tovelop.maphant.service.DmService
import com.tovelop.maphant.service.UserService
import com.tovelop.maphant.type.response.Response
import com.tovelop.maphant.type.response.SuccessResponse
import com.tovelop.maphant.utils.SecurityHelper.Companion.isNotLogged
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dm")
class DmController(private val dmService: DmService, private val userService: UserService) {
    @PostMapping
    fun sendDM(@RequestBody @Validated requestSendDmDto: RequestSendDmDto): SuccessResponse<DmDto> {
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken
        val userNickname = auth.getUserData().nickname
        val userId: Int = auth.getUserId()

        // 서비스 호출
        return SuccessResponse(dmService.sendDm(userNickname, userId, requestSendDmDto.receiver_id as Int, requestSendDmDto.content as String))
    }

    @GetMapping("/unread/count")
    fun findAllUnreadDm(): ResponseEntity<Response<Int>> {
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken
        val userId: Int = auth.getUserId()

        return ResponseEntity.ok().body(Response.success(dmService.findUnReadDmCount(userId)))
    }

    @GetMapping("/target/search")
    fun searchTarget(@RequestParam nickname: String): ResponseEntity<Response<List<UserNicknameDTO>>> {
        println(SecurityContextHolder.getContext().authentication!!.isNotLogged())

        if (SecurityContextHolder.getContext().authentication!!.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error("로그인이 필요합니다."))
        }

        if (nickname.length < 2) {
            return ResponseEntity.badRequest().body(Response.error("닉네임은 2글자 이상 입력해주세요."))
        }

        return ResponseEntity.ok().body(Response.success(userService.searchUserByNickname(nickname)))
    }
}