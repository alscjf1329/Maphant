package com.tovelop.maphant.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisService(@Autowired private val template: RedisTemplate<String, String>) {

    fun set(key: String, value: String) {
        val valueOperations = template.opsForValue()
        valueOperations.set(key, value)
//        dataStorage[key] = value
    }

    fun get(key: String): String? {
        val valueOperations = template.opsForValue()
        return valueOperations[key]
//        return dataStorage[key]
    }

    fun setnx(key: String, value: String): Boolean {
        if (template.hasKey(key)) {
            return false
        }

        set(key, value)
        return true

//        if(dataStorage.containsKey(key)) {
//            return false
//        }
//
//        dataStorage[key] = value
//        return true
    }

    fun del(key: String) {
        template.delete(key)
    }
    fun expire(key: String, timeout: Long) {
        template.expire(key, timeout, java.util.concurrent.TimeUnit.SECONDS)
    }
    fun setKeepTTL(key: String, value: String) {
        val timeout = template.getExpire(key)
        set(key, value)

        if (timeout >= 0) {
            expire(key, timeout)
        } else if(timeout == -1L) {
            template.delete(key)
        }
    }
}