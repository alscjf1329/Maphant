package com.tovelop.maphant.service

import com.tovelop.maphant.dto.BoardSearchResponseDto
import com.tovelop.maphant.dto.SearchWordInverseDto
import com.tovelop.maphant.mapper.BoardMapper
import com.tovelop.maphant.mapper.SearchWordInverseMapper
import com.tovelop.maphant.mapper.SearchWordMapper
import com.tovelop.maphant.type.paging.Pagination
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.ln

@Service
class SearchService(private val searchWordMapper: SearchWordMapper,
                    private val searchWordInverseMapper: SearchWordInverseMapper,
                    private val boardMapper: BoardMapper) {

    @Transactional
    fun create(boardId: Int, title: String, content: String, tags: List<String>?){
        /**
         * 1. 연속된 2글자씩 자르기
         *      -  같은 글자들은 횟수(tf) 세주기
         * 2. search_word 에 저x장
         *      - 해당 테이블에 없는 데이터면 df는 1
         *      - 해당 테이블에 있는 데이터면 df + 1
         *      - return 되는 값이 search_word_id
         *      - 구한 결과를 search_word_inverse 테이블에 저장
         *            - tf -> 1에서 구한 tf로 저장
         *            - idf -> idf를 searchWordInverse테이블에 저장
         */
        var combinedString = "$title $content "
        if(tags != null) {
            combinedString += tags.joinToString(" ")
        }

        val searchKeywordMap = splitAndCount(combinedString) //Map<String, Int>
        val wordList =searchKeywordMap.keys.toList()
        val boardCount = boardMapper.getCountAllBoards()

        if(wordList.isEmpty()) return

        searchWordMapper.insertSearchWords(wordList)
        val searchWords = searchWordMapper.findByWords(wordList)

        val inverseDataList = searchWords?.map { searchWordDto ->
            SearchWordInverseDto(
                boardId,
                searchWordDto.id,
                tf = searchKeywordMap[searchWordDto.word]!!,
                idf = ln((boardCount / (1 + searchWordDto.df)).toDouble())
            )
        }

        searchWordInverseMapper.insertSearchWordInverses(inverseDataList)
    }

    fun search(
        searchKeyword: String,
        userId: Int,
        categoryId: Int,
        boardTypeId: Int?,
        pagingDto: PagingDto
    ): PagingResponse<BoardSearchResponseDto> {
        /**
         * 1. keyword 연속된 2글자씩 자르기 (구한 2글자를 토근이란 명칭이로 가정)
         * 2. 토큰을 search_word select -> id, word, df 값이 구해짐
         * 3. search_word_Inverse 테이블도 select (where search_word_id)
         *         - board_id, tf
         * 4. 각 board_id 마다 tf * idf(구해주면서)
         * 5. 토큰마다 구한 tf*idf 를 board_id 마다 합산
         * 6. 내림차순으로 정렬 후 반환
         */
        // 연속된 2글자로 자르기
        val searchKeywordMap = splitAndCount(searchKeyword)
        val searchKeywordList = searchKeywordMap.keys.toList()

        val count = searchWordInverseMapper.getCountSearchBoardListByWords(searchKeywordList, categoryId, boardTypeId)

        val pagination = Pagination(count, pagingDto)

        val boards = searchWordInverseMapper.searchBoardListByWords(
            searchKeywordList,
            userId,
            categoryId,
            boardTypeId,
            pagingDto
        )

        return PagingResponse(boards, pagination)

    }

    @Transactional
    fun update(boardId:Int, title: String, content: String, tags: List<String>?) {
        delete(boardId)
        create(boardId, title, content, tags)
    }

    @Transactional
    fun delete(boardId: Int) {
        searchWordMapper.updateDfByBoardId(boardId)
        searchWordInverseMapper.deleteByBoardId(boardId)
    }

    fun getTfIdf(tf: Int, df: Int): Double {
        val n = boardMapper.getCountAllBoards().toDouble()
        val idf = ln(n / (1 + df))

        return tf * idf
    }

    @Scheduled(cron = "0 0 3 1/1 * ?") //매일 03:00에 실행
    fun updateIdf() {
        val boardCount = boardMapper.getCountAllBoards()
        searchWordInverseMapper.updateIdf(boardCount)
    }

    fun splitAndCount(input: String): Map<String, Int> {
        val wordMap = mutableMapOf<String, Int>()

        input.lowercase().trim().split(" ").forEach { word ->
            for (i in 0 until word.length - 1) {
                val twoChar = word.substring(i, i + 2)
                wordMap[twoChar] = wordMap.getOrDefault(twoChar, 0) + 1
            }
        }

        return wordMap
    }
}