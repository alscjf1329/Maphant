package com.tovelop.maphant.controller


import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.*
import com.tovelop.maphant.service.BoardService
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.type.response.Response
import com.tovelop.maphant.type.response.ResponseUnit
import com.tovelop.maphant.utils.SecurityHelper.Companion.isLogged
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.checkerframework.common.value.qual.EnumVal
import com.tovelop.maphant.service.RateLimitingService
import com.tovelop.maphant.utils.SecurityHelper.Companion.isNotLogged
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/board")
class BoardController(@Autowired val boardService: BoardService, @Autowired val rateLimitingService: RateLimitingService) {
    val sortCriterionMap = mapOf(1 to "created_at", 2 to "like_cnt")

    data class SortCriterionInfo(val id: Int, val name: String)

    @GetMapping("/boardType")
    fun readBoardType(): ResponseEntity<Any> {
        return ResponseEntity.ok().body(Response.success(boardService.getAllBoardType()))
    }

    @GetMapping("/sortCriterion")
    fun readSortCriterion(): ResponseEntity<Any> {
        return ResponseEntity.ok().body(Response.success(sortCriterionMap.map { SortCriterionInfo(it.key, it.value) }))
    }

    @GetMapping("/hot")
    fun readHotBoard(
        request: HttpServletRequest,
        @RequestParam(required = false) boardTypeId: Int?,
        @ModelAttribute @Valid pagingDto: PagingDto,
        @RequestHeader("x-category") category: Int
    ): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        val userId = auth.getUserId()
        if (!boardService.isInCategory(category) || (boardTypeId != null && !(boardService.isInBoardTypeId(boardTypeId)) || boardTypeId == 0)) {
            // 클라이언트가 존재하지 않는 카테고리나 게시판 유형을 요청한 경우
            return ResponseEntity.badRequest().body(Response.error<Any>("존재하지 않는 게시판 유형입니다."))
        }

