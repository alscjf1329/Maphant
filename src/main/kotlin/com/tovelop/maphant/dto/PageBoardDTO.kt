package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class PageBoardDTO(
    val boardId : Int,
    val title: String,
    val createdAt: LocalDateTime,
    val modifiedAt : LocalDateTime?,
    val userNickname: String,
    val commentCnt: Int,
    val likeCnt: Int,
    val isAnonymous: Int,
    val isHide: Int,
    var isLike: Int?,
    val imagesUrl: String? = null
)
