package com.tovelop.maphant.service

import com.tovelop.maphant.dto.PollDTO
import com.tovelop.maphant.dto.PollInfoDTO
import com.tovelop.maphant.mapper.PollMapper
import com.tovelop.maphant.type.response.Response
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PollService(val pollMapper: PollMapper) {

    fun increaseOptionCount(userId: Int, pollId: Int, pollOption: Int): Boolean {
        try {
            pollMapper.insertPollUser(userId, pollId, pollOption)
            pollMapper.updateCount(pollOption, pollId)
        } catch (e: Exception) {
            if (e.cause is java.sql.SQLIntegrityConstraintViolationException) {
                val prevOptionId = pollMapper.prevOptionId(userId, pollId)
                pollMapper.decreaseCount(prevOptionId, pollId)
                pollMapper.updatePollUser(userId, pollId, pollOption)
                pollMapper.updateCount(pollOption, pollId)
            } else {
                return false
            }
        }
        return true
    }

    fun createPoll(poll: PollDTO): ResponseEntity<Any> {
        if (!isExistencePollByBoardId(poll.boardId)) {
            pollMapper.insertPoll(poll)
            poll.options.forEach { pollMapper.insertPollOption(poll.id!!, it) }
            return ResponseEntity.ok().body(Response.stateOnly(true))
        } else {
            return ResponseEntity.badRequest().body(Response.error<Any>("투표가 이미 존재합니다."))
        }
    }

    fun getPoll(pollId: Int): Result<PollInfoDTO> {
        // 투표 현황 조회, 투표 하지 않았을 때 사용
        return Result.runCatching { pollMapper.selectPollInfo(pollId) }
    }

    fun getPollIdByBoardId(boardId: Int): Int? {
        return pollMapper.getPollIdByBoardId(boardId)
    }

    fun getPollByBoardId(boardId: Int, userId: Int): Result<PollInfoDTO> {
        return Result.runCatching { pollMapper.selectPollInfoByBoardId(boardId, userId) }
    }

    fun getPollByPollId(pollId: Int, userId: Int): Result<PollInfoDTO> {
        return Result.runCatching { pollMapper.selectPollInfoById(pollId, userId) }
    }

    fun isPollOption(pollId: Int, pollOptionId: Int): Boolean {
        return Result.runCatching { pollMapper.isPollOption(pollId, pollOptionId) }.isSuccess
    }

    fun isExistencePollByBoardId(boardId: Int): Boolean {
        return Result.runCatching { pollMapper.isExistencePollByBoardId(boardId) }.isSuccess
    }

    fun deletePollByBoardId(boardId: Int): ResponseEntity<Any> {
        val deletedPollCount = pollMapper.deletePollByBoardId(boardId)
        if (deletedPollCount == 0) {
            return ResponseEntity.badRequest().body(Response.error<Any>("투표가 존재하지 않습니다."))
        }
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    fun deletePollByPollId(pollId: Int): ResponseEntity<Any> {
        val deletedPollCount = pollMapper.deletePollByPollId(pollId)
        if (deletedPollCount == 0) {
            return ResponseEntity.badRequest().body(Response.error<Any>("투표가 존재하지 않습니다."))
        }
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    fun closePollByBoardId(boardId: Int): ResponseEntity<Any> {
        val closedPollCount = pollMapper.closePollByBoardId(boardId)
        if (closedPollCount == 0) {
            return ResponseEntity.badRequest().body(Response.error<Any>("존재하지 않거나 마감된 투표입니다."))
        }
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    fun closePollByPollId(pollId: Int): ResponseEntity<Any> {
        val closedPollCount = pollMapper.closePollByPollId(pollId)
        if (closedPollCount == 0) {
            return ResponseEntity.badRequest().body(Response.error<Any>("존재하지 않거나 마감된 투표입니다."))
        }
        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    fun isPollUserByPollId(userId: Int, pollId: Int): Boolean {
        return Result.runCatching { pollMapper.isPollUserByPollId(userId, pollId) }.isSuccess
    }

    fun isPollUserByBoardId(userId: Int, boardId: Int): Boolean {
        return Result.runCatching { pollMapper.isPollUserByBoardId(userId, boardId) }.isSuccess
    }

    fun isPolledUser(userId: Int, pollId: Int): Int {
        return pollMapper.isPolledUser(userId, pollId) ?: 0
    }
}