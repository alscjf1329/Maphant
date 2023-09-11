package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class PollInfoDTO(
    var title: String = "",
    var expireDate: LocalDateTime? = LocalDateTime.now(),
    var state: Char = 't',
    var selectedOptionId: Int? = null,
    var pollOptions: List<PollOption>? = null
)

data class PollOption(var optionId: Int = 0, var optionName: String = "", var optionCount: Int = 0)