package com.tovelop.maphant.dto

data class UserDataDTO(
    val id: Int = 0,
    val email: String = "",
    var password: String = "",
    val name: String = "",
    val nickname: String = "",
    val studentNo: String = "",
    val role: String = "",
    val state: Int = 0,
    val category: List<UserDataCategoryDTO> = mutableListOf(),
    val profileImg: String? = null,
)
