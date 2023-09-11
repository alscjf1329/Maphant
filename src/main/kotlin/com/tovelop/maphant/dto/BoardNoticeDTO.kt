package com.tovelop.maphant.dto

data class BoardNoticeDTO(
    val id: Int,
    val boardType: Int,
    val title: String,
    val body: String,
    val imagesUrl: String
)
