package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class AdminBoardReportDTO(
    val boardId: Int,
    val boardTitle: String,
    val boardBody: String,
    val boardUserId: Int,
    val boardUserEmail: String
//    신고 당한 글의 정보만 가져온다. 신고자는 여러명일 수 있기 때문에
//    신고한 유저들의 정보는 따로 분리하여 코드를 작성한다.
//    즉 글의 정보만 미리 보여주고 따로 요청하여 신고와 신고자의 정보를 확인할 수 있게 한다.
//    val reporterUserId: Int,
//    val reporterUserEmail: String,
//    val boardReportedAt: LocalDateTime,
//    val boardReportName: String
)
