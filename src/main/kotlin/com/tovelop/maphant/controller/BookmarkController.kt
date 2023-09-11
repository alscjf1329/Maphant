package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.BookmarkDTO
import com.tovelop.maphant.service.BookmarkService
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.type.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/bookmark")
class BookmarkController(@Autowired val bookmarkService: BookmarkService) {

    //북마크 추가 api
    @PostMapping("/{boardId}")
    fun bookmark(@PathVariable("boardId") boardId: Int): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken

        val bookmarkResult = bookmarkService.insert(auth.getUserId(), boardId)
        if (!bookmarkResult) {
            return ResponseEntity.badRequest().body(
                Response.error<Any>
                    ("이미 등록된 북마크거나 게시물이 삭제되었습니다.")
            )
        }
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    //나의 북마크 보기
    @GetMapping("/my-list")
    fun showBookmarks(@ModelAttribute pagingDto: PagingDto): ResponseEntity<Response<PagingResponse<BookmarkDTO>>> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth == null || auth !is TokenAuthToken || !auth.isAuthenticated) {
            return ResponseEntity.badRequest().body(Response.error("로그인 안됨."))
        }
        val bookmarkList = bookmarkService.showBookmarks(auth.getUserId(), pagingDto)

        val pagingResponse = PagingResponse(bookmarkList.list, bookmarkList.pagination)

        return ResponseEntity.ok().body(Response.success(pagingResponse))
    }

    @DeleteMapping("/{boardId}")
    fun deleteBookmarks(@PathVariable("boardId") boardId: Int): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        val deleteResult = bookmarkService.deleteBookmark(auth.getUserId(), boardId)
        if (deleteResult == 0) {
            return ResponseEntity.badRequest().body(Response.error<Any>("북마크 삭제에 실패했습니다. 아이디를 확인하세요."))
        }

        return ResponseEntity.ok().body(Response.stateOnly(true))
    }
}