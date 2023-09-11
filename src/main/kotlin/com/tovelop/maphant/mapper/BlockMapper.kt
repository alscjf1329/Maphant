package com.tovelop.maphant.mapper

import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface BlockMapper {

    fun block(blockerId: Int, blockedId: Int): Boolean

    fun deleteBlock(blockerId:Int, blockedId:Int) : Boolean

    fun getBlockCount(blockerId:Int, blockedId:Int): Int
}