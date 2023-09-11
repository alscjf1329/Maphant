package com.tovelop.maphant.type.paging

import jakarta.validation.constraints.Min

data class CursorDto (
    @field:Min(value = 0, message = "cursor는 0보다 크거나 같아야합니다.")
    val cursor:Int = 0,
    @field:Min(value = 1, message = "limit는 1보다 크거나 같아야합니다.")
    val limit: Int = 10
)