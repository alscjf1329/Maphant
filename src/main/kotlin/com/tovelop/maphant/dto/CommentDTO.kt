package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class CommentDTO(
    val id: Int,
    val user_id: Int,
    val board_id: Int,
    val body: String,
    val is_anonymous: Boolean,
    val created_at: LocalDateTime?,
    val modified_at: LocalDateTime?,
    val like_cnt: Int,
    val state: Int,
)

data class CommentExtDTO(
    val id: Int,
    val user_id: Int,
    var nickname: String,
    val board_id: Int,
    val body: String,
    val is_anonymous: Boolean,
    val created_at: LocalDateTime,
    val modified_at: LocalDateTime?,
    val like_cnt: Int,
    val comment_id: Int?
) {
    fun timeFormat(comment: CommentExtDTO, time: String): FormatTimeDTO {
        return FormatTimeDTO(
            id = comment.id,
            user_id = comment.user_id,
            nickname = comment.nickname,
            board_id = comment.board_id,
            body = comment.body,
            is_anonymous = comment.is_anonymous,
            created_at = comment.created_at,
            modified_at = comment.modified_at,
            like_cnt = comment.like_cnt,
            comment_id = comment.comment_id,
            time = time
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
    val body: String,
    val modified_at: LocalDateTime?,
)

data class FormatTimeDTO(
    val id: Int,
    val user_id: Int,
    val nickname: String,
    val board_id: Int,
    val body: String,
    val is_anonymous: Boolean,
    val created_at: LocalDateTime,
    val modified_at: LocalDateTime?,
    val like_cnt: Int,
    val comment_id: Int?,
    val time: String,
)