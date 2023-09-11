package com.tovelop.maphant.mapper

import com.tovelop.maphant.dto.ReportDTO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface ReportMapper {
    fun getReportList(): List<ReportDTO>
}