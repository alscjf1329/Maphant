package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class BoardNoticeListDTO(
    val id: Int,
    val title: String,
    val imagesUrl: String,
    val createdAt: LocalDateTime,
)
