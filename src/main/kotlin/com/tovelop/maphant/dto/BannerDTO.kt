package com.tovelop.maphant.dto

data class BannerDTO(
    val id: Int?,
    val title: String,
    val company: String,
    val imagesUrl: String,
    val url: String,
    val frequency: Int,
    val pay: Int
)
