package kr.pe.hws.stockmanager.api.asset.dto

import kr.pe.hws.stockmanager.api.config.dto.BaseResponse

data class AssetChartResponse(
    val assetCharts: AssetChartDto,
    val baseResponse: BaseResponse
)