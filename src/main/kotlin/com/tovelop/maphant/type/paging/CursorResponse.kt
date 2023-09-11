package com.tovelop.maphant.type.paging

import com.tovelop.maphant.dto.ResultDmDto

open class CursorResponse<T>(
    val list: List<T>,
    val nextCursor: Int?
) {
}