package com.tovelop.maphant.service

import com.tovelop.maphant.dto.BoardNoticeDTO
import com.tovelop.maphant.mapper.BoardNoticeMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BoardNoticeService(@Autowired val boardNoticeMapper: BoardNoticeMapper) {
    fun insertNotice(boardNoticeDTO: BoardNoticeDTO) {
        boardNoticeMapper.insertNotice(boardNoticeDTO)
    }
}