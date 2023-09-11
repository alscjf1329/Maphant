package com.tovelop.maphant.service

import com.tovelop.maphant.dto.*
import com.tovelop.maphant.mapper.AdminPageMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
<<<<<<< HEAD
class AdminPageService(@Autowired val adminPageMapper: AdminPageMapper, @Autowired val userService: UserService, @Autowired val boardService: BoardService) {
    fun updateUserState(email: String, state: Int){
=======
class AdminPageService(@Autowired val adminPageMapper: AdminPageMapper, @Autowired val userService: UserService) {
    fun updateUserState(email: String, state: Int) {
>>>>>>> 4fcd8ead5ed2d1c751466290896e47b0f4278b9c
        userService.updateUserState(email, state)
    }

    fun updateUserRole(role: String, id: Int) {
        userService.updateUserRole(role, id)
    }

    /**
     * 신고 당한 글에 대한 정보들만 가져온다.
     * 신고 내용과 신과자의 대한 정보는 findReportInfo()를 통해 따로 응답.
     * sortType: 정렬 기준 (reportedAt: 오래된 신고부터, mostReportedRanking: 신고를 많이 받은 순서로)
     * reportSize: 불러올 신고의 개수.
     */
    fun findBoardReport(sortType: String, reportSize: Int): List<AdminBoardReportDTO> {
        return when (sortType) {
            "reportedAt" -> adminPageMapper.findBoardReportByReportedAt(reportSize)
            "mostReportedRanking" -> adminPageMapper.findBoardReportByMostReportedRanking(reportSize)
            else -> adminPageMapper.findBoardReportBySortType(reportSize, sortType)
        }
    }
    fun findBoardReportInfo(boardId: Int): List<BoardReportInfoDTO>{
        return adminPageMapper.findBoardReportInfo(boardId)
    }

    fun findCommentReport(sortType: String, reportSize: Int): List<AdminCommentReportDTO>{
        return when(sortType){
            "reportedAt" -> adminPageMapper.findCommentReportByReportedAt(reportSize)
            "mostReportedRanking" -> adminPageMapper.findCommentReportByMostReportedRanking(reportSize)
            else -> adminPageMapper.findCommentReportBySortType(reportSize, sortType)
        }
    }
    fun findCommentReportInfo(commentId: Int): List<CommentReportInfoDTO>{
        return adminPageMapper.findCommentReportInfo(commentId)
    }

    fun insertUserReport(userReportDTO: UserReportDTO){
        adminPageMapper.insertUserReport(userReportDTO)
    }
    /**
     * 현재 제재중인 유저들의 정보와 제재에 대한 정보를 가져옴
     */
    fun findCurrentUserSanction(): List<CurrentUserSanctionDTO> { //sanction = 제재
        return adminPageMapper.findCurrentUserSanction()
    }

    /**
     * 제재를 받은 적이 있는 모든 유저의 정보를 가져옴
     */
    fun findAllUserSanction(): List<AllUserSanctionDTO> {
        return adminPageMapper.findAllUserSanction()
    }

    /**
     * 특정 유저에 대한 모든 제재 내역을 가져온다.
     */
    fun findUserAllSanctionByUserId(userId: Int): List<UserReportDTO> {
        return adminPageMapper.findUserAllSanctionByUserId(userId)
    }
    fun deleteRecentUserReportByUserId(userId: Int){
        adminPageMapper.deleteRecentUserReportByUserId(userId)
    }
    fun updateBoardSanction(boardId: Int) {
        boardService.updateStateOfBoard(boardId, 2)
    }

    /**
     *유저 제재에 의한 게시글 블락 처리
     */
    fun updateBoardBlockByUserId(userId: Int) {
        adminPageMapper.updateBoardStateByUserId(userId,0, 3)
    }

    /**
     *유저 제재 해제에 의한 게시글 언블락 처리
     */
    fun updateBoardUnblockByUserId(userId: Int) {
        adminPageMapper.updateBoardStateByUserId(userId, 3, 0)
    }
    fun updateCommentSanction(commentId: Int) {
        adminPageMapper.updateCommentSanction(commentId, 2)
    }
    fun updateCommentBlockByUserId(userId: Int) {
        adminPageMapper.updateCommentStateByUserId(userId, 0, 3)
    }
    fun updateCommentUnblockByUserId(userId: Int) {
        adminPageMapper.updateCommentStateByUserId(userId, 3, 0)
    }
}
