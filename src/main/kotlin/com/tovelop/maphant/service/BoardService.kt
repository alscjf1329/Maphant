package com.tovelop.maphant.service

import com.tovelop.maphant.dto.*
import com.tovelop.maphant.mapper.BoardMapper
import com.tovelop.maphant.mapper.TagMapper
import com.tovelop.maphant.type.paging.Pagination
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Collections
import java.util.Random


@Service
class BoardService(@Autowired val boardMapper: BoardMapper,
                   @Autowired private val redisService: RedisService,
                   @Autowired private val tagMapper: TagMapper,
                   @Autowired private val pollService: PollService) {
    fun getABoardCnt(parentId:Int):Int{
        return boardMapper.getABoardCnt(parentId)
    }
    fun getBoardTypeIdByBoardTypeName(boardTypeName: String): Int {
        return boardMapper.getBoardTypeIdByBoardTypeName(boardTypeName)
    }

    fun getCategoryIdByCategoryName(categoryName: String): Int {
        return boardMapper.getCategoryIdByCategoryName(categoryName)
    }

    fun findBoardList(findBoardDTO: FindBoardDTO, userId: Int, categoryId: Int): List<PageBoardDTO> {
        val startRow = (findBoardDTO.page - 1) * findBoardDTO.pageSize
        return boardMapper.findBoardList(userId,findBoardDTO, startRow, categoryId)
    }

    fun getBoardSizeByCategoryIdAndBoardTypeId(categoryId: Int, boardTypeId: Int): Int {
        return boardMapper.getBoardSizeByCategoryIdAndBoardTypeId(categoryId, boardTypeId)
    }

    fun insertBoard(boardDTO: BoardDTO) {
        boardMapper.insertBoard(boardDTO)
        println("boardId = ${boardDTO.id}")
    }

    fun findBoard(boardId: Int, userId: Int): ExtBoardDTO? {
        val board = boardMapper.findBoardById(userId,boardId)
        board?.tags = tagMapper.findBoardTags(boardId)
        return board
    }

    fun updateBoard(updateBoardDTO: UpdateBoardDTO) {
        boardMapper.updateBoard(updateBoardDTO)
    }

    fun deleteBoard(boardId: Int) {
        boardMapper.deleteBoard(boardId)
    }

    fun getIsHideByBoardId(boardId: Int): Boolean? {
        val board = boardMapper.findBoard(boardId)
        return board?.isHide == 1
    }

    fun getUserIdByBoardId(boardId: Int): Int? {
        val board = boardMapper.findBoard(boardId)
        return board?.userId
    }

    fun isModified(boardId: Int): Boolean {
        val board = boardMapper.findBoard(boardId)
        return board?.modifiedAt != null
    }

    fun insertBoardLike(boardId: Int, userId: Int) {
        boardMapper.insertBoardLike(boardId, userId)
    }

    fun deleteBoardLike(boardId: Int, userId: Int) {
        boardMapper.deleteBoardLike(boardId, userId)
    }

    fun insertBoardReport(boardId: Int, userId: Int, reportId: Int) {
        boardMapper.insertBoardReport(boardId, userId, reportId)
    }

    fun findBoardByKeyword(keyword: String, boardTypeId: Int, categoryId: Int): List<BoardDTO> {
        return boardMapper.findBoardByKeyword(keyword, boardTypeId, categoryId)
    }

    fun findBoardListBySearch(boardSearchDto:BoardSearchDto,pagingDto: PagingDto, categoryId: Int, userId: Int): PagingResponse<BoardSearchResponseDto> {

        val count = boardMapper.countBoardListBySearch(boardSearchDto,categoryId);
        if(count < 1) return PagingResponse(Collections.emptyList(),Pagination(0,pagingDto))

        val pagination = Pagination(count,pagingDto)
        val boardList = boardMapper.findBoardListBySearch(boardSearchDto, pagingDto, categoryId, userId)

        return PagingResponse(boardList,pagination)
    }

    fun isInCategory(categoryId: Int): Boolean {
        return boardMapper.isInCategory(categoryId) != null
    }

    fun isInBoardTypeId(boardType: Int): Boolean {
        return boardMapper.isInBoardTypeId(boardType) != null
    }

    fun findBoardLike(boardId: Int, userId: Int): Boolean {
        val boardLikeDTO = boardMapper.findBoardLike(boardId, userId)
        return boardLikeDTO != null
    }

    fun isInReportByBoardId(boardId: Int, userId: Int): Boolean {
        val boardReportDTO = boardMapper.isInReportByBoardId(boardId, userId)
        return boardReportDTO != 0
    }

    fun isInReportId(reportId: Int): Boolean {
        val reportName = boardMapper.isInReportId(reportId)
        return !reportName.isNullOrBlank()
    }

    fun isInBoardByBoardId(boardId: Int): Boolean {
        val isInboardId = boardMapper.isInBoardByBoardId(boardId)
        return isInboardId != null
    }

    fun completeBoard(parentBoardId: Int, childBoardId: Int, userId: Int) {
        boardMapper.insertBoardQna(parentBoardId, childBoardId)
        boardMapper.updateIsCompleteOfBoard(parentBoardId, 1)
    }

    fun isinCompleteByBoardId(boardId: Int): Boolean {
        val board = boardMapper.findBoard(boardId)
        return board?.isComplete == 1
    }

    fun isParent(parentBoardId: Int, childBoardId: Int): Boolean {
        val childBoard = boardMapper.findBoard(childBoardId)
        return childBoard?.parentId == parentBoardId
    }

    fun updateStateOfBoard(boardId: Int, state: Int) {
        boardMapper.updateStateOfBoard(boardId, state)
    }

    fun findAnswerBoardListByParentBoardId(parentBoardId: Int): List<BoardDTO> {
        return boardMapper.findAnswerBoardListByParentBoardId(parentBoardId)
    }

    fun getAllBoardType(): MutableList<BoardTypeDTO> {
        return boardMapper.getAllBoardType()
    }

    fun findHotBoardList(
        userId: Int,
        category: Int,
        boardTypeId: Int?,
        pagingDto: PagingDto
    ): PagingResponse<HotBoardDto> {
        //페이지가 1일떈 새로운 seed값 생성 10분 후 삭제
        if (pagingDto.page == 1) {
            redisService.set("seed|$userId", Random().nextLong().toString())
            redisService.expire("seed|$userId", 60 * 10)
        }

        var seed = redisService.get("seed|$userId")?.toLong();

        //사용자가 page가 1이아닌 2부터 요청할 경우 && redis에 seed값이 없을 경우 null일 수 있음
        if (seed == null) {
            seed = Random().nextLong()
            redisService.set("seed|$userId", seed.toString())
            redisService.expire("seed|$userId", 60 * 10)
        }

        val pagination: Pagination
        val hotBoards: List<HotBoardDto>


        if (boardTypeId != null) {
            val count = boardMapper.getHotBoardCountWithBoardType(category, boardTypeId);
            pagination = Pagination(count, pagingDto)
            hotBoards = boardMapper.findHotBoardsWithBoardType(userId, category, boardTypeId, seed, pagingDto)
        } else {
            val count = boardMapper.getHotBoardCount(category);
            pagination = Pagination(count, pagingDto)
            hotBoards = boardMapper.findHotBoards(userId, category, seed, pagingDto)
        }

        return PagingResponse(hotBoards, pagination)
    }

    fun findLastInsertId(): Int {
        return boardMapper.findLastInsertId()
    }

    fun getPollBoardList(userId: Int ,boardTypeId: Int?, categoryId: Int, pagingDto: PagingDto): PagingResponse<PageBoardDTO> {
        val count = boardMapper.countPollBoardList(categoryId,boardTypeId)

        val pagination = Pagination(count,pagingDto)

        val boardList = boardMapper.getPollBoardList(userId,categoryId,boardTypeId,pagingDto)

        return PagingResponse(boardList,pagination)

    }
}


