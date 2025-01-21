package kr.pe.hws.stockmanager.api.chart.dto

import kr.pe.hws.stockmanager.api.config.dto.BaseResponse
import kr.pe.hws.stockmanager.domain.kis.index.IndexChartDomain

data class IndexChartResponseDto (
    val kospiChart: IndexChartDomain.IndexChart,
    val kosdaqChart: IndexChartDomain.IndexChart,

    val snp500Chart: IndexChartDomain.IndexChart,
    val nasdaqChart: IndexChartDomain.IndexChart,

    val dawChart: IndexChartDomain.IndexChart,
    val philadelphiaChart: IndexChartDomain.IndexChart,
    val baseResponse: BaseResponse
)