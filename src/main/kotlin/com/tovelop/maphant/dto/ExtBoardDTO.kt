package com.tovelop.maphant.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tovelop.maphant.service.BookmarkService
import java.time.LocalDateTime

data class ExtBoardDTO(
    val id: Int?,
    @JsonIgnore
    val userId: Int,
    val parentId: Int?,
    val categoryId: Int,
    val userNickname: String,
    val typeId: Int,
    val title: String,
    val body: String,
    val state: Int,
    val isHide: Int,
    val isComplete: Int,
    val isAnonymous: Int,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
    val commentCnt: Int,
    val likeCnt: Int,
    val reportCnt: Int,
    private val imagesUrlString: String?,
    val isLike: Boolean,
    val isMyBoard: Boolean,
    var tags: List<ReqTagDTO>,
    var isBookmarked: Boolean = false,
) {
    val imagesUrl: List<String>
        get() = imagesUrlString?.split(",") ?: listOf()

    constructor(
        id: Int?,
        userId: Int,
        parentId: Int?,
        categoryId: Int,
        userNickname: String,
        typeId: Int,
        title: String,
        body: String,
        state: Int,
        isHide: Int,
        isComplete: Int,
        isAnonymous: Int,
        createdAt: LocalDateTime?,
        modifiedAt: LocalDateTime?,
        commentCnt: Int,
        likeCnt: Int,
        reportCnt: Int,
        imagesUrl: String?,
        isLike: Boolean,
        isMyBoard: Boolean,
    ) : this(
        id,
        userId,
        parentId,
        categoryId,
        userNickname,
        typeId,
        title,
        body,
        state,
        isHide,
        isComplete,
        isAnonymous,
        createdAt,
        modifiedAt,
        commentCnt,
        likeCnt,
        reportCnt,
        imagesUrl,
        isLike,
        isMyBoard,
        listOf(),
        false
    )

    fun addBookmark(bookmark: Boolean) {
        this.isBookmarked = bookmark
    }
}

data class UpgradeExtBoardDTO(
    val id: Int?,
    val parentId: Int?,
    val categoryId: Int,
    val userId: Int,
    val typeId: Int,
    val title: String,
    val body: String,
    val state: Int,
    val isHide: Int,
    val isComplete: Int,
    val isAnonymous: Int,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
    val commentCnt: Int,
    val likeCnt: Int,
    val reportCnt: Int,
    val imagesUrl: List<String>?,
    val isLike: Boolean,
    var tags: List<ReqTagDTO>
)

