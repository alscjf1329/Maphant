package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.BoardSearchResponseDto
import com.tovelop.maphant.mapper.BoardMapper
import com.tovelop.maphant.service.SearchService
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.type.response.Response
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/search")
class SearchController(private val searchService: SearchService,
                       private val boardMapper: BoardMapper) {

    @GetMapping("/boards")
    fun search(@RequestParam search:String,
                @RequestParam boardTypeId:Int?,
                @ModelAttribute pagingDto: PagingDto,
                @RequestHeader("x-category") category: Int): ResponseEntity<Response<PagingResponse<BoardSearchResponseDto>>> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        val userId = auth.getUserId()

        if(search.length<2) throw IllegalStateException("두 글자 이상 검색하세요")

        return ResponseEntity.ok().body(Response.success(
            searchService.search(search,userId,category,boardTypeId,pagingDto)
        ))
    }

    @GetMapping("init")
    fun init() {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        val userId = auth.getUserId()
        if(userId != 1) return
        val boards = boardMapper.findAllBoards()

        boards?.forEach { board -> searchService.create(board.boardId, board.title, board.body, board.tags) }
    }
}