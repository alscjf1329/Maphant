package com.tovelop.maphant.dto

data class InitBoardDto(
    val boardId:Int,
    val title: String,
    val body:String,
    val tagStrings:String?
) {
    val tags: List<String>
        get() = tagStrings?.split(",") ?: listOf()
}
