package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.*
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Mapper
@Repository
interface AdminPageMapper {
    fun updateUserState(userId: Int, state: Int)
    fun findBoardReportByReportedAt(reportSize: Int): List<AdminBoardReportDTO>
    fun findBoardReportByMostReportedRanking(reportSize: Int): List<AdminBoardReportDTO>
    fun findBoardReportBySortType(reportSize: Int, sortType: String): List<AdminBoardReportDTO>
    fun findBoardReportInfo(boardId: Int): List<BoardReportInfoDTO>
    fun findCommentReportByReportedAt(reportSize: Int): List<AdminCommentReportDTO>
    fun findCommentReportByMostReportedRanking(reportSize: Int): List<AdminCommentReportDTO>
    fun findCommentReportBySortType(reportSize: Int, sortType: String): List<AdminCommentReportDTO>
    fun findCommentReportInfo(commentId: Int): List<CommentReportInfoDTO>
    fun insertUserReport(userReportDTO: UserReportDTO)
    fun findCurrentUserSanction(): List<CurrentUserSanctionDTO>
    fun findAllUserSanction(): List<AllUserSanctionDTO>
    fun findUserAllSanctionByUserId(userId: Int): List<UserReportDTO>
    fun deleteRecentUserReportByUserId(userId: Int)
    fun updateBoardStateByUserId(userId: Int, beforeState: Int, afterState: Int)
    fun updateCommentSanction(commentId: Int)
    fun updateCommentStateByUserId(userId: Int, beforeState: Int, afterState: Int)
    fun findAllAppUserId(): List<Int>
    fun findUserReportList(userIdList: List<Int>): List<UserDTO>
    fun findBoardSanctionCountByUserId(): CountSanctionDTO
    fun findCommentSanctionCountByUserId(): CountSanctionDTO
    fun findBoardCommentSanctionCount(): List<CountSanctionDTO>
    fun findBoardReportInfoByUserId(userId: Int): List<BoardSanctionDTO>
    fun findCommentReportInfoByUserId(userId: Int): List<CommentSanctionDTO>
    fun findReportByUserId(userId: Int): Boolean
    fun findReportInfoByUserId(boardId: Int): List<UserReportDTO>
    fun updateBoardReportStateByBoardId(boardId: Int)
    fun updateCommentReportStateByCommentId(commentId: Int)
    fun findLoginLogByDate(start: LocalDateTime, end: LocalDateTime): Int
    fun findBoardLogByDate(start: LocalDateTime, end: LocalDateTime): Int
    fun findCommentLogByDate(start: LocalDateTime, end: LocalDateTime): Int
    fun findDayBoardLogByDate(start: LocalDate, end: LocalDate): List<DayLogDTO>
    fun findDayCommentLogByDate(start: LocalDate, end: LocalDate): List<DayLogDTO>
    fun findWeekLoginLogByDate(start: LocalDateTime, end: LocalDateTime): List<WeekLogDTO>
    fun findWeekBoardLogByDate(start: LocalDateTime, end: LocalDateTime): List<WeekLogDTO>
    fun findWeekCommentLogByDate(start: LocalDateTime, end: LocalDateTime): List<WeekLogDTO>
    fun findDayLoginLogByDate(start: LocalDate, end: LocalDate): List<DayLogDTO>
}
