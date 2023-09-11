package com.tovelop.maphant.service

import com.tovelop.maphant.dto.*
import com.tovelop.maphant.mapper.BlockMapper
import com.tovelop.maphant.mapper.DmMapper
import com.tovelop.maphant.mapper.RoomMapper
import com.tovelop.maphant.mapper.UserMapper
import com.tovelop.maphant.type.paging.Pagination
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.type.paging.dm.DmPagingResponse
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
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

        val receiverNickname = userMapper.findNicknameIdBy(receiver_id);
        if(receiverNickname == null) {
            throw NullPointerException("receiver_id에 해당하는 user가 없습니다.")
        }

        var is_sender: Boolean = true
        var room: RoomDto = roomMapper.findRoom(sender_id, receiver_id);

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
        var is_from_sender: Boolean = false
        if (is_sender) is_from_sender = true

        val dmDto = DmDto(
            id = null,
            is_from_sender = is_from_sender,
            content = content,
            is_read = false,
            time = LocalDateTime.now(),
            room_id = room.id,
            visible = VisibleChoices.BOTH
        )

        dmMapper.createDm(dmDto)

        // is_from_sender == true이면 receiver_unread_count ++
        if(is_from_sender){
            roomMapper.updateSenderUnreadCountAndLastContent(room.id, content)
        }
        // is_from_sender == false이면 sender_unread_count ++
        else roomMapper.updateReceiverUnreadCountAndLastContent(room.id, content)


        fcmService.send(FcmMessageDTO(
            receiver_id,
            userNickname,
            content
        ))

        return dmDto
    }

    @Transactional
    fun getDmList(meId: Int, roomId: Int, params: PagingDto): PagingResponse<ResultDmDto> {
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


        if (isSender) {
            val count = dmMapper.findDmCount(roomId, VisibleChoices.BOTH, VisibleChoices.ONLY_SENDER);
            val pagination = Pagination(count, params)
            if (count < 1)
                return PagingResponse(Collections.emptyList(), null)
            val list =
                dmMapper.findDmListWithPaging(isSender, roomId, params, VisibleChoices.BOTH, VisibleChoices.ONLY_SENDER)

            val otherName = userMapper.findNicknameIdBy(room.receiver_id) as String;
            return DmPagingResponse(room.receiver_id, otherName, list, pagination)
        }

        val count = dmMapper.findDmCount(roomId, VisibleChoices.BOTH, VisibleChoices.ONLY_RECEIVER);
        val pagination = Pagination(count, params)
        if (count < 1)
            return PagingResponse(Collections.emptyList(), null)
        val list =
            dmMapper.findDmListWithPaging(isSender, roomId, params, VisibleChoices.BOTH, VisibleChoices.ONLY_RECEIVER)
        val otherName = userMapper.findNicknameIdBy(room.sender_id) as String
        return DmPagingResponse(room.sender_id, otherName, list, pagination)
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
            //상대방이 삭제 안한 경우
            dmMapper.updateDmVisible(roomId, VisibleChoices.BOTH, VisibleChoices.ONLY_RECEIVER)
            //상대방이 이미 삭제한 경우
            dmMapper.updateDmVisible(roomId, VisibleChoices.ONLY_SENDER, VisibleChoices.NOBODY)
            //isSenderDeleted = true
            roomMapper.updateSenderIsDeletedAndSenderUnreadCountZero(roomId)
        } else {
            //상대방이 삭제 안한 경우
            dmMapper.updateDmVisible(roomId, VisibleChoices.BOTH, VisibleChoices.ONLY_SENDER)
            //상대방이 이미 삭제한 경우
            dmMapper.updateDmVisible(roomId, VisibleChoices.ONLY_RECEIVER, VisibleChoices.NOBODY)
            //isReceiverDeleted = true
            roomMapper.updateReceiverIsDeletedAndReceiverUnreadCountZero(roomId)
        }
    }
}