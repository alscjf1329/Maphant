package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.FcmMessageDTO
import com.tovelop.maphant.dto.NotificationDBDTO
import com.tovelop.maphant.type.paging.PagingDto
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Mapper
@Repository
interface NotificationMapper {
    fun getNotificationsByUserId(userId: Int, params: PagingDto): List<NotificationDBDTO>
    fun createNotification(notification: FcmMessageDTO, jsonStr: String)

    fun cntNotificationsByUserId(userId: Int): Int
    fun updateNotificationReadAt(id: Int, readAt: LocalDateTime)
}
