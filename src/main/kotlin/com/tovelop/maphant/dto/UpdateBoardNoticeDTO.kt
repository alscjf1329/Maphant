package com.tovelop.maphant.dto

data class UpdateBoardNoticeDTO(
    val noticeId: Int,
    val title: String,
    val body: String,
    val imagesUrl: String
)
