package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.service.BlockService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import com.tovelop.maphant.type.response.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/block")
class BlockController(
    private val blockService: BlockService
){

    @PostMapping("/{blockId}")
    fun creatBlock(@PathVariable blockedId:Int): ResponseEntity<Response<String>>{
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken
        val userId: Int = auth.getUserId()

        blockService.block(userId, blockedId)
        return ResponseEntity.ok().body(Response.success("해당 유저를 차단하였습니다."))
    }
    @DeleteMapping("/{blockedId}")
    fun deleteBlock(@PathVariable blockedId:Int): ResponseEntity<Response<String>> {
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken
        val userId: Int = auth.getUserId()

        blockService.releaseBlock(userId, blockedId)
        return ResponseEntity.ok().body(Response.success("해당 유저를 차단 해제하였습니다."))
    }
}