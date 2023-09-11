package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.BoardSearchResponseDto
import com.tovelop.maphant.dto.ReqTagDTO
import com.tovelop.maphant.dto.TagDTO
import com.tovelop.maphant.type.paging.PagingDto
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface TagMapper {
    fun insertTag(categoryId: Int, boardId: Int, tagName: String):Int
    fun deleteTag(boardId: Int, tagId: Int)

    //게시글 수정 시 해당 함수의 반환값이 0일때만 태그 추가 가능
    fun getTagExistenceInBoard(boardId: Int, tagId: Int):Int
    fun insertBoardTag(boardId: Int, tagId: Int)
    fun deleteBoardTag(boardId: Int)
    fun getTagByName(name: String): TagDTO?
    fun getTagList(): List<TagDTO>
    
    //게시물에 포함된 태그 목록 불러오는 함수
    fun getTagListByBoardId(boardId: Int):List<TagDTO>
    fun minusTagCnt(id: Int)
    fun plusTagCnt(id: Int)
    fun getTagCnt(id:Int):Int
    fun getTagId(tagName: String):Int
    fun findBoardTags(boardId: Int): List<ReqTagDTO>

    fun findBoardByTagName(tagName: String, userId:Int, categoryId: Int, pagingDto: PagingDto): List<BoardSearchResponseDto>

    fun countBoardByTagName(tagName: String,categoryId: Int,userId:Int):Int
}