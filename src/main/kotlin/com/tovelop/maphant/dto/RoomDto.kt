package com.tovelop.maphant.dto

import org.joda.time.DateTime
import java.time.LocalDateTime

data class RoomDto(
    val id: Int,
    val last_content: String,
    val sender_id: Int,
    val receiver_id: Int,
    val time: LocalDateTime,
    var sender_is_deleted: Boolean,
    var receiver_is_deleted: Boolean,
    // unread_count 추가
    val sender_unread_count: Int,
    val receiver_unread_count: Int,
    //cursor 추가
    val sender_dm_cursor: Int,
    val receiver_dm_cursor:Int,
)
