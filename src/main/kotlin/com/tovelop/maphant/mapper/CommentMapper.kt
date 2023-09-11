package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.CommentDTO
import com.tovelop.maphant.dto.CommentExtDTO
import com.tovelop.maphant.dto.CommentLikeDTO
import com.tovelop.maphant.dto.CommentReportDTO
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.dto.*
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
interface CommentMapper {
    fun findAllComment(boardId: Int, userId: Int, params: PagingDto): List<CommentExtDTO>
    fun findAllCommentByUser(userId: Int, params: PagingDto): List<CommentExtDTO>
    fun insertComment(commentDTO: CommentDTO)
    fun deleteComment(userId: Int, commentId: Int)
    fun updateComment(updateCommentDTO: UpdateCommentDTO)
    fun insertCommentLike(userId: Int, commentId: Int)
    fun findCommentLike(userId: Int, commentId: Int): List<CommentLikeDTO>?
    fun cntCommentLike(commentId: Int): Int
    fun deleteCommentLike(userId: Int, commentId: Int)
    fun insertCommentReport(userId: Int, commentId: Int, reportId: Int)
    fun findCommentReport(commentId: Int): List<CommentReportDTO>?
    fun getCommentById(commentId: Int): CommentDTO?
    fun getCommentCount(boardId: Int): Int
    fun insertReply(replyDTO: ReplyDTO)
    fun changeState(commentId: Int, state: Int)
    fun getBoardUserId(boardId: Int): Int
}