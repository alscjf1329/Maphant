package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.BoardNoticeDTO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface BoardNoticeMapper {
    fun insertNotice(boardNoticeDTO: BoardNoticeDTO)
}