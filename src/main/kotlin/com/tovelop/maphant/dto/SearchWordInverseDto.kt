package com.tovelop.maphant.dto

data class SearchWordInverseDto(
    val board_id:Int,
    val search_word_id:Int,
    val tf:Int,
    val idf:Double
)
