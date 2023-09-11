package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.CreateRoomDto
import com.tovelop.maphant.dto.RoomDto
import com.tovelop.maphant.dto.RoomListResultDto
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Mapper
@Repository
interface RoomMapper {
    fun findRoom(sender_id: Int, receiver_id: Int): RoomDto?
    fun createRoom(createHeaderDto: CreateRoomDto): Boolean
    fun updateSenderUnreadCountAndLastContent(room_id: Int, content: String): Boolean
    fun updateReceiverUnreadCountAndLastContent(room_id: Int, content: String): Boolean

    fun findRoomById(room_id: Int): RoomDto
    fun findRoomList(user_id: Int): List<RoomListResultDto>
    fun updateWhenSenderIsDeleted(room_id: Int): Boolean
    fun updateWhenReceiverIsDeleted(room_id: Int): Boolean

    fun findUnReadDmCount(user_id: Int): Int
    fun updateSenderIsDelete(room_id: Int):Boolean
    fun updateReceiverIsDeleted(room_id: Int):Boolean

    fun updateRoomTime(time:LocalDateTime,room_id: Int):Boolean

    fun updateSenderDmCursor(room_id: Int): Boolean
    fun updateReceiverDmCursor(room_id: Int):Boolean

    fun updateRoomWhenSendDm(time:LocalDateTime, content: String, isSender:Boolean, room_id: Int):Boolean
}