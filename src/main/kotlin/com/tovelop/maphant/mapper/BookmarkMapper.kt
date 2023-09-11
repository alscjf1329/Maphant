package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.BookmarkDTO
import com.tovelop.maphant.type.paging.PagingDto
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface BookmarkMapper {

    fun insertBoard(userId: Int, boardId: Int)

    fun selectBoardAllById(userId: Int, params: PagingDto): List<BookmarkDTO>

    fun getBoardCount(userId: Int): Int

    fun deleteBoardById(userId: Int, boardId: Int): Int

    fun isBookmarked(userId: Int, boardId: Int): Boolean?
}