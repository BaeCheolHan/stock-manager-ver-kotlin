package kr.pe.hws.stockmanager.api.chart.controller

import kr.pe.hws.stockmanager.api.chart.dto.IndexChartResponseDto
import kr.pe.hws.stockmanager.api.chart.service.ChartService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chart")
class ChartController (
    private val service: ChartService
){
    @GetMapping("/v1/index")
    fun getIndexCharts(): IndexChartResponseDto {
        return service.getIndexCharts()
    }
}