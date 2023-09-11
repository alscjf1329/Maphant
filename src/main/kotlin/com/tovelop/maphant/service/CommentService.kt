package com.tovelop.maphant.service

import com.tovelop.maphant.dto.CommentDTO
import com.tovelop.maphant.dto.ReplyDTO
import com.tovelop.maphant.dto.UpdateCommentDTO
import com.tovelop.maphant.dto.CommentExtDTO
import com.tovelop.maphant.mapper.CommentMapper
import com.tovelop.maphant.type.paging.Pagination
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import org.springframework.stereotype.Service
import java.util.*
import javax.xml.stream.events.Comment

@Service
class CommentService(val commentMapper: CommentMapper) {
    fun findAllComment(boardId: Int, userId: Int, params: PagingDto) =
        commentMapper.findAllComment(boardId, userId, params)

    fun getCommentList(boardId: Int, userId: Int, params: PagingDto): PagingResponse<CommentExtDTO> {
        val count = commentMapper.getCommentCount(boardId)
        if (count < 1) {
            return PagingResponse(Collections.emptyList(), null)
        }
        val pagination = Pagination(count, params)
        val comments = commentMapper.findAllComment(boardId, userId, params)
        return PagingResponse(comments, pagination)
    }

    fun insertComment(commentDTO: CommentDTO) = commentMapper.insertComment(commentDTO)
    fun deleteComment(userId: Int, commentId: Int) = commentMapper.deleteComment(userId, commentId)
    fun updateComment(updateCommentDTO: UpdateCommentDTO) = commentMapper.updateComment(updateCommentDTO)
    fun insertCommentLike(userId: Int, commentId: Int) = commentMapper.insertCommentLike(userId, commentId)

    fun findCommentLike(userId: Int, commentId: Int) = commentMapper.findCommentLike(userId, commentId)

    fun cntCommentLike(commentId: Int) = commentMapper.cntCommentLike(commentId)

    fun deleteCommentLike(userId: Int, commentId: Int) = commentMapper.deleteCommentLike(userId, commentId)

    fun insertCommentReport(userId: Int, commentId: Int, reportId: Int) =
        commentMapper.insertCommentReport(userId, commentId, reportId)

    fun findCommentReport(commentId: Int) =
        commentMapper.findCommentReport(commentId)

    fun getCommentById(commentId: Int) = commentMapper.getCommentById(commentId)

    fun insertReply(replyDTO: ReplyDTO) = commentMapper.insertReply(replyDTO)

    fun changeState(commentId: Int, state: Int) = commentMapper.changeState(commentId, state)
    fun getBoardUserId(boardId: Int) = commentMapper.getBoardUserId(boardId)
}