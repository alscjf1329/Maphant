package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.BoardResDto
import com.tovelop.maphant.dto.NotificationDBDTO
import com.tovelop.maphant.dto.NotificationResponseDTO
import com.tovelop.maphant.service.NotificationService
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.type.response.Response
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.format.annotation.DateTimeFormat as DateTimeFormat

@RestController
@RequestMapping("/notifications")
class NotificationController(private val notificationService: NotificationService) {

    @GetMapping("/")
    fun getNotifications(
        @ModelAttribute pagingDto: PagingDto,
    ): ResponseEntity<Response<PagingResponse<NotificationResponseDTO>>> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        return ResponseEntity.ok().body(Response.success(notificationService.getNotificationsByUserId(auth.getUserId(), pagingDto)))
    }

//    @GetMapping("/{userId}")
//    fun getNotificationsByUserId(@PathVariable userId: Int): List<NotificationResponseDTO> {
//        return notificationService.getNotificationsByUserId(userId)
//    }

    @PostMapping("/updateread")
    fun updateNotificationReadAt (@RequestBody body: Map<String, Any>): ResponseEntity<Any> {
        var id: Int?
        var readAt: LocalDateTime?
        try {
            id = body["id"] as Int
            val readAtString = body["readAt"] as String?
            if(readAtString == null) readAt = LocalDateTime.now()
            else readAt = LocalDateTime.parse(readAtString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response.error<Any>(e.message ?: "알 수 없는 오류가 발생했습니다."))
        }

        notificationService.updateNotificationReadAt(id, readAt)

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
