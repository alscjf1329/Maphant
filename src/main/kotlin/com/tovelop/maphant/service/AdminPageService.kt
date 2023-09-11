package com.tovelop.maphant.service

import com.tovelop.maphant.dto.*
import com.tovelop.maphant.mapper.AdminPageMapper
import com.tovelop.maphant.mapper.BoardMapper
import com.tovelop.maphant.mapper.CommentMapper
import com.tovelop.maphant.mapper.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AdminPageService(
    @Autowired val adminPageMapper: AdminPageMapper,
    @Autowired val userMapper: UserMapper,
    @Autowired val boardMapper: BoardMapper,
    @Autowired val commentMapper: CommentMapper,
    @Autowired val fcmService: FcmService) {
    fun updateUserState(userId: Int, state: Int) {
        adminPageMapper.updateUserState(userId, state)
    }

    fun updateUserRole(role: String, id: Int) {
        userMapper.updateUserRole(role, id)
    }

    /**
     * 신고 당한 글에 대한 정보들만 가져온다.
     * 신고 내용과 신과자의 대한 정보는 findReportInfo()를 통해 따로 응답.
     * sortType: 정렬 기준 (reportedAt: 오래된 신고부터, mostReportedRanking: 신고를 많이 받은 순서로)
     * reportSize: 불러올 신고의 개수.
     */
    fun findBoardReport(sortType: String, reportSize: Int): List<AdminBoardReportDTO>? {
        return when (sortType) {
            "reportedAt" -> adminPageMapper.findBoardReportByReportedAt(reportSize)
            "mostReportedRanking" -> adminPageMapper.findBoardReportByMostReportedRanking(reportSize)
            else -> null
        }
    }
    fun findBoardReportInfo(boardId: Int): List<BoardReportInfoDTO>{
        return adminPageMapper.findBoardReportInfo(boardId)
    }

    fun findCommentReport(sortType: String, reportSize: Int): List<AdminCommentReportDTO>?{
        return when(sortType){
            "reportedAt" -> adminPageMapper.findCommentReportByReportedAt(reportSize)
            "mostReportedRanking" -> adminPageMapper.findCommentReportByMostReportedRanking(reportSize)
            else -> null
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
        boardMapper.updateStateOfBoard(boardId, 2)
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
        commentMapper.changeState(commentId, 2)
    }
    fun updateCommentBlockByUserId(userId: Int) {
        adminPageMapper.updateCommentStateByUserId(userId, 0, 3)
    }
    fun updateCommentUnblockByUserId(userId: Int) {
        adminPageMapper.updateCommentStateByUserId(userId, 3, 0)
    }

    /**
     * userIdList = 푸쉬 메시지를 받을 유저들의 id 리스트, 만약 비어 있다면 모든 앱 사용 유저에게 푸쉬 메시지를 전송
     */
    fun sendPushMessage(userIdList: List<Int>, title: String, body: String) {
        if (userIdList.isEmpty()){
            //모든 유저 id가져와서 메시지 전송
            val allAppUserId = adminPageMapper.findAllAppUserId()
            //메시지 전송
            allAppUserId.map { fcmService.send(FcmMessageDTO(it, title, body)) }
            return
        }
        userIdList.map { fcmService.send(FcmMessageDTO(it, title, body)) }
    }
    fun findUserReportList(): List<UserDTO> {
        return adminPageMapper.findUserReportList(adminPageMapper.findBoardCommentSanctionCount().map { it.userId })
    }
    fun findUserReportInfo(userId: Int): List<List<Any>> {
        return adminPageMapper.findUserReportInfo()
    }
}
