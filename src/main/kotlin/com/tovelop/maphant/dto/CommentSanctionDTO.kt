package com.tovelop.maphant.dto

data class CommentSanctionDTO(
    val id: Int,
    val boardId: Int,
    val body: String,
    val reportCnt: Int
)
