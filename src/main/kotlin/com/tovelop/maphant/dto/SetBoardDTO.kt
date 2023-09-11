package com.tovelop.maphant.dto

data class SetBoardDTO(
    val parentId: Int?,
    val typeId: Int,
    val title: String,
    var body: String,
    val isHide: Int,
    val isComplete: Int,
    val isAnonymous: Int,
    val imagesUrl: List<String>?,
    val tagNames: List<String>?,
    val poll : BoardPollDto?
) {
    fun toBoardDTO(userId: Int, categoryId: Int): BoardDTO {
        return BoardDTO(
            id = null,
            parentId = parentId,
            categoryId = categoryId,
            userId = userId,
            typeId = typeId,
            title = title,
            body = body,
            state = 0,
            isHide = isHide, // 익명여부
            isComplete = isComplete, // 체택인데 기본 0?
            isAnonymous = isAnonymous,
            commentCnt = 0,
            likeCnt = 0,
            reportCnt = 0,
            imagesUrl = imagesUrl?.joinToString(","), // 이미지는 s3? 물어보고 수정
            createdAt = null,
            modifiedAt = null,
        )
    }
}
