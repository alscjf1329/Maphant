package com.tovelop.maphant.dto

import java.time.LocalDate

data class UserDTO(
    val id: Int? = null,
    val univId: Int?,
    val email: String,
    var password: String,
    val nickname: String,
    val name: String,
    val sno: String,
    val phNum: String?,
    val state: Int,
    val role: String,
    val agreedAt: LocalDate? = null,
    val createdAt: LocalDate? = null,
    val lastmodifiedAt: LocalDate? = null
)
