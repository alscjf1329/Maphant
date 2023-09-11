package com.tovelop.maphant.service

import com.tovelop.maphant.dto.*
import com.tovelop.maphant.mapper.BlockMapper
import com.tovelop.maphant.mapper.DmMapper
import com.tovelop.maphant.mapper.RoomMapper
import com.tovelop.maphant.mapper.UserMapper
import com.tovelop.maphant.type.paging.CursorResponse
import com.tovelop.maphant.type.paging.Pagination
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.type.paging.dm.DmCursorPagingResponse
import com.tovelop.maphant.type.paging.dm.DmPagingResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class DmService(
    private val roomMapper: RoomMapper,
    private val dmMapper: DmMapper,
    private val userMapper: UserMapper,
    private val fcmService: FcmService,
    private val blockMapper: BlockMapper
) {

    fun findUnReadDmCount(userId:Int):Int {
        return roomMapper.findUnReadDmCount(userId);
    }

    fun findRoomList(userId: Int): List<RoomListResultDto> {
        return roomMapper.findRoomList(userId)
    }

    @Transactional
    fun sendDm(userNickname:String, sender_id: Int, receiver_id: Int, content: String): DmDto {
        if(sender_id == receiver_id) throw IllegalStateException("자기 자신한테 쪽지를 보낼 수 없습니다.")
        if(content.isNullOrEmpty()) throw IllegalStateException("content에 내용을 작성해 주세요.")
        val receiverNickname = userMapper.findNicknameIdBy(receiver_id);
        if(receiverNickname == null) {
            throw NullPointerException("receiver_id에 해당하는 user가 없습니다.")
        }

        var is_sender: Boolean = true
        var room: RoomDto? = roomMapper.findRoom(sender_id, receiver_id);

        if(blockMapper.getBlockCount(sender_id, receiver_id)>0||blockMapper.getBlockCount(receiver_id, sender_id)>0){
            throw IllegalStateException("쪽지를 보낼 수 없는 상대입니다.")
        }


        if (room == null) { // 로그인한 사용자가 sender_id로 만든 대화방이 없는경우
            room = roomMapper.findRoom(receiver_id, sender_id);
            is_sender = false

            if (room == null) { //로그인한 사용자가 receiver_id와의 대화방이 없는경우
                // 대화방 만들기
                roomMapper.createRoom(
                    CreateRoomDto(
                        last_content = content,
                        time = LocalDateTime.now(),
                        sender_id = sender_id,
                        receiver_id = receiver_id,
                        sender_is_deleted = false,
                        receiver_is_deleted = false,
                        sender_unread_count=0,
                        receiver_unread_count=0,
                    )
                )
                // 만든 대화방 가져오기
                room = roomMapper.findRoom(sender_id, receiver_id);
                // 로그인한 사용자가 sender이므로 is_sender는 true
                is_sender = true
            }
        }

        //여기로 오면 항상 room은 null이 아니다.
        room as RoomDto

        val dmDto = DmDto(
            id = null,
            is_from_sender = is_sender,
            content = content,
            is_read = false,
            time = LocalDateTime.now(),
            room_id = room.id,
            visible = VisibleChoices.BOTH
        )

        dmMapper.createDm(dmDto)
        roomMapper.updateRoomWhenSendDm(dmDto.time,dmDto.content,is_sender,room.id)

        fcmService.send(FcmMessageDTO(
            receiver_id,
            userNickname,
            content,
            mapOf(
                "roomId" to "${room.id}",
            )
        ))

        return dmDto
    }

    @Transactional
    fun getDmListWithCursorBasedPaging(meId: Int, roomId: Int, cursor: Int, limit:Int): DmCursorPagingResponse<ResultDmDto> {
        var isSender: Boolean? = null;
        var room: RoomDto = roomMapper.findRoomById(roomId)

        if (room == null) {
            throw NullPointerException("대화방이 존재하지 않습니다.")
        }

        if (room.sender_id == meId) isSender = true
        if (room.receiver_id == meId) isSender = false

        if (isSender == null) throw NullPointerException("대화방이 존재하지 않습니다.")

        //안읽은 dm읽음 처리, unread_count = 0으로 업데이트
        dmMapper.updateNotReadDm(roomId, !isSender)

        if(isSender){
            dmMapper.updateSenderUnreadDmZero(roomId)
        }
        else dmMapper.updateReceiverUnreadDmZero(roomId)

        var cursor = cursor
        //프론트에서 처음으로 요청한 경우
        if(cursor == 0)  cursor = dmMapper.findLastDmId(roomId);



        val dmCursor = if (isSender) room.sender_dm_cursor else room.receiver_dm_cursor
        val otherId = if (isSender) room.receiver_id else room.sender_id

        val list = dmMapper.findDmListWithCursorBasedPaging(isSender, roomId, cursor, dmCursor,limit)
        val otherName = userMapper.findNicknameIdBy(otherId) as String
        val nextCursor = if(list.size == limit) list[limit-1].id else null

        return DmCursorPagingResponse(otherId, otherName, list, nextCursor)
    }

    @Transactional
    fun deleteRoom(meId: Int, roomId: Int) {
        var isSender: Boolean? = null

        var room = roomMapper.findRoomById(roomId)

        if (room == null) throw NullPointerException("대화방이 존재하지 않습니다.")

        if (room.sender_id == meId) isSender = true;
        if (room.receiver_id == meId) isSender = false;


        if (isSender == null) throw NullPointerException("대화방이 존재하지 않습니다.")

        if (isSender == true) {
            roomMapper.updateWhenSenderIsDeleted(roomId)
        } else {
            roomMapper.updateWhenReceiverIsDeleted(roomId)
        }
    }
}