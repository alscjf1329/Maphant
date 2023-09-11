package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class BoardNoticeDTO(
    val id: Int? = null,
    val boardType: Int,
    val title: String,
    val body: String,
    val imagesUrl: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime? = null
)
