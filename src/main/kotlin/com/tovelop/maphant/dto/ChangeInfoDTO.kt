package com.tovelop.maphant.dto

data class ChangeInfoDTO(
    val nickname: String?,
    val oldPassword: String?,
    val newPassword: String?,
    val newPasswordCheck: String?,
    val phNum: String?,
    val category: String?,
    val major: String?
)
