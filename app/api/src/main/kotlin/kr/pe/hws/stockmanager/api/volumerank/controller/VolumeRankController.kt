package kr.pe.hws.stockmanager.api.volumerank.controller

import kr.pe.hws.stockmanager.api.config.dto.BaseResponse
import kr.pe.hws.stockmanager.api.volumerank.dto.VolumeRankResponse
import kr.pe.hws.stockmanager.api.volumerank.service.VolumeRankService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class VolumeRankController(
    private val volumeRankService: VolumeRankService
) {
    @GetMapping("/v1/volume-rank/kr")
    fun getVolumeRank(): VolumeRankResponse {
        return VolumeRankResponse(
            data = volumeRankService.getVolumeRanks(),
            baseResponse = BaseResponse.success()
        )
    }
}