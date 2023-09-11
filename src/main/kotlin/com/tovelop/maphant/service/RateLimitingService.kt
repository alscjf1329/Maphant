package com.tovelop.maphant.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RateLimitingService(@Autowired val redisService: RedisService) {
    fun requestCheck(userId: Int, requestType: String){
        val incValue = when(requestType){
            "WRITE_POST" -> 2
            "WRITE_COMMENT" -> 1
            "INSERT_PICTURE" -> 2
            else -> return
        }
        val key = "WRITE_REQUEST|$userId"
        val value = (redisService.get(key)?.toIntOrNull() ?: 0) + incValue
        redisService.setKeepTTL(key, (value).toString())
        if (value == incValue) {
            // 댓글, 글 첫 요청시 1분간 유효 처리정
            redisService.expire(key, 60)
        }
        // 1분간 요청이 댓글, 사진 5번이상이면 , 글 포함해서 10번이상이면 5분 차단
        if (value >= 10 && redisService.get("BAN|$userId") == null) {
            redisService.set("BAN|$userId", "true")
            redisService.expire("BAN|$userId", 60 * 5)
        }
    }
    fun isBanned(userId: Int): Boolean{
        val banKey = "BAN|$userId"
        return redisService.get(banKey) != null
    }
}