package com.tovelop.maphant.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.NotificationDBDTO
import com.tovelop.maphant.dto.NotificationResponseDTO
import com.tovelop.maphant.service.NotificationService
import com.tovelop.maphant.type.response.Response
import org.springframework.http.ResponseEntity
import org.springframework.http.StreamingHttpOutputMessage.Body
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/notifications")
class NotificationController(private val notificationService: NotificationService) {

    @GetMapping("/")
    fun getNotifications(): List<NotificationDBDTO> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        return notificationService.getNotificationsByUserId(auth.getUserId())
    }

    @GetMapping("/{userId}")
    fun getNotificationsByUserId(@PathVariable userId: Int): List<NotificationDBDTO> {
        return notificationService.getNotificationsByUserId(userId)
    }

    @PostMapping("/")
    fun updateNotification (body: Map<String, Any>): ResponseEntity<Any> {
        var id: Int
        var readAt: LocalDateTime?
        try {
            id = body["id"] as Int
            readAt = body["readAt"] as LocalDateTime
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response.error<Any>(e.message ?: "알 수 없는 오류가 발생했습니다."))
        }

        notificationService.updateNotification(id, readAt)

        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

//    @GetMapping("{userId}/{notificationId}")
//    fun updateNotification(
//        @PathVariable userId: Int,
//        @PathVariable notificationId: Int
//    ) {
//        notificationService.updateNotification(notificationId)
//    }
}
