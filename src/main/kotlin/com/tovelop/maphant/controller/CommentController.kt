package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.*
import com.tovelop.maphant.service.CommentService
import com.tovelop.maphant.service.FcmService
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.type.response.Response
import com.tovelop.maphant.type.response.ResponseUnit
import com.tovelop.maphant.utils.FormatterHelper.Companion.formatTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/comment")
class CommentController(
    @Autowired val commentService: CommentService,
    @Autowired private val fcmService: FcmService
) {


    data class CommentRequest(
        val userId: Int,
        val commentId: Int,
        val reportId: Int?,
    )

    data class ReportRequest(
        val commentId: Int,
    )

    @GetMapping("/list/{boardId}")
    fun findAllComment(
        @ModelAttribute pagingDto: PagingDto,
        @PathVariable boardId: Int,
    ): ResponseEntity<Response<List<FormatTimeDTO>>> {
        val auth = SecurityContextHolder.getContext().authentication

        if (auth is AnonymousAuthenticationToken) {
            return ResponseEntity.badRequest().body(Response.error("로그인 후 이용해주세요."))
        }
        val userId = (auth as TokenAuthToken).getUserId()
        val comment = commentService.getCommentList(boardId, userId, pagingDto)

        if (boardId < 1 || comment.list.isEmpty()) {
            return ResponseEntity.badRequest().body(Response.error("게시글을 찾을 수 없습니다."))
        }


        val commentTime = comment.list.map {
            if (it.is_anonymous) {
//                it.user_id = 0
                it.nickname = "익명"
            }
            if (it.modified_at == null) {
                it.timeFormat(it, it.created_at.formatTime())
            } else {
                it.timeFormat(it, it.modified_at.formatTime() + "(수정됨)")
            }
        }
        return ResponseEntity.ok().body(Response.success(commentTime))
    }

    @PostMapping("/insert")
    fun insertComment(@RequestBody commentDTO: CommentDTO): ResponseEntity<ResponseUnit> {
        SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (commentDTO.body.isBlank()) {
            return ResponseEntity.badRequest().body(Response.error("댓글 내용을 입력해주세요."))
        }
        if (commentDTO.body.length > 255) {
            return ResponseEntity.badRequest().body(Response.error("댓글은 255자 이내로 작성해주세요."))
        }
        commentService.insertComment(commentDTO)
        fcmService.send(
            FcmMessageDTO(
                commentService.getBoardUserId(commentDTO.board_id),
                "댓글이 달렸습니다",
                commentDTO.body
            )
        )
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    @PostMapping("/reply")
    fun replyComment(@RequestBody replyDTO: ReplyDTO): ResponseEntity<ResponseUnit> {
        SecurityContextHolder.getContext().authentication as TokenAuthToken
        val rep = replyDTO.body
        if (rep.isBlank()) {
            return ResponseEntity.badRequest().body(Response.error("댓글 내용을 입력해주세요."))
        }
        if (rep.length > 255) {
            return ResponseEntity.badRequest().body(Response.error("댓글은 255자 이내로 작성해주세요."))
        }
        commentService.insertReply(replyDTO)
        fcmService.send(
            FcmMessageDTO(
                commentService.getBoardUserId(replyDTO.parent_id),
                "댓글이 달렸습니다",
                replyDTO.body
            )
        )
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Int): ResponseEntity<ResponseUnit> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        val current = commentService.getCommentById(commentId)!!
        val userId = auth.getUserId()
        if (current.user_id != userId) {
            return ResponseEntity.badRequest().body(Response.error("댓글 작성자만 삭제할 수 있습니다."))
        }
        if (current.state == 1) {
            return ResponseEntity.badRequest().body(Response.error("존재하지 않는 댓글입니다."))
        }
        commentService.deleteComment(userId, commentId)
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    @PostMapping("/update")
    fun updateComment(@RequestBody updateCommentDTO: UpdateCommentDTO): ResponseEntity<ResponseUnit> {
        val current = commentService.getCommentById(updateCommentDTO.id)!!
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        val userId = auth.getUserId()

        if (current.user_id != userId) {
            return ResponseEntity.badRequest().body(Response.error("댓글 작성자만 수정할 수 있습니다."))
        }
        if (current.state == 1) {
            return ResponseEntity.badRequest().body(Response.error("존재하지 않는 댓글입니다."))
        }
        if (updateCommentDTO.body.isBlank()) {
            return ResponseEntity.badRequest().body(Response.error("댓글 내용을 입력해주세요."))
        }
        if (updateCommentDTO.body.length > 255) {
            return ResponseEntity.badRequest().body(Response.error("댓글은 255자 이내로 작성해주세요."))
        }
//        if (commentService.getCommentById(updateCommentDTO.id)!!.is_anonymous != updateCommentDTO.is_anonymous) {
//            return ResponseEntity.badRequest().body(Response.error("댓글 익명 여부는 수정할 수 없습니다."))
//        }
        commentService.updateComment(updateCommentDTO)
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    @PostMapping("/like")
    fun changeCommentLike(@RequestBody commentRequest: CommentRequest): ResponseEntity<Response<String>> {
        SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (commentService.getCommentById(commentRequest.commentId) == null) {
            return ResponseEntity.badRequest().body(Response.error("존재하지 않는 댓글입니다."))
        }
        return if (commentService.findCommentLike(
                commentRequest.userId,
                commentRequest.commentId
            ) != emptyList<CommentLikeDTO>()
        ) {
            commentService.deleteCommentLike(commentRequest.userId, commentRequest.commentId)
            ResponseEntity.ok().body(Response.success("좋아요를 취소했습니다."))
        } else {
            commentService.insertCommentLike(commentRequest.userId, commentRequest.commentId)
            ResponseEntity.ok().body(Response.success("좋아요를 눌렀습니다."))
        }
    }

    @GetMapping("/like")
    fun findCommentLike(@RequestBody commentRequest: CommentRequest): ResponseEntity<Response<List<CommentLikeDTO>?>> {
        val comment = commentService.findCommentLike(commentRequest.userId, commentRequest.commentId)
        if (commentService.getCommentById(commentRequest.commentId) == null) {
            return ResponseEntity.badRequest().body(Response.error("존재하지 않는 댓글입니다."))
        }
        return ResponseEntity.ok().body(Response.success(comment))
    }

    @GetMapping("/cnt-like/{commentId}")
    fun cntCommentLike(@PathVariable commentId: Int): ResponseEntity<Response<Int>> {
        SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (commentService.getCommentById(commentId) == null) {
            return ResponseEntity.badRequest().body(Response.error("존재하지 않는 댓글입니다."))
        }
        commentService.cntCommentLike(commentId)
        return ResponseEntity.ok().body(Response.success(commentService.cntCommentLike(commentId)))
    }

    @PostMapping("/report")
    fun insertCommentReport(@RequestBody commentRequest: CommentRequest): ResponseEntity<ResponseUnit> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (commentService.getCommentById(commentRequest.commentId) == null) {
            return ResponseEntity.badRequest().body(Response.error("존재하지 않는 댓글입니다."))
        }
        if (commentService.findCommentReport(
                commentRequest.commentId
            ) != emptyList<CommentReportDTO>()
        ) {
            return ResponseEntity.badRequest().body(Response.error("이미 신고한 댓글입니다."))
        }
        if (commentRequest.reportId == null) {
            return ResponseEntity.badRequest().body(Response.error("신고 사유를 선택해주세요."))
        }
        commentService.insertCommentReport(auth.getUserId(), commentRequest.commentId, commentRequest.reportId)
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    @GetMapping("/report")
    fun findCommentReport(@RequestBody reportRequest: ReportRequest): ResponseEntity<Response<List<CommentReportDTO>?>> {
        if (commentService.getCommentById(reportRequest.commentId) == null) {
            return ResponseEntity.badRequest().body(Response.error("존재하지 않는 댓글입니다."))
        }
        val comment =
            commentService.findCommentReport(reportRequest.commentId)
        return ResponseEntity.ok().body(Response.success(comment))
    }
}