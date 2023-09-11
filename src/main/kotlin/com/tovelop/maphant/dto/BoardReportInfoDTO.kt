package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class BoardReportInfoDTO(
    val reportId: Int,
    val reporterUserId: Int,
    val reporterUserEmail: String,
    val boardReportedAt: LocalDateTime,
    val boardReportName: String
)
