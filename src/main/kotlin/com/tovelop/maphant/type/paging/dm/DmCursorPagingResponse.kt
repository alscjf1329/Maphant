package com.tovelop.maphant.type.paging.dm

import com.tovelop.maphant.type.paging.CursorResponse

class DmCursorPagingResponse<T>(
    other_id: Int,
    other_nickname: String,
    list: List<T> = mutableListOf(),
    nextCursor:Int? = null
): CursorResponse<T>(list,nextCursor) {
    // 새로운 클래스의 멤버들
    val other_id = other_id
    val other_nickname = other_nickname
}