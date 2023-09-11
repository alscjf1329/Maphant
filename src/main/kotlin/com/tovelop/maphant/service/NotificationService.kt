package com.tovelop.maphant.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.tovelop.maphant.dto.FcmMessageDTO
import com.tovelop.maphant.dto.NotificationDBDTO
import com.tovelop.maphant.dto.NotificationResponseDTO
import com.tovelop.maphant.mapper.NotificationMapper
import com.tovelop.maphant.type.paging.Pagination
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class NotificationService(
    private val notificationMapper: NotificationMapper
) {

    fun getNotificationsByUserId(userId: Int, params: PagingDto): PagingResponse<NotificationResponseDTO> {
        var count = notificationMapper.cntNotificationsByUserId(userId)
        val notifications = notificationMapper.getNotificationsByUserId(userId, params).map {
            NotificationResponseDTO(
                id = it.id,
                etc = ObjectMapper().readValue(it.etc ?: ""),
                title = it.title,
                body = it.body,
                createdAt = it.createdAt,
                readAt = it.readAt
            )
        }
        if (count < 1) return PagingResponse(Collections.emptyList(), null)
        val pagination = Pagination(count, params)

        return PagingResponse(notifications, pagination)
    }

    fun createNotification(notification: FcmMessageDTO) {
        val data: Map<String, String>? = notification.etc
        // convert map to json string
        val jsonStr = ObjectMapper().writeValueAsString(data)

        notificationMapper.createNotification(notification, jsonStr)
    }

    fun updateNotificationReadAt(id: Int, readAt: LocalDateTime) {
        notificationMapper.updateNotificationReadAt(id, readAt)
    }
}