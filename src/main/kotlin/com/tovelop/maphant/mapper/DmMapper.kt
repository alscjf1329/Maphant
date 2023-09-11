package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.DmDto
import com.tovelop.maphant.dto.ResultDmDto
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.dto.VisibleChoices
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface DmMapper {
    fun createDm(dmDto: DmDto): Int
    fun findDmList(room_id: Int, vararg visible: VisibleChoices): List<DmDto>

    fun findDmListWithPaging(
        isSender: Boolean,
        room_id: Int,
        params: PagingDto,
        vararg visible: VisibleChoices
    ): List<ResultDmDto>

    fun updateNotReadDm(room_id: Int, is_from_sender: Boolean): Boolean

    fun updateSenderUnreadDmZero(room_id: Int)

    fun updateReceiverUnreadDmZero(room_id: Int)

    //roomId에 해당하는 대화방의 Dm객체의 oldVisible을 newVisible로 바꿔준다
    fun updateDmVisible(room_id: Int, oldVisible: VisibleChoices, newVisible: VisibleChoices): Boolean

    fun findDmCount(room_id: Int, vararg visible: VisibleChoices): Int
}