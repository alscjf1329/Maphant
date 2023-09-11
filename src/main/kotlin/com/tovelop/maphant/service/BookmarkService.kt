package com.tovelop.maphant.service

import com.tovelop.maphant.dto.BookmarkDTO
import com.tovelop.maphant.mapper.BookmarkMapper
import com.tovelop.maphant.type.paging.Pagination
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookmarkService(@Autowired val bookmarkMapper: BookmarkMapper) {

    fun insert(userId: Int, boardId: Int): Boolean {
        try {
            bookmarkMapper.insertBoard(userId, boardId)
        } catch (e: Exception) {
            //이미 추가 됐거나, 북마크 전에 삭제된 경우
            return false
        }
        return true
    }

    fun showBookmarks(userId: Int, params: PagingDto): PagingResponse<BookmarkDTO> {
        val count = bookmarkMapper.getBoardCount(userId)
        if (count < 1) {
            return PagingResponse(Collections.emptyList(), null)
        }
        val pagination = Pagination(count, params)
        val bookmarks = bookmarkMapper.selectBoardAllById(userId, params)
        return PagingResponse(bookmarks, pagination)
    }

    fun deleteBookmark(userId: Int, boardId: Int): Int {
        return bookmarkMapper.deleteBoardById(userId, boardId)
    }

    fun isBookmarked(userId: Int, boardId: Int): Boolean {
        return bookmarkMapper.isBookmarked(userId, boardId) ?: false
    }
}