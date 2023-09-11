package com.tovelop.maphant.service

import com.tovelop.maphant.dto.ResultDmDto
import com.tovelop.maphant.dto.RoomDto
import com.tovelop.maphant.dto.RoomListResultDto
import com.tovelop.maphant.dto.VisibleChoices
import com.tovelop.maphant.mapper.BlockMapper
import com.tovelop.maphant.mapper.DmMapper
import com.tovelop.maphant.mapper.RoomMapper
import com.tovelop.maphant.mapper.UserMapper
import com.tovelop.maphant.type.paging.CursorDto
import com.tovelop.maphant.type.paging.Pagination
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.dm.DmCursorPagingResponse
import com.tovelop.maphant.type.paging.dm.DmPagingResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class DmServiceTest {

    @Mock
    private lateinit var roomMapper: RoomMapper

    @Mock
    private lateinit var dmMapper: DmMapper

    @Mock
    private lateinit var userMapper: UserMapper

    @Mock
    private lateinit var fcmService: FcmService

    @Mock
    private lateinit var blockMapper: BlockMapper

    @InjectMocks
    private lateinit var dmService: DmService

    @Test
    @DisplayName("사용자의 안읽은 쪽지 개수를 반환한다.")
    fun findUnReadDmCountTest() {
        //given
        val userId = 1
        val expectResult = 10
        whenever(roomMapper.findUnReadDmCount(userId)).thenReturn(expectResult)

        //when
        val result = dmService.findUnReadDmCount(userId)

        //then
        assert(result == expectResult)
        verify(roomMapper).findUnReadDmCount(userId)
    }

    @Test
    @DisplayName("findRoomList를 호출시 List<RoomListResultDto>를 반환타입으로 가져야한다.")
    fun findRoomLisTest() {
        //given
        val userId = 1;
        val expectResult = listOf(
            RoomListResultDto(
                1,"test",userId,2, LocalDateTime.MAX,false,false,2,"nick",0
            )
        )
        whenever(roomMapper.findRoomList(userId)).thenReturn(expectResult)

        //when
        val result = dmService.findRoomList(userId)

        //then
        assert(result == expectResult)
        verify(roomMapper).findRoomList(userId)
    }

    @Test
    @DisplayName("receiver_id에 해당하는 user 튜플이 없는경우 예외를 반환한다.")
    fun sendDmNotReceiverUser() {
        //given
        val sender_id = 1
        val receiver_id = 2;
        val userNickname = "user"
        val content = "test"
        whenever(userMapper.findNicknameIdBy(receiver_id)).thenReturn(null)

        //when
        val exception = assertThrows<NullPointerException> {
            dmService.sendDm(userNickname,sender_id,receiver_id, content)
        }

        //then
        assertEquals("receiver_id에 해당하는 user가 없습니다.",exception.message)
    }

    @Test
    @DisplayName("sender_id로 만들어진 대화방이 있으면 해당 대화방을 외래키로 가지는 dm을 만든다.")
    fun sendDmWithSenderId() {
        //given
        val sender_id = 1
        val receiver_id = 2
        val userNickname = "user"
        val content = "test"
        val room = RoomDto(
            1, "test", sender_id, receiver_id, LocalDateTime.now(), false, false,0,0,0,0
        )
        whenever(roomMapper.findRoom(sender_id,receiver_id)).thenReturn(room)
        whenever(userMapper.findNicknameIdBy(receiver_id)).thenReturn("test")
        //when
        dmService.sendDm(userNickname,sender_id,receiver_id, content)

        //then
        verify(roomMapper, times(0)).findRoom(receiver_id,sender_id)
        verify(roomMapper, times(0)).createRoom(any());
        verify(dmMapper, times(1)).createDm(any())
        verify(roomMapper).updateRoomWhenSendDm(any(), eq(content), eq(true), eq(room.id));

    }

    @Test
    @DisplayName("receiver_id로 만들어진 대화방이 있으면 해당 대화방을 외래키로 가지는 dm을 만든다.")
    fun sendDmWithReceiverId() {
        //given
        val sender_id = 1
        val receiver_id = 2
        val userNickname = "user"
        val content = "test"
        val room = RoomDto(
            1, "test", receiver_id, sender_id, LocalDateTime.now(), false, false,0,0,0,0
        )
        whenever(roomMapper.findRoom(sender_id,receiver_id)).thenReturn(null)
        whenever(roomMapper.findRoom(receiver_id,sender_id)).thenReturn(room)
        whenever(userMapper.findNicknameIdBy(receiver_id)).thenReturn("test")
        //when
        dmService.sendDm(userNickname,sender_id,receiver_id, content)

        //then
        verify(roomMapper, times(1)).findRoom(receiver_id,sender_id)
        verify(roomMapper, times(0)).createRoom(any());
        verify(dmMapper, times(1)).createDm(any())
        verify(roomMapper).updateRoomWhenSendDm(any(), eq(content), eq(false), eq(room.id));
    }

    @Test
    @DisplayName("sender_id, receiver_id로 만들어진 대화방이 없으면 대화방을 새로 만들고 해당 대화방을 외래키로 가지는 dm을 만든다.")
    fun sendDmNotRoom() {
        //given
        val sender_id = 1
        val receiver_id = 2
        val userNickname = "user"
        val content = "test"
        val room = RoomDto(
            1, "test", sender_id, receiver_id, LocalDateTime.now(), false, false,0,0,0,0
        )
        whenever(roomMapper.findRoom(sender_id,receiver_id)).thenReturn(null).thenReturn(room)
        whenever(roomMapper.findRoom(receiver_id,sender_id)).thenReturn(null)
        whenever(userMapper.findNicknameIdBy(receiver_id)).thenReturn("test")
        //when
        dmService.sendDm(userNickname,sender_id,receiver_id, content)

        //then
        verify(roomMapper, times(2)).findRoom(sender_id,receiver_id)
        verify(roomMapper, times(1)).findRoom(receiver_id,sender_id)
        verify(roomMapper, times(1)).createRoom(any());
        verify(dmMapper, times(1)).createDm(any())
        verify(roomMapper).updateRoomWhenSendDm(any(), eq(content), eq(true), eq(room.id));
    }

    @Test
    @DisplayName("roomId에 해당하는 대화방이 없는경우 예외를 반환한다.")
    fun getDmListNotRoom() {
        //given
        val meId = 1;
        val roomId = 1;
        val params = CursorDto(0,10);
        whenever(roomMapper.findRoomById(roomId)).thenReturn(null)

        //when
        val exception = assertThrows<NullPointerException> {
            dmService.getDmListWithCursorBasedPaging(meId, roomId, params.cursor,params.limit)
        }

        //then
        assertEquals("대화방이 존재하지 않습니다.", exception.message)
    }

    @Test
    @DisplayName("roomId에 해당하는 대화방이 있지만 로그인한 유저가 속한 대화방이 아닌경우 예외를 반환한다.")
    fun getDmListNotMyRoom() {
        //given
        val meId = 1;
        val roomId = 1;
        val params = CursorDto(0,10);
        val room = RoomDto(
            1, "test", 2, 3, LocalDateTime.now(), false, false,0,0,0,0
        )
        whenever(roomMapper.findRoomById(roomId)).thenReturn(room)

        //when
        val exception = assertThrows<NullPointerException> {
            dmService.getDmListWithCursorBasedPaging(meId, roomId, params.cursor,params.limit)
        }

        //then
        assertEquals("대화방이 존재하지 않습니다.", exception.message)
    }

    @Test
    @DisplayName("roomId에 해당하는 대화방이 있으면서 로그인한 유저가 sender로 만들어진 대화방이라면 dm list를 반환한다.")
    fun getDmListWithMeIsSender() {
        //given
        val meId = 1;
        val roomId = 1;
        val params = CursorDto(0,10);
        val isSender = true
        val room = RoomDto(
            1, "test", meId, 3, LocalDateTime.now(), false, false,0,0,0,0
        )
        val count = 10
        val list = listOf(ResultDmDto(1,true,"test",LocalDateTime.now(),false,roomId,VisibleChoices.BOTH))
        val otherName = "otherName"

        whenever(roomMapper.findRoomById(roomId)).thenReturn(room)
        whenever(dmMapper.findDmListWithCursorBasedPaging(isSender,roomId, params.cursor, 0, params.limit))
            .thenReturn(list)
        whenever(userMapper.findNicknameIdBy(room.receiver_id)).thenReturn(otherName)

        val expectResult = DmCursorPagingResponse(3,otherName,list,null)

        //when
        val result = dmService.getDmListWithCursorBasedPaging(meId, roomId, params.cursor,params.limit)


        //then
        assert(result.list == expectResult.list)
        assert(result.nextCursor == expectResult.nextCursor)
        assert(result.other_id == expectResult.other_id)
        assert(result.other_nickname == expectResult.other_nickname)

        verify(dmMapper).updateNotReadDm(roomId,!isSender)
        verify(dmMapper).updateSenderUnreadDmZero(roomId)
        verify(dmMapper).findLastDmId(roomId)
        verify(dmMapper).findDmListWithCursorBasedPaging(isSender,roomId, params.cursor, 0, params.limit)
        verify(userMapper).findNicknameIdBy(room.receiver_id)
    }

    @Test
    @DisplayName("roomId에 해당하는 대화방이 있으면서 로그인한 유저가 receiver로 만들어진 대화방이라면 dm list를 반환한다.")
    fun getDmListWithMeIsReceiver() {
        //given
        val meId = 1;
        val roomId = 1;
        val params = CursorDto(0,10);
        val isSender = false
        val room = RoomDto(
            1, "test", 3, meId, LocalDateTime.now(), false, false,0,0,0,0
        )
        val count = 10
        val list = listOf(ResultDmDto(1,true,"test",LocalDateTime.now(),false,roomId,VisibleChoices.BOTH))
        val otherName = "otherName"

        whenever(roomMapper.findRoomById(roomId)).thenReturn(room)
        whenever(dmMapper.findDmListWithCursorBasedPaging(isSender,roomId, params.cursor, 0, params.limit))
            .thenReturn(list)
        whenever(userMapper.findNicknameIdBy(room.sender_id)).thenReturn(otherName)

        val expectResult = DmCursorPagingResponse(3,otherName,list,null)

        //when
        val result = dmService.getDmListWithCursorBasedPaging(meId, roomId, params.cursor,params.limit)


        //then
        assert(result.list == expectResult.list)
        assert(result.nextCursor == expectResult.nextCursor)
        assert(result.other_id == expectResult.other_id)
        assert(result.other_nickname == expectResult.other_nickname)

        verify(dmMapper).updateNotReadDm(roomId,!isSender)
        verify(dmMapper).updateReceiverUnreadDmZero(roomId)
        verify(dmMapper).findLastDmId(roomId)
        verify(dmMapper).findDmListWithCursorBasedPaging(isSender,roomId, params.cursor, 0, params.limit)
        verify(userMapper).findNicknameIdBy(room.sender_id)
    }

    @Test
    @DisplayName("roomId에 해당하는 Room이 없는경우 예외를 반환한다.")
    fun deleteRoomWithNotRoom() {
        //given
        val meId = 1;
        val roomId = 1;
        whenever(roomMapper.findRoomById(roomId)).thenReturn(null)

        //when
        val exception = assertThrows<NullPointerException> {
            dmService.deleteRoom(meId, roomId)
        }

        //then
        assertEquals("대화방이 존재하지 않습니다.", exception.message)
    }

    @Test
    @DisplayName("roomId에 해당하는 대화방이 있지만 로그인한 유저가 속한 대화방이 아닌경우 예외를 반환한다.")
    fun deleteRoomWithNotMyRoom() {
        //given
        val meId = 1;
        val roomId = 1;
        val room = RoomDto(roomId,"test",2,3,LocalDateTime.now(),false,false,0,0,0,0)
        whenever(roomMapper.findRoomById(roomId)).thenReturn(room)

        //when
        val exception = assertThrows<NullPointerException> {
            dmService.deleteRoom(meId, roomId)
        }

        //then
        assertEquals("대화방이 존재하지 않습니다.", exception.message)
    }

    @Test
    @DisplayName("roomId에 해당하는 대화방이 로그인한 유저가 sender로 만들어진 대화방이라면 대화방을 삭제한다.")
    fun deleteRoomWithIsSender() {
        //given
        val meId = 1;
        val roomId = 1;
        val room = RoomDto(roomId,"test",meId,3,LocalDateTime.now(),false,false,0,0,0,0)
        whenever(roomMapper.findRoomById(roomId)).thenReturn(room)

        //when
        dmService.deleteRoom(meId,roomId);

        //then
        verify(roomMapper).updateWhenSenderIsDeleted(roomId)
    }

    @Test
    @DisplayName("roomId에 해당하는 대화방이 로그인한 유저가 receiver로 만들어진 대화방이라면 대화방을 삭제한다.")
    fun deleteRoomWithIsReceiver() {
        //given
        val meId = 1;
        val roomId = 1;
        val room = RoomDto(roomId,"test",2,meId,LocalDateTime.now(),false,false,0,0,0,0)
        whenever(roomMapper.findRoomById(roomId)).thenReturn(room)

        //when
        dmService.deleteRoom(meId,roomId);

        //then
        verify(roomMapper).updateWhenReceiverIsDeleted(roomId)
    }

}