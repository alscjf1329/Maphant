package com.tovelop.maphant.dto

import org.joda.time.DateTime
import java.time.LocalDate
import java.time.LocalDateTime

enum class VisibleChoices {
    BOTH,
    ONLY_SENDER,
    ONLY_RECEIVER,
    NOBODY
}

data class DmDto(
    var id: Int?,
    val is_from_sender: Boolean,
    val content: String,
    val time: LocalDateTime,
    val is_read: Boolean,
    val room_id: Int,
    var visible: VisibleChoices
) {
    fun createDm(): DmDto {
        return DmDto(
            id = null,
            is_from_sender = is_from_sender,
            content = content,
            is_read = false,
            time = LocalDateTime.now(),
            room_id = room_id,
            visible = visible
        )
    }
}
