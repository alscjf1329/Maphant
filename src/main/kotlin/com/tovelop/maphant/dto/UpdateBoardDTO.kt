package com.tovelop.maphant.dto

data class UpdateBoardDTO(
    val id: Int,
    var title: String,
    val body: String,
    val isHide: Int,
    val imagesUrl: String? = null,
    val tags: List<String>?
)

data class UpgradeUpdateBoardDTO(
    val id: Int,
    var title: String,
    var body: String,
    val isHide: Int,
    val imagesUrl: List<String>? = null,
    val tags: List<String>?
){
    fun toUpdateBoardDTO(): UpdateBoardDTO {
        return UpdateBoardDTO(
            id = id,
            title = title,
            body=body,
            isHide=isHide,
            imagesUrl = imagesUrl?.joinToString(","),
            tags = tags
        )
    }
}