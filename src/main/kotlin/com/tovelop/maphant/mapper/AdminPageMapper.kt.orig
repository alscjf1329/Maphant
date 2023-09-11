package com.tovelop.maphant.mapper

<<<<<<< HEAD
import com.tovelop.maphant.dto.*
=======
import com.tovelop.maphant.dto.AdminBoardReportDTO
>>>>>>> 4fcd8ead5ed2d1c751466290896e47b0f4278b9c
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface AdminPageMapper {
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
}
