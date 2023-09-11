package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class NotificationDBDTO(
    val id: Int = 0,
    val etc: String? = null,
    val title: String = "",
    val body: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val readAt: LocalDateTime? = null
)

data class NotificationResponseDTO(
    val id: Int,
    val etc: Map<String, String>? = null,
    val title: String,
    val body: String,
    val createdAt: LocalDateTime,
    val readAt: LocalDateTime?
)