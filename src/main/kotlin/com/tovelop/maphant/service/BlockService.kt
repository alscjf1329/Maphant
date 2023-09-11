package com.tovelop.maphant.service

import com.tovelop.maphant.mapper.BlockMapper
import com.tovelop.maphant.mapper.UserMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service

class BlockService(
    private val blockMapper: BlockMapper,
    private val userMapper: UserMapper
) {
    @Transactional
    fun block(blockerId: Int, blockId:Int) {

        if(userMapper.countById(blockId) == 0) {
            throw IllegalStateException("차단하려는 id의 유저가 존재하지 않습니다.")
        }

        val count = blockMapper.getBlockCount(blockerId, blockId)

        if(count>=1){
            throw IllegalStateException("해당 유저를 이미 차단하였습니다.")
        }
        blockMapper.block(blockerId, blockId)
    }

    @Transactional
    fun releaseBlock(blockerId:Int, blockedId:Int) {

        //blockerId가 blockedId를 차단한 적 있는지 확인
        val count = blockMapper.getBlockCount(blockerId, blockedId)

        if(count == 0)
            throw IllegalStateException("해당 유저를 차단한적이 없습니다.")

        blockMapper.deleteBlock(blockerId,blockedId)
    }
}