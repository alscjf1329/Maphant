package com.tovelop.maphant.dto

data class BoardSearchDto(
    var content: String?,
    val boardTypeId: Int?,
    var tagName: String?
) {
    fun trimFields() {
        content = content?.trim()
        tagName = tagName?.trim()
    }
}
