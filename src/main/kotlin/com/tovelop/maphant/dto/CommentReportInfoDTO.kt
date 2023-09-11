package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class CommentReportInfoDTO(
    val reportId: Int,
    val reporterUserId: Int,
    val reporterUserEmail: String,
    val commentReportedAt: LocalDateTime,
    val commentReportName: String
)