        return ResponseEntity.ok()
            .body(Response.success(boardService.findHotBoardList(userId, category, boardTypeId, pagingDto)))
    }
    data class BoardListInfo(val name: String, val list: List<PageBoardDTO>)

    @GetMapping("")
    fun readBoardList(
        @RequestParam boardTypeId: Int,
        @RequestParam page: Int,
        @RequestParam pageSize: Int,
        @RequestParam sortCriterionId: Int,
        @RequestHeader("x-category") category: Int
    ): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error<Any>("로그인 안됨"))
        }
        auth as TokenAuthToken
        //pageNum과 pageSize는 양의 정수
        if (page <= 0) {
            return ResponseEntity.badRequest().body(Response.error<Any>("pageNum가 일치하지 않습니다."))
        }
        if (pageSize <= 0) {
            return ResponseEntity.badRequest().body(Response.error<Any>("pageSize가 일치하지 않습니다."))
        }
        if (sortCriterionId !in sortCriterionMap) {
            // sortStandard 값이 유효하지 않은 경우
            return ResponseEntity.badRequest().body(Response.error<Any>("유효하지 않은 sortCriterion 값입니다."))
        }
        if (!boardService.isInCategory(category) || !(boardService.isInBoardTypeId(boardTypeId) || boardTypeId == 0)) {
            // 클라이언트가 존재하지 않는 카테고리나 게시판 유형을 요청한 경우
            return ResponseEntity.badRequest().body(Response.error<Any>("존재하지 않는 게시판 유형입니다."))
        }
        val boardList =
            if (boardTypeId == 0) {
                boardService.getAllBoardType().map {
                    BoardListInfo(
                        it.name, boardService.findBoardList(
                            FindBoardDTO(it.id, page, pageSize, sortCriterionMap[sortCriterionId]!!),
                            auth.getUserId(),
                            category
                        )
                    )
                }
            } else {
                boardService.findBoardList(
                    FindBoardDTO(boardTypeId, page, pageSize, sortCriterionMap[sortCriterionId]!!),
                    auth.getUserId(),
                    category
                )
            }

        return ResponseEntity.ok().body(Response.success(boardList))
    }

    @PostMapping("/like/{boardId}")
    fun insertLikePost(@PathVariable("boardId") boardId: Int): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error<Any>("로그인 안됨"))
        }
        if (boardService.findBoard(boardId, auth.getUserId()) == null) {
            return ResponseEntity.badRequest().body(Response.error<Any>("게시글이 존재하지 않습니다."))
        }
        // 이미 좋아요를 누른 게시글인 경우
        if (boardService.findBoardLike(boardId, auth.getUserId())) {
            return ResponseEntity.badRequest().body(Response.error<Any>("이미 좋아요를 누른 게시글입니다."))
        }
        boardService.insertBoardLike(boardId, auth.getUserId())
        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @DeleteMapping("/like/{boardId}")
    fun deleteLikeBoard(@PathVariable("boardId") boardId: Int): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error<Any>("로그인 안됨"))
        }
        val board = boardService.findBoard(boardId, auth.getUserId())
            ?: return ResponseEntity.badRequest().body(Response.error<Any>("게시글이 존재하지 않습니다."))
        boardService.deleteBoardLike(boardId, board.userId)
        return ResponseEntity.ok(Response.stateOnly(true))
    }

    data class BoardInfo(val board: ExtBoardDTO, val answerList: List<BoardDTO>?)

    @GetMapping("/{boardId}")
    fun readBoard(@PathVariable("boardId") boardId: Int): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error<Any>("로그인 안됨"))
        }
        val board = boardService.findBoard(boardId, auth.getUserId())
        if (board == null || boardService.getIsHideByBoardId(boardId) == null) {
            return ResponseEntity.badRequest().body(Response.error<Any>("게시글이 존재하지 않습니다."))
        }
        if (boardService.getIsHideByBoardId(boardId)!!) {
            if (board.userId != auth.getUserId() && auth.getUserRole() != "admin") {
                return ResponseEntity.badRequest().body(Response.error<Any>("권한이 없습니다."))
            }
        }
        if (board.typeId == 2 && board.parentId == null) {
            val answerList = boardService.findAnswerBoardListByParentBoardId(board.id!!)
            return ResponseEntity.ok(Response.success(BoardInfo(board, answerList)))
        }
        return ResponseEntity.ok(Response.success(BoardInfo(board, null)))
    }

    @DeleteMapping("/{boardId}")
    fun deleteBoard(@PathVariable("boardId") boardId: Int): ResponseEntity<Any> {
        // 게시글 삭제
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error<Any>("로그인 안됨"))
        }
        val reBoard = boardService.findBoard(boardId, auth.getUserId())
            ?: return ResponseEntity.badRequest().body(Response.error<Any>("게시글이 존재하지 않습니다."))
        if (reBoard.isComplete == 1) {
            return ResponseEntity.badRequest().body(Response.error<Any>("체택된 게시글은 삭제할 수 없습니다."))
        }
        println(auth.getUserRole())
        // 관리자 권한 확인(관리자는 모든 게시글 삭제 가능)
        // 본인 게시글 인지 확인
        if (auth.getUserRole() != "admin" && auth.getUserId() != boardService.getUserIdByBoardId(boardId)) {
            return ResponseEntity.badRequest().body(Response.error<Any>("권한이 없습니다."))
        }
        boardService.deleteBoard(boardId)
        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @PostMapping("/create")
    fun createBoard(@RequestBody board: SetBoardDTO): ResponseEntity<ResponseUnit> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error("로그인 안됨"))
        }
        if (rateLimitingService.isBanned(auth.getUserId())) {
            return ResponseEntity.badRequest().body(Response.error("게시글 작성이 금지된 사용자입니다."))
        }
        // 제목 내용 빈칸인지 확인
        return if (board.title.isNotBlank() && board.body.isNotBlank()) {
            boardService.insertBoard(board.toBoardDTO(auth.getUserId()))
            rateLimitingService.requestCheck(auth.getUserId(),"WRITE_POST")
            ResponseEntity.ok(Response.stateOnly(true))
        } else {
            ResponseEntity.ok(Response.stateOnly(false))
            // 제목 또는 내용이 빈칸인 경우 실패 응답을 반환합니다.
        }
    }

    @PutMapping("/update")
    fun updateBoard(@RequestBody board: UpdateBoardDTO): ResponseEntity<ResponseUnit> {
        // 현재 로그인한 사용자 정보 가져오기
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error("로그인 안됨"))
        }
        // 게시글이 존재하지 않는 경우
        val reBoard = boardService.findBoard(board.id, auth.getUserId())
            ?: return ResponseEntity.badRequest().body(Response.error("게시글이 존재하지 않습니다."))
        // 제목 및 내용 빈칸 확인
        if (reBoard.isComplete == 1) {
            return ResponseEntity.badRequest().body(Response.error("채택된 글은 수정이 불가합니다."))
        }
        if (board.title.isEmpty() || board.body.isEmpty()) {
            return ResponseEntity.badRequest().body(Response.error("제목과 내용을 입력해주세요."))
        }
        // 본인 게시글 확인
        if (reBoard.userId != auth.getUserId() && auth.getUserRole() != "admin") {
            return ResponseEntity.badRequest().body(Response.error("권한이 없습니다."))
        }
        // 게시글 읽어오기
        boardService.updateBoard(board)
        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @GetMapping("/search")
    fun searchBoard(@RequestParam content: String): Any {
        val searchBoard = boardService.findBoardByKeyword(content)
        return ResponseEntity.ok(Response.success(searchBoard))
    }

    @PostMapping("/report")
    fun reportBoard(@RequestParam boardId: Int, @RequestParam reportId: Int): ResponseEntity<ResponseUnit> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error("로그인 안됨"))
        }
        if (!boardService.isInBoardByBoardId(boardId)) {
            return ResponseEntity.badRequest().body(Response.error("게시글이 존재하지 않습니다."))
        }
        if (auth.getUserId() == boardService.getUserIdByBoardId(boardId)) {
            return ResponseEntity.badRequest().body(Response.error("자신의 게시글은 신고할 수 없습니다."))
        }
        if (!boardService.isInReportId(reportId)) {
            return ResponseEntity.badRequest().body(Response.error("없는 신고 유형입니다."))
        }
        if (boardService.isInReportByBoardId(boardId, auth.getUserId())) {
            return ResponseEntity.badRequest().body(Response.error("이미 신고한 게시글입니다."))
        }
        // 신고하기
        boardService.insertBoardReport(boardId, auth.getUserId(), reportId)
        // return: json
        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @PostMapping("/complete")
    fun completeBoard(@RequestParam questId: Int, @RequestParam answerId: Int): ResponseEntity<ResponseUnit> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error("로그인 안됨"))
        }
        if (!boardService.isInBoardByBoardId(questId) || !boardService.isInBoardByBoardId(answerId)) {
            return ResponseEntity.badRequest().body(Response.error("게시글이 존재하지 않습니다."))
        }
        if (boardService.getUserIdByBoardId(questId) != auth.getUserId()) {
            return ResponseEntity.badRequest().body(Response.error("자신의 게시글이 아닙니다."))
        }
        if (auth.getUserId() == boardService.getUserIdByBoardId(answerId)) {
            return ResponseEntity.badRequest().body(Response.error("자신의 게시글은 체택할 수 없습니다."))
        }
        if (boardService.isinCompleteByBoardId(questId) || boardService.isinCompleteByBoardId(answerId)) {
            return ResponseEntity.badRequest().body(Response.error("이미 체택한 게시글입니다."))
        }
        if (!boardService.isParent(questId, answerId)) {
            return ResponseEntity.badRequest().body(Response.error("해당글의 답변이 아닙니다."))
        }

        // 채택하기
        boardService.completeBoard(questId, answerId, auth.getUserId())
        // return: json
        return ResponseEntity.ok(Response.stateOnly(true))
    }
}

