package com.tovelop.maphant.dto

data class UserDataDTO(
    val id: Int = 0,
    val email: String = "",
    var password: String = "",
    val name: String = "",
    val nickname: String = "",
    val role: String = "",
    val category: List<UserDataCategoryDTO> = mutableListOf(),
    val profileImg: String? = null,
)
