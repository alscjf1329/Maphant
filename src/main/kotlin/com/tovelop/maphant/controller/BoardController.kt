package com.tovelop.maphant.controller


import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.*
import com.tovelop.maphant.service.*
import com.tovelop.maphant.type.paging.Pagination
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.type.response.Response
import com.tovelop.maphant.type.response.ResponseUnit
import com.tovelop.maphant.utils.BadWordFiltering
import com.tovelop.maphant.utils.SecurityHelper.Companion.isNotLogged
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/board/")
class BoardController(
    @Autowired val boardService: BoardService,
    @Autowired val rateLimitingService: RateLimitingService,
    @Autowired val tagService: TagService,
    @Autowired val pollService: PollService,
    @Autowired val searchService: SearchService,
    @Autowired val bookmarkService: BookmarkService
) {
    val sortCriterionMap = mapOf(1 to "created_at", 2 to "like_cnt")

    data class SortCriterionInfo(val id: Int, val name: String)

    @GetMapping("boardType/")
    fun readBoardType(): ResponseEntity<Any> {
        return ResponseEntity.ok().body(Response.success(boardService.getAllBoardType()))
    }

    @GetMapping("sortCriterion/")
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

    data class BoardListInfo(val name: String?, val list: List<PageBoardDTO>, val pagination: Pagination)

    @GetMapping("")
    fun readBoardList(
        @RequestParam boardTypeId: Int,
        @RequestParam parentId: Int?,
        @RequestParam page: Int,
        @RequestParam recordSize: Int,
        @RequestParam pageSize: Int,
        @RequestParam sortCriterionId: Int,
        @RequestHeader("x-category") category: Int
    ): ResponseEntity<Any> {
        val aBoardTypeId = 8
        val auth = SecurityContextHolder.getContext().authentication
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error<Any>("로그인 안됨"))
        }
        auth as TokenAuthToken
        //pageNum과 pageSize는 양의 정수
        if (page <= 0) {
            return ResponseEntity.badRequest().body(Response.error<Any>("pageNum가 일치하지 않습니다."))
        }
        if (recordSize <= 0) {
            return ResponseEntity.badRequest().body(Response.error<Any>("recordSize가 일치하지 않습니다."))
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
        if (boardTypeId == aBoardTypeId && parentId == null) {
            return ResponseEntity.badRequest().body(Response.error<Any>("질문 게시글 id가 필요합니다."))
        }
        val boardList = if (boardTypeId == 0) {
            boardService.getAllBoardType().map {
                BoardListInfo(
                    it.name, boardService.findBoardList(
                        FindBoardDTO(it.id, parentId, page, recordSize, sortCriterionMap[sortCriterionId]!!),
                        auth.getUserId(),
                        category
                    ), Pagination(
                        if (boardTypeId == aBoardTypeId) boardService.getABoardCnt(parentId!!)
                        else boardService.getBoardSizeByCategoryIdAndBoardTypeId(category, it.id),
                        PagingDto(page, recordSize, pageSize)
                    )
                )
            }
        } else {
            BoardListInfo(
                null, boardService.findBoardList(
                    FindBoardDTO(boardTypeId, parentId, page, recordSize, sortCriterionMap[sortCriterionId]!!),
                    auth.getUserId(),
                    category
                ), Pagination(
                    if (boardTypeId == aBoardTypeId) boardService.getABoardCnt(parentId!!)
                    else boardService.getBoardSizeByCategoryIdAndBoardTypeId(category, boardTypeId),
                    PagingDto(page, recordSize, pageSize)
                )
            )
        }

        return ResponseEntity.ok().body(Response.success(boardList))
    }

    @PostMapping("like/{boardId}/")
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

    @DeleteMapping("like/{boardId}/")
    fun deleteLikeBoard(@PathVariable("boardId") boardId: Int): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error<Any>("로그인 안됨"))
        }
        val board = boardService.findBoard(boardId, auth.getUserId()) ?: return ResponseEntity.badRequest()
            .body(Response.error<Any>("게시글이 존재하지 않습니다."))
        boardService.deleteBoardLike(boardId, auth.getUserId())
        return ResponseEntity.ok(Response.stateOnly(true))
    }

    data class BoardInfo(val board: ExtBoardDTO, val poll: Any?)

    @GetMapping("{boardId}/")
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
        val pollId = pollService.getPollIdByBoardId(boardId)
        board.addBookmark(bookmarkService.isBookmarked(auth.getUserId(), boardId))
        //투표 없는 경우
        if(pollId==null){
            return ResponseEntity.ok(
                Response.success(
                    BoardInfo(
                        board, null
                    )
                )
            )
        }
        val optionList = pollService.getPollByBoardId(boardId, auth.getUserId())
        //투표한 경우
        if (pollService.isPolledUser(auth.getUserId(), pollId) == 0) {
            return ResponseEntity.ok(
                Response.success(
                    BoardInfo(
                        board, pollService.getPoll(pollId)
                    )
                )
            )
        }
        if (optionList.getOrNull() == null) return ResponseEntity.badRequest()
            .body(Response.error<Any>("삭제 됐거나 없는 투표입니다."))
        //투표하지 않은 경우
        return ResponseEntity.ok(
            Response.success(
                BoardInfo(
                    board, optionList.getOrNull()
                )
            )
        )
    }

    @DeleteMapping("{boardId}/")
    fun deleteBoard(@PathVariable("boardId") boardId: Int): ResponseEntity<Any> {
        // 게시글 삭제
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error<Any>("로그인 안됨"))
        }
        val reBoard = boardService.findBoard(boardId, auth.getUserId()) ?: return ResponseEntity.badRequest()
            .body(Response.error<Any>("게시글이 존재하지 않습니다."))
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

        //게시물에 있었던 각 태그들의 갯수를 1씩 감소시킴
        tagService.deleteTagCnt(boardId)
        searchService.delete(boardId)

        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @PostMapping("create/")
    fun createBoard(
        @RequestBody board: SetBoardDTO, @RequestHeader("x-category") category: Int
    ): ResponseEntity<ResponseUnit> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error("로그인 안됨"))
        }
        if (rateLimitingService.isBanned(auth.getUserId())) {
            return ResponseEntity.badRequest().body(Response.error("게시글 작성이 금지된 사용자입니다."))
        }
        if (board.title.isBlank() || board.body.isBlank()) {
            return ResponseEntity.badRequest().body(Response.error("제목이나 본문이 비어있습니다."))
        }

        val badWordFiltering = BadWordFiltering()
        if (badWordFiltering.hasBadWords(board.title)) {
            return ResponseEntity.badRequest().body(Response.error("제목에는 비속어를 적을 수 없습니다."))
        }
        board.body = badWordFiltering.filterBadWords(board.body)
        val boardDto = board.toBoardDTO(auth.getUserId(), category)
        boardService.insertBoard(boardDto)
        rateLimitingService.requestCheck(auth.getUserId(), "WRITE_POST")

        if (board.poll != null) { //투표생성
            val poll = PollDTO(
                board.poll.id,
                boardDto.id as Int,
                board.poll.title,
                board.poll.options,
                board.poll.expireDateTime,
                board.poll.state
            )
            pollService.createPoll(poll)
        }

        // tagNames가 비어있지 않은 경우 tagService.insertTag
        if (board.tagNames.isNullOrEmpty().not()) board.tagNames?.let {
            tagService.insertTag(category, boardDto.id!!, it)
            it.forEach { tagName ->
                tagService.insertBoardTag(
                    boardDto.id, tagService.getTagByName(tagName)?.id ?: throw Exception("태그가 존재하지 않습니다.")
                )
            }
        }

        searchService.create(boardDto.id!!, boardDto.title, boardDto.body, board.tagNames)


        // 제목 내용 빈칸인지 확인
        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @PutMapping("update/")
    fun updateBoard(
        @RequestBody board: UpgradeUpdateBoardDTO, @RequestHeader("x-category") category: Int
    ): ResponseEntity<ResponseUnit> {
        // 현재 로그인한 사용자 정보 가져오기
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        if (auth.isNotLogged()) {
            return ResponseEntity.badRequest().body(Response.error("로그인 안됨"))
        }
        // 게시글이 존재하지 않는 경우
        val reBoard = boardService.findBoard(board.id, auth.getUserId()) ?: return ResponseEntity.badRequest()
            .body(Response.error("게시글이 존재하지 않습니다."))
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
        val badWordFiltering = BadWordFiltering()
        if (badWordFiltering.hasBadWords(board.title)) {
            return ResponseEntity.badRequest().body(Response.error("제목에는 비속어를 적을 수 없습니다."))
        }
        board.body = badWordFiltering.filterBadWords(board.body)
        boardService.updateBoard(board.toUpdateBoardDTO())
        searchService.update(board.id, board.title, board.body, board.tags)
        // 태그 수정하기
        if (!board.tags.isNullOrEmpty()) tagService.modifyTag(category, board.id, board.tags)

        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @GetMapping("search/")
    fun searchBoard(
        @ModelAttribute boardSearchDto: BoardSearchDto,
        @ModelAttribute pagingDto: PagingDto,
        @RequestHeader("x-category") category: Int
    ): ResponseEntity<Response<PagingResponse<BoardSearchResponseDto>>> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        val userId = auth.getUserId()

        boardSearchDto.trimFields()
        val result = boardService.findBoardListBySearch(boardSearchDto, pagingDto, category, userId)
        return ResponseEntity.ok(Response.success(result))
    }

    @PostMapping("report/")
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

    @PostMapping("complete/")
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

    @GetMapping("poll")
    fun getPollBoardList(
        @RequestParam("boardTypeId") boardTypeId: Int?,
        @ModelAttribute @Valid pagingDto: PagingDto,
        @RequestHeader("x-category") category: Int
    ): ResponseEntity<Response<PagingResponse<PageBoardDTO>>> {
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken
        val userId = auth.getUserId()

        val result = boardService.getPollBoardList(userId, boardTypeId, category, pagingDto)

        return ResponseEntity.ok(Response.success(result))
    }
}

