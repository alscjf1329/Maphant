package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class PollDTO(
    val id: Int? = null,
    val boardId: Int,
    val title: String,
    val options: List<String>,
    val expireDateTime: LocalDateTime? = LocalDateTime.now(),
    var state: Int = 0
)

data class BoardPollDto (
    val id: Int? = null,
    val title: String,
    val options: List<String>,
    val expireDateTime: LocalDateTime? = LocalDateTime.now(),
    var state: Int = 0
)
