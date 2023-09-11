package com.tovelop.maphant.dto

data class ProfileDto(
    val user_id: Int,
    val profile_img: String?,
    val body: String?
)

data class ProfileCategoryDTO(
    val nickname: String? = "",
    val body: String? = "",
    val profileImg: String? = null,
    val category: List<UserDataCategoryDTO> = mutableListOf(),
)
