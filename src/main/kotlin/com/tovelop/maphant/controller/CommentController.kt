package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.*
import com.tovelop.maphant.service.CommentService
import com.tovelop.maphant.service.FcmService
import com.tovelop.maphant.service.RateLimitingService
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.type.response.Response
import com.tovelop.maphant.type.response.ResponseUnit
import com.tovelop.maphant.utils.BadWordFiltering
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
    @Autowired private val fcmService: FcmService,
    @Autowired val rateLimitingService: RateLimitingService,
) {

    data class CommentRequest(
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
    ): ResponseEntity<Response<PagingResponse<FormatTimeDTO>>> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth is AnonymousAuthenticationToken) {
            return ResponseEntity.badRequest().body(Response.error("로그인 후 이용해주세요."))
        }
        val userId = (auth as TokenAuthToken).getUserId()
        if (commentService.findBoard(boardId) == null) {
            return ResponseEntity.badRequest().body(Response.error("존재하지 않는 게시글입니다."))
        }
        val comment = commentService.getCommentList(boardId, userId, pagingDto)
        val anonymous = commentService.getAnonymousListByBoardId(boardId)
        val anonymousKV = anonymous.map { it.user_id to it }.toMap()

        val commentTime = comment.list.map {
            var isMyComment = false
            if (it.user_id == userId) {
                isMyComment = true
            }
            if (it.is_anonymous && anonymousKV.containsKey(it.user_id)) {
                it.nickname = "익명" + anonymousKV[it.user_id]!!.rowNum
                it.user_id = null

            }
            if (it.modified_at == null) {
                it.timeFormat(it, it.created_at.formatTime(), isMyComment)
            } else {
                it.timeFormat(it, it.modified_at.formatTime() + "(수정됨)", isMyComment)
            }
        }
        val pagingResponse = PagingResponse(commentTime, comment.pagination)
        return ResponseEntity.ok().body(Response.success(pagingResponse))
    }


    @PostMapping("/insert")
    fun insertComment(@RequestBody commentDTO: setCommentDTO): ResponseEntity<ResponseUnit> {
        val userId = (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId()
        if (commentService.findBoard(commentDTO.board_id) == null) {
            return ResponseEntity.badRequest().body(Response.error("존재하지 않는 게시글입니다."))
        }
        if (rateLimitingService.isBanned(userId)) {
            return ResponseEntity.badRequest().body(Response.error("차단된 사용자입니다."))
        } else {
            rateLimitingService.requestCheck(userId, "WRITE_COMMENT")
        }
        if (commentDTO.body.isBlank()) {
            return ResponseEntity.badRequest().body(Response.error("댓글 내용을 입력해주세요."))
        }
        if (commentDTO.body.length > 255) {
            return ResponseEntity.badRequest().body(Response.error("댓글은 255자 이내로 작성해주세요."))
        }

        if (commentDTO.parent_id != null) {
            if (commentService.getCommentById(commentDTO.parent_id ?: 0) == null) {
                return ResponseEntity.badRequest().body(Response.error("존재하지 않는 댓글입니다."))
            }
            val id = commentService.getCommentById(commentDTO.parent_id)
            commentDTO.board_id = id!!.board_id
        }

        commentService.findBoard(commentDTO.board_id)?.let {
            if (it.state == 1) {
                return ResponseEntity.badRequest().body(Response.error("삭제된 게시글입니다."))
            }
            if (it.state == 2 || it.state == 3) {
                return ResponseEntity.badRequest().body(Response.error("차단된 게시글입니다."))
            }
        }

        commentService.getCommentById(commentDTO.parent_id ?: 0)?.let {
            if (it.state == 1) {
                return ResponseEntity.badRequest().body(Response.error("삭제된 댓글입니다."))
            }
            if (it.state == 2 || it.state == 3) {
                return ResponseEntity.badRequest().body(Response.error("차단된 댓글입니다."))
            }
        }

        val badWordFiltering = BadWordFiltering()
        commentDTO.body = badWordFiltering.filterBadWords(commentDTO.body)

        commentService.insertComment(commentDTO.toCommentDTO(userId))
        fcmService.send(
            FcmMessageDTO(
                commentService.getBoardUserId(commentDTO.board_id),
                "댓글이 달렸습니다.",
                commentDTO.body,
                mutableMapOf(
                    "type" to "comment",
                    "boardId" to commentDTO.board_id.toString(),
                    "commentId" to commentDTO.id.toString(),
                    "board_type" to commentService.findBoard(commentDTO.board_id)!!.typeId.toString(),
                )
            )
        )
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }


    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Int): ResponseEntity<ResponseUnit> {
        val current = commentService.getCommentById(commentId) ?: return ResponseEntity.badRequest()
            .body(Response.error("존재하지 않는 댓글입니다."))

        val userId = (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId()
        if (current.user_id != userId) {
            return ResponseEntity.badRequest().body(Response.error("댓글 작성자만 삭제할 수 있습니다."))
        }
        if (current.state == 1) {
            return ResponseEntity.badRequest().body(Response.error("삭제된 댓글입니다."))
        }
        commentService.deleteComment(userId, commentId)
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    @PostMapping("/update")
    fun updateComment(@RequestBody updateCommentDTO: UpdateCommentDTO): ResponseEntity<ResponseUnit> {
        val current = commentService.getCommentById(updateCommentDTO.id)!!
        val userId = (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId()

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
        val badWordFiltering = BadWordFiltering()
        updateCommentDTO.body = badWordFiltering.filterBadWords(updateCommentDTO.body)
        commentService.updateComment(updateCommentDTO)
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    @PostMapping("/like")
    fun changeCommentLike(@RequestBody commentRequest: CommentRequest): ResponseEntity<Response<String>> {
        val userId = (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId()
        val comment = commentService.getCommentById(commentRequest.commentId) ?: return ResponseEntity.badRequest()
            .body(Response.error("존재하지 않는 댓글입니다."))
        if (comment.state == 1) {
            return ResponseEntity.badRequest().body(Response.error("삭제된 댓글입니다."))
        }
        if (comment.state == 2 || comment.state == 3) {
            return ResponseEntity.badRequest().body(Response.error("차단된 댓글입니다."))
        }
        return if (commentService.findCommentLike(
                userId,
                commentRequest.commentId
            ) != emptyList<CommentLikeDTO>()
        ) {
            commentService.deleteCommentLike(userId, commentRequest.commentId)
            ResponseEntity.ok().body(Response.success("좋아요를 취소했습니다."))
        } else {
            commentService.insertCommentLike(userId, commentRequest.commentId)
            ResponseEntity.ok().body(Response.success("좋아요를 눌렀습니다."))
        }
    }

    @GetMapping("/like")
    fun findCommentLike(@RequestBody commentRequest: CommentRequest): ResponseEntity<Response<List<CommentLikeDTO>?>> {
        val userId = (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId()
        val comment = commentService.findCommentLike(userId, commentRequest.commentId)
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
        val userId = (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId()
        val comment = commentService.getCommentById(commentRequest.commentId) ?: return ResponseEntity.badRequest()
            .body(Response.error("존재하지 않는 댓글입니다."))
        if (comment.state == 1) {
            return ResponseEntity.badRequest().body(Response.error("삭제된 댓글입니다."))
        }
        if (comment.state == 2 || comment.state == 3) {
            return ResponseEntity.badRequest().body(Response.error("차단된 댓글입니다."))
        }
        if (comment.user_id == userId) {
            return ResponseEntity.badRequest().body(Response.error("자신의 댓글은 신고할 수 없습니다."))
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
        commentService.insertCommentReport(userId, commentRequest.commentId, commentRequest.reportId)
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