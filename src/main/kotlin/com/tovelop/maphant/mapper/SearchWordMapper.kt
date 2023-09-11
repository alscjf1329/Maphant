package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.SearchWordDto
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface SearchWordMapper {
    fun getWordCnt(word:String):Int
    fun plusDfCnt(word: String)
    fun insertSearchWord(word: String)
    fun getIdByWord(word: String):Int
    fun getDfByWord(word: String):Int
    fun findSearchWordListByWord(words:List<String>): List<SearchWordDto>?

    fun findByWords(wordList: List<String>): List<SearchWordDto>?

    fun insertSearchWords(wordList: List<String>):Boolean
    fun updateDfByBoardId(boardId:Int):Boolean

}