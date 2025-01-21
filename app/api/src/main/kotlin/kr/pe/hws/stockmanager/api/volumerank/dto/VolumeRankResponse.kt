package kr.pe.hws.stockmanager.api.volumerank.dto

import kr.pe.hws.stockmanager.api.config.dto.BaseResponse

data class VolumeRankResponse(
    val data: List<VolumeRankDto>,
    val baseResponse: BaseResponse
)