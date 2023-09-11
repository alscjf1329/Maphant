package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.*
import com.tovelop.maphant.service.AwsS3Service
import com.tovelop.maphant.service.ProfileService
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.type.response.Response
import com.tovelop.maphant.type.response.SuccessResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/profile")
class ProfileController(
    val profileService: ProfileService,
    private val awsS3Service: AwsS3Service
) {

    @GetMapping
    fun getProfile(): ResponseEntity<Response<ProfileNicknameAndBodyAndImageDto>> {
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken
        val userId: Int = auth.getUserId()!!

        return ResponseEntity.ok().body(Response.success(profileService.getNicknameAndBodyAndImage(userId)));
    }

    @PatchMapping
    fun updateProfile(@RequestParam nickname:String?, @RequestParam body:String?, @RequestParam file: MultipartFile?): ResponseEntity<Response<String>> {
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken
        val userId: Int = auth.getUserId()!!

        if(nickname!=null)profileService.updateProfileNickname(userId, nickname)
        if(body!=null)profileService.updateProfileBody(userId, body)
        if(file!=null){
            val imageUrl: String = awsS3Service.uploadFile(file)
            profileService.updateProfileImage(userId, imageUrl, file)
        }


        return ResponseEntity.ok().body(Response.success("프로필 수정 성공"));
    }

    @GetMapping("/comment")
    fun getCommentList(@ModelAttribute @Valid pagingDto: PagingDto): ResponseEntity<Response<PagingResponse<CommentExtDTO>>>{
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken
        val userId: Int = auth.getUserId()

        return return ResponseEntity.ok().body(Response.success(profileService.getCommentList(userId, pagingDto)));
    }

    @GetMapping("/board")
    fun getBoardList(@ModelAttribute @Valid pagingDto: PagingDto): ResponseEntity<Response<PagingResponse<BoardResDto>>> {
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken
        val userId: Int = auth.getUserId()

        return ResponseEntity.ok().body(Response.success(profileService.getBoardsList(userId, pagingDto)));
    }
}