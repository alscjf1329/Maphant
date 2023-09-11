package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.PollDTO
import com.tovelop.maphant.dto.PollInfoDTO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface PollMapper {
    fun insertPoll(poll: PollDTO)

    fun insertPollOption(pollId: Int, option: String)

    fun insertPollUser(userId: Int, pollId: Int, pollOption: Int)

    fun updatePollUser(userId: Int, pollId: Int, pollOption: Int)

    fun prevOptionId(userId: Int, pollId: Int): Int

    fun decreaseCount(optionId: Int, pollId: Int)

    fun updateCount(optionId: Int, pollId: Int)

    fun getPollIdByBoardId(boardId: Int): Int?

    fun selectPollInfoById(pollId: Int, userId: Int): PollInfoDTO

    fun selectPollInfo(pollId: Int): PollInfoDTO

    fun selectPollInfoByBoardId(boardId: Int, userId: Int): PollInfoDTO

    fun isPollOption(pollId: Int, pollOptionId: Int): Int

    fun isExistencePollByBoardId(boardId: Int): Int

    fun deletePollByBoardId(boardId: Int): Int

    fun deletePollByPollId(pollId: Int): Int

    fun closePollByBoardId(boardId: Int): Int

    fun closePollByPollId(pollId: Int): Int

    fun isPollUserByPollId(userId: Int, pollId: Int): Int

    fun isPollUserByBoardId(userId: Int, boardId: Int): Int

    fun isPolledUser(userId: Int, pollId: Int): Int?
}