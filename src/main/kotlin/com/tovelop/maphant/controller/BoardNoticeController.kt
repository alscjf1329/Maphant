package com.tovelop.maphant.controller

import com.tovelop.maphant.dto.BoardNoticeDTO
import com.tovelop.maphant.dto.UpdateBoardNoticeDTO
import com.tovelop.maphant.service.BoardNoticeService
import com.tovelop.maphant.type.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/notice")
class BoardNoticeController(@Autowired val boardNoticeService: BoardNoticeService) {

    @GetMapping("/{id}")
    fun getNotice(@PathVariable("id") noticeId: Int): ResponseEntity<Any> {
        val notice =
            boardNoticeService.findNotice(noticeId) ?: return ResponseEntity.badRequest().body("존재하지 않거나 삭제된 공지입니다.")
        return ResponseEntity.ok().body(notice)
    }

    @PostMapping("/create")
    fun insertNotice(@RequestBody boardNoticeDTO: BoardNoticeDTO): ResponseEntity<Any> {
        boardNoticeService.insertNotice(boardNoticeDTO)
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    @PutMapping("/update")
    fun updateNotice(@RequestBody updateBoardNoticeDTO: UpdateBoardNoticeDTO): ResponseEntity<Any> {
        val updateResult = boardNoticeService.updateNotice(updateBoardNoticeDTO)
        if (updateResult == 0)
            return ResponseEntity.badRequest().body(Response.error<Any>("수정에 실패했습니다. 아이디를 확인하세요."))
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    @DeleteMapping("/{noticeId}")
    fun deleteNotice(@PathVariable("noticeId") noticeId: Int): ResponseEntity<Any> {
        val deleteResult = boardNoticeService.deleteNotice(noticeId)
        if (deleteResult == 0)
            return ResponseEntity.badRequest().body(Response.error<Any>("삭제에 실패했습니다. 아이디를 확인하세요."))
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    @GetMapping("/all")
    fun findNoticeAll(): ResponseEntity<Any> {
        val noticeResult =
            boardNoticeService.findNoticeList() ?: return ResponseEntity.badRequest().body("불러 오는데 실패했습니다.")
        return ResponseEntity.ok().body(noticeResult)
    }
}