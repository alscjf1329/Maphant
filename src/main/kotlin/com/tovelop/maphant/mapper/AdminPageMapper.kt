package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.*
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

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
    fun findUserReportInfo(): List<List<Any>>
    fun findBoardReportInfoByUserId(userId: Int): List<BoardSanctionDTO>
    fun findCommentReportInfoByUserId(userId: Int): List<CommentSanctionDTO>
}
