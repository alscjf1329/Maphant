package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class BoardResDto(
    val id:Int,
    val category_id: Int,
    val user_id:Int,
    val type: String,
    val title:String,
    val body:String,
    val is_hide:Int,
    val is_complete:Boolean,
    val is_anonymous:Boolean,
    val created_at:LocalDateTime,
    val modified_at:LocalDateTime?,
    val comment_cnt:Int,
    val like_cnt:Int,
    val report_cnt:Int,
    val images_url:String?,
    val isLike:Boolean
)
