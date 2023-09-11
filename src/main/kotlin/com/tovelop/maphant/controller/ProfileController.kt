package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.BoardResDto
import com.tovelop.maphant.dto.CommentExtDTO
import com.tovelop.maphant.dto.ProfileCategoryDTO
import com.tovelop.maphant.service.AwsS3Service
import com.tovelop.maphant.service.ProfileService
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.type.response.Response
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/profile")
class ProfileController(
    val profileService: ProfileService,
    private val awsS3Service: AwsS3Service
) {

    @GetMapping
    fun getProfile(@RequestParam targetUserId: Int?): ResponseEntity<Response<ProfileCategoryDTO>> {
        val userId: Int = (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId()

        if(userId == null)throw IllegalStateException("존재하지 않는 회원입니다")

        return ResponseEntity.ok()
            .body(Response.success(profileService.getNicknameAndBodyAndImage(targetUserId ?: userId)))
    }

    @PatchMapping
    fun updateProfile(
        @RequestParam nickname: String?,
        @RequestParam body: String?,
        @RequestParam file: MultipartFile?
    ): ResponseEntity<Response<String>> {
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken
        val userId: Int = auth.getUserId()
        if (nickname != null) profileService.updateProfileNickname(userId, nickname)
        if (body != null) profileService.updateProfileBody(userId, body)
        if (file != null) {
            if (file.isEmpty) profileService.updateProfileImageDefault(userId) else {
                val imageUrl: String = awsS3Service.uploadFile(file)
                profileService.updateProfileImage(userId, imageUrl, file)
            }
        }

        return ResponseEntity.ok().body(Response.success("프로필 수정 성공"))
    }

    @GetMapping("/comment")
    fun getCommentList(
        @ModelAttribute @Valid pagingDto: PagingDto,
        @RequestParam targetUserId: Int?
    ): ResponseEntity<Response<PagingResponse<CommentExtDTO>>> {
        val userId: Int = (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId()

        if(userId == null)throw IllegalStateException("존재하지 않는 회원입니다")

        return ResponseEntity.ok()
            .body(Response.success(profileService.getCommentList(userId, targetUserId ?: userId, pagingDto)))
    }

    @GetMapping("/board")
    fun getBoardList(
        @ModelAttribute @Valid pagingDto: PagingDto,
        @RequestParam targetUserId: Int?
    ): ResponseEntity<Response<PagingResponse<BoardResDto>>> {
        val userId: Int = (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId()

        if(userId == null)throw IllegalStateException("존재하지 않는 회원입니다")

        return ResponseEntity.ok()
            .body(Response.success(profileService.getBoardsList(userId, targetUserId ?: userId, pagingDto)))
    }

    @GetMapping("/like")
    fun getLikeBoardList(
        @ModelAttribute @Valid pagingDto: PagingDto,
    ): ResponseEntity<Response<PagingResponse<BoardResDto>>> {
        val userId: Int = (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId()

        if(userId == null)throw IllegalStateException("존재하지 않는 회원입니다")

        return ResponseEntity.ok()
            .body(Response.success(profileService.getLikeBoardsList(userId, pagingDto)))
    }

}