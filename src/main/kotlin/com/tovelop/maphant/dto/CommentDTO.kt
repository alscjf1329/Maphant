package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class CommentDTO(
    val id: Int,
    val user_id: Int,
    val parent_id: Int?,
    val board_id: Int,
    val body: String,
    val is_anonymous: Boolean,
    val created_at: LocalDateTime?,
    val modified_at: LocalDateTime?,
    val like_cnt: Int,
    val state: Int,
)

data class setCommentDTO(
    val id: Int,
    val parent_id: Int?,
    var board_id: Int,
    var body: String,
    val is_anonymous: Boolean,
    val created_at: LocalDateTime?,
    val modified_at: LocalDateTime?,
    val like_cnt: Int,
    val state: Int,
) {
    fun toCommentDTO(user_id: Int): CommentDTO {
        return CommentDTO(
            id = id,
            parent_id = parent_id,
            user_id = user_id,
            board_id = board_id,
            body = body,
            is_anonymous = is_anonymous,
            created_at = created_at,
            modified_at = modified_at,
            like_cnt = like_cnt,
            state = state,
        )
    }
}

data class CommentRESDTO(
    val id: Int,
    val parent_id: Int?,
    var nickname: String,
    val boardtype_id: Int,
    val board_type: String,
    val board_id: Int,
    val board_title: String,
    val body: String,
    val is_anonymous: Boolean,
    val created_at: LocalDateTime,
    val modified_at: LocalDateTime?,
    val like_cnt: Int,
    val comment_id: Int?,
    val isMyComment: Boolean,
)

data class CommentExtDTO(
    val id: Int,
    val parent_id: Int?,
    val user_id: Int?,
    var nickname: String,
    val boardtype_id: Int,
    val board_type: String,
    val board_id: Int,
    val board_title: String,
    val body: String,
    val is_anonymous: Boolean,
    val created_at: LocalDateTime,
    val modified_at: LocalDateTime?,
    val like_cnt: Int,
    val comment_id: Int?,
) {
    fun timeFormat(comment: CommentExtDTO, time: String, authId: Int): FormatTimeDTO {
        return FormatTimeDTO(
            id = comment.id,
            parent_id = comment.parent_id,
            user_id = comment.user_id,
            nickname = comment.nickname,
            boardtype_id = comment.boardtype_id,
            board_type = comment.board_type,
            board_id = comment.board_id,
            board_title = comment.board_title,
            body = comment.body,
            is_anonymous = comment.is_anonymous,
            created_at = comment.created_at,
            modified_at = comment.modified_at,
            like_cnt = comment.like_cnt,
            comment_id = comment.comment_id,
            time = time,
            isMyComment = comment.user_id == authId,
        )
    }
}

data class ReplyDTO(
    val id: Int,
    val user_id: Int,
    val parent_id: Int,
    val board_id: Int,
    val body: String,
    val is_anonymous: Boolean,
    val created_at: LocalDateTime?,
    val like_cnt: Int,
    val state: Int,
)

data class CommentLikeDTO(
    val user_id: Int,
    val comment_id: Int,
)

data class CommentReportDTO(
    val user_id: Int,
    val report_id: Int,
)

data class UpdateCommentDTO(
    val id: Int,
    var body: String,
    val modified_at: LocalDateTime?,
)

data class FormatTimeDTO(
    val id: Int,
    val parent_id: Int?,
    val user_id: Int?,
    val nickname: String,
    val boardtype_id: Int,
    val board_type: String,
    val board_id: Int,
    val board_title: String,
    val body: String,
    val is_anonymous: Boolean,
    val created_at: LocalDateTime,
    val modified_at: LocalDateTime?,
    val like_cnt: Int,
    val comment_id: Int?,
    val time: String,
    val isMyComment: Boolean = false,
)

data class AnonymousListDTO(
    val user_id: Int,
    val rowNum: Int,
)