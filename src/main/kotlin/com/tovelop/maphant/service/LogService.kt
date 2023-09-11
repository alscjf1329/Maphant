package com.tovelop.maphant.service

import com.tovelop.maphant.mapper.LogMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LogService(@Autowired val logMapper: LogMapper) {
    fun login(userId: Int, userIp: String) {
        logMapper.insertLoginLog(userId, userIp)
    }
}