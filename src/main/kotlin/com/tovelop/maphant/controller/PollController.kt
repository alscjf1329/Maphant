package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.PollDTO
import com.tovelop.maphant.service.PollService
import com.tovelop.maphant.type.response.Response
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/poll")
class PollController(val pollService: PollService) {

    @GetMapping("/{board_id}")
    @ResponseBody
    fun getPoll(@PathVariable("board_id") boardId: Int) =
        ResponseEntity.ok().body(Response.success(mutableMapOf("poll_id" to pollService.getPollIdByBoardId(boardId))))

    @PostMapping("/") // 투표 생성
    fun createPoll(@RequestBody poll: PollDTO): ResponseEntity<Any> {
        return pollService.createPoll(poll)
    }

    @PostMapping("/close/{poll_id}")
    fun closePollByPollId(@PathVariable("poll_id") pollId: Int): ResponseEntity<Any> {
        if (!pollService.isPollUserByPollId(
                (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId(),
                pollId
            )
        ) {
            return ResponseEntity.badRequest().body(Response.error<Any>("투표를 생성한 사람만 투표를 종료할 수 있습니다."))
        }
        return pollService.closePollByPollId(pollId)
    }

    @PostMapping("/close/board/{board_id}")
    fun closePollByBoardId(@PathVariable("board_id") boardId: Int): ResponseEntity<Any> {
        if (!pollService.isPollUserByBoardId(
                (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId(),
                boardId
            )
        ) {
            return ResponseEntity.badRequest().body(Response.error<Any>("투표를 생성한 사람만 투표를 종료할 수 있습니다."))
        }
        return pollService.closePollByBoardId(boardId)
    }

    @DeleteMapping("/{poll_id}")
    fun deletePollByPollId(@PathVariable("poll_id") pollId: Int): ResponseEntity<Any> {
        if (!pollService.isPollUserByPollId(
                (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId(),
                pollId
            )
        ) {
            return ResponseEntity.badRequest().body(Response.error<Any>("투표를 생성한 사람만 투표를 삭제할 수 있습니다."))
        }
        return pollService.deletePollByPollId(pollId)
    }

    @DeleteMapping("/board/{board_id}")
    fun deletePollByBoardId(@PathVariable("board_id") boardId: Int): ResponseEntity<Any> {
        if (!pollService.isPollUserByBoardId(
                (SecurityContextHolder.getContext().authentication as TokenAuthToken).getUserId(),
                boardId
            )
        ) {
            return ResponseEntity.badRequest().body(Response.error<Any>("투표를 생성한 사람만 투표를 삭제할 수 있습니다."))
        }
        return pollService.deletePollByBoardId(boardId)
    }

    @PostMapping("/{poll_id}")
    fun selectOption(@PathVariable("poll_id") pollId: Int, @RequestBody pollOption: Int): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken

        val optionResult = pollService.increaseOptionCount(auth.getUserId(), pollId, pollOption)

        if (!optionResult) {
            return ResponseEntity.badRequest().body(
                Response.error<Any>("투표에 실패했습니다")
            )
        }

        return ResponseEntity.ok().body(Response.stateOnly(true))
    }

    @GetMapping("/my-poll/{poll_id}")
    @ResponseBody
    fun pollInfo(@PathVariable("poll_id") pollId: Int): ResponseEntity<Any> {
        // 투표를 하지 않은 유저라면 pollId만 사용하여 투표 리스트를 보여주게 했음.
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (pollService.isPolledUser(auth.getUserId(), pollId) == 0) {
            return ResponseEntity.ok().body(Response.success(pollService.getPoll(pollId)))
        }
        // 투표를 한 유저라면 투표한 옵션을 보여줌.
        val optionList = pollService.getPollByPollId(pollId, auth.getUserId())
        if (optionList.getOrNull() == null)
            return ResponseEntity.badRequest().body(Response.error<Any>("삭제 됐거나 없는 투표입니다."))
        return ResponseEntity.ok().body(Response.success(optionList.getOrNull()))
    }

    @GetMapping("/board/{board_id}")
    @ResponseBody
    fun pollInfoByBoardId(@PathVariable("board_id") boardId: Int): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        val pollId = pollService.getPollIdByBoardId(boardId)
        if (pollService.isPolledUser(auth.getUserId(), pollId) == 0) {
            return ResponseEntity.ok().body(Response.success(pollService.getPoll(pollId)))
        }
        val optionList = pollService.getPollByBoardId(boardId, auth.getUserId())
        if (optionList.getOrNull() == null)
            return ResponseEntity.badRequest().body(Response.error<Any>("삭제 됐거나 없는 투표입니다."))
        return ResponseEntity.ok().body(Response.success(optionList.getOrNull()))
    }
}