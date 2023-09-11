package com.tovelop.maphant.controller

import com.tovelop.maphant.dto.ReportDTO
import com.tovelop.maphant.service.ReportService
import com.tovelop.maphant.type.response.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/report")
class ReportController(val reportService: ReportService) {
    @GetMapping("/list")
    fun getReportList(): ResponseEntity<Response<List<ReportDTO>>> {
        return ResponseEntity.ok()
            .body(Response.success(reportService.getReportList()))
    }
}