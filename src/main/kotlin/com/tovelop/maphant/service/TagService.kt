package com.tovelop.maphant.service

import com.tovelop.maphant.dto.BoardSearchResponseDto
import com.tovelop.maphant.mapper.TagMapper
import com.tovelop.maphant.type.paging.Pagination
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(private val tagMapper: TagMapper) {
    fun insertTag(categoryId: Int, boardId: Int, tagNames: List<String>) {
        tagNames.forEach {
            tagMapper.insertTag(categoryId, boardId, it.replace("[^\\p{L}\\p{N}_]".toRegex(), ""))
        }
    }

    fun deleteTag(boardId: Int, tagId: Int){
        if(tagMapper.getTagExistenceInBoard(boardId, tagId)>0){
            return tagMapper.deleteTag(boardId, tagId)
        }
    }

    @Transactional
    fun modifyTag(categoryId: Int, boardId: Int, newTags:List<String>){
        val tags = tagMapper.getTagListByBoardId(boardId)

        tags.forEach{tag ->
            if(tagMapper.getTagCnt(tag.id)>0) {
                tagMapper.minusTagCnt(tag.id)
            }
        }
        tagMapper.deleteBoardTag(boardId)

        newTags.forEach{
            newTag ->
                tagMapper.insertTag(categoryId, boardId, newTag.replace("[^\\p{L}\\p{N}_]".toRegex(), ""))

                val tagId = tagMapper.getTagId(newTag.replace("[^\\p{L}\\p{N}_]".toRegex(), ""))
                tagMapper.insertBoardTag(boardId,tagId)
        }
    }

    fun insertBoardTag(boardId: Int, tagId: Int) = tagMapper.insertBoardTag(boardId, tagId)

    fun getTagByName(name: String) = tagMapper.getTagByName(name.replace("[^\\p{L}\\p{N}_]".toRegex(), ""))

    fun getTagList() = tagMapper.getTagList()

    @Transactional
    fun deleteTagCnt(boardId: Int){
        val tags = tagMapper.getTagListByBoardId(boardId)

        tagMapper.deleteBoardTag(boardId)
        tags.forEach{tag ->
            if(tagMapper.getTagCnt(tag.id)>0) {
                tagMapper.minusTagCnt(tag.id)
            }
        }
    }

    fun getBoardListWithTag(tagName:String, userId:Int, categoryId: Int, pagingDto: PagingDto): PagingResponse<BoardSearchResponseDto> {

        val count = tagMapper.countBoardByTagName(tagName,categoryId,userId)
        val boardList = tagMapper.findBoardByTagName(tagName, userId, categoryId, pagingDto);
        val pagination = Pagination(count, pagingDto)

        return PagingResponse(boardList,pagination)
    }
}