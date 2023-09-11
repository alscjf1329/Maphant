package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.*
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.response.SuccessResponse
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile

@Mapper
@Repository
interface ProfileMapper {
    // user테이블의 id 값으로 프로필 사진 불러오기
    fun findNicknameAndBodyAndImageById(userId:Int):ProfileNicknameAndBodyAndImageDto

    fun findById(userId: Int): ProfileDto?

    fun updateProfileImage(userId: Int, imageUrl:String):Boolean

    fun updateProfileNickname(userId: Int, nickname: String):Boolean

    fun updateProfileBody(userId: Int, body: String):Boolean

    fun findAllBoardByIdWithPaging(userId: Int, params: PagingDto): List<BoardResDto>

    fun insertProfile(userId: Int, imageUrl: String): Boolean

    fun getBoardCount(userId: Int): Int
}