package com.tovelop.maphant.service

import com.tovelop.maphant.dto.ReportDTO
import com.tovelop.maphant.mapper.ReportMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReportService(@Autowired private val reportMapper: ReportMapper) {
    fun getReportList(): List<ReportDTO> {
        return reportMapper.getReportList()
    }
}