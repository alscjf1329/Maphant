package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class BoardDTO(
    val id: Int?,
    val parentId: Int?,
    val categoryId: Int,
    val userId: Int,
    val typeId: Int,
    val title: String,
    val body: String,
    val state: Int,
    val isHide: Int,
    val isComplete: Int,
    val isAnonymous: Int,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
    val commentCnt: Int,
    val likeCnt: Int,
    val reportCnt: Int,
    val imagesUrl: String?
){
//    fun toExtBoardDTO(isLike: Boolean, tags:List<ReqTagDTO>): ExtBoardDTO {
//        return ExtBoardDTO(
//            id,
//            parentId,
//            categoryId,
//            userId,
//            typeId,
//            title,
//            body,
//            state,
//            isHide,
//            isComplete,
//            isAnonymous,
//            createdAt,
//            modifiedAt,
//            commentCnt,
//            likeCnt,
//            reportCnt,
//            imagesUrl,
//            isLike,
//            tags
//        )
//    }
}
