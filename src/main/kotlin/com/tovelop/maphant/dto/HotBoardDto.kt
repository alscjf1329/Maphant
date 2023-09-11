package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class HotBoardDto(
    val boardId : Int,
    val title: String,
    val body : String,
    val userId: Int,
    val userNickname: String,
    val typeId: Int,
    val type: String,
    val commentCnt: Int,
    val likeCnt: Int,
    val isAnonymous: Int,
    val isHide: Int,
    var isLike: Boolean,
    val createdAt: LocalDateTime,
    val modifiedAt : LocalDateTime?
)
