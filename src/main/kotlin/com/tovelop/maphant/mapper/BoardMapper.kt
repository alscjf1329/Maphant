package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.*
import com.tovelop.maphant.type.paging.PagingDto
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface BoardMapper {
    fun getABoardCnt(parentId: Int): Int
    fun getBoardTypeIdByBoardTypeName(boardTypeName: String): Int
    fun getCategoryIdByCategoryName(categoryName: String): Int
    fun findBoardList(userId: Int, findBoardDTO: FindBoardDTO, startRow: Int, categoryId: Int): List<PageBoardDTO>
    fun getBoardSizeByCategoryIdAndBoardTypeId(categoryId: Int, boardTypeId: Int): Int
    fun insertBoard(boardDTO: BoardDTO): Int
    fun findBoard(boardId: Int): BoardDTO?
    fun findBoardById(userId: Int, boardId: Int): ExtBoardDTO?
    fun updateBoard(updateBoardDTO: UpdateBoardDTO)
    fun deleteBoard(boardId: Int)
    fun insertBoardLike(boardId: Int, userId: Int)
    fun deleteBoardLike(boardId: Int, userId: Int)
    fun insertBoardReport(boardId: Int, userId: Int, reportId: Int)
    fun findBoardByKeyword(keyword: String, boardTypeId: Int, categoryId: Int): List<BoardDTO>
    fun isInCategory(categoryId: Int): String?
    fun isInBoardTypeId(boardTypeId: Int): Int?
    fun findBoardLike(boardId: Int, userId: Int): BoardLikeDTO?
    fun isInReportByBoardId(boardId: Int, userId: Int): Int
    fun isInReportId(reportId: Int): String?
    fun isInBoardByBoardId(boardId: Int): Int?
    fun insertBoardQna(parentBoardId: Int, childBoardId: Int)
    fun updateIsCompleteOfBoard(boardId: Int, isComplete: Int)
    fun updateStateOfBoard(boardId: Int, state: Int)
    fun findAnswerBoardListByParentBoardId(parentBoardId: Int): List<BoardDTO>
    fun findHotBoardsWithBoardType(
        userId: Int,
        categoryId: Int,
        boardType: Int,
        seed: Long,
        pagingDto: PagingDto
    ): List<HotBoardDto>

    fun getHotBoardCountWithBoardType(categoryId: Int, boardType: Int): Int
    fun findHotBoards(userId: Int, categoryId: Int, seed: Long, pagingDto: PagingDto): List<HotBoardDto>
    fun getHotBoardCount(categoryId: Int): Int
    fun getAllBoardType(): MutableList<BoardTypeDTO>
    fun findLastInsertId(): Int

    fun findBoardListBySearch(
        boardSearchDto: BoardSearchDto,
        pagingDto: PagingDto,
        categoryId: Int,
        userId: Int
    ): List<BoardSearchResponseDto>

    fun countBoardListBySearch(boardSearchDto: BoardSearchDto, categoryId: Int): Int

    fun getPollBoardList(userId:Int, categoryId:Int, boardTypeId:Int?, pagingDto: PagingDto): List<PageBoardDTO>
    fun countPollBoardList(categoryId:Int, boardTypeId:Int?):Int

    fun findAllBoards(): List<InitBoardDto>?

    fun getCountAllBoards():Int

    fun findBoardByDfIdf(boardTfIdfMap: MutableMap<Int, Double>, pagingDto: PagingDto, categoryId:Int, boardTypeId: Int?): List<PageBoardDTO>
}
