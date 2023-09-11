package com.tovelop.maphant.dto

import java.time.LocalDateTime

/**
 * 주간 로그를 위한 DTO
 * startDate: 해당 주의 시작 날짜
 */
data class WeekLogDTO(
    val startDate: LocalDateTime,
    val count: Int
)
