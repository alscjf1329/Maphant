package com.tovelop.maphant.dto

data class AdminCommentReportDTO(
    val commentId: Int,
    val commentBody: String,
    val commentUserId: Int,
    val commentUserEmail: String
)
