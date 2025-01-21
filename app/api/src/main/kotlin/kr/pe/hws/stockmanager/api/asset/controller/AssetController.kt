package kr.pe.hws.stockmanager.api.asset.controller

import kr.pe.hws.stockmanager.api.asset.dto.AssetChartResponse
import kr.pe.hws.stockmanager.api.asset.service.AssetService
import kr.pe.hws.stockmanager.api.config.dto.BaseResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/asset")
class AssetController(
    private val service: AssetService
) {

    @GetMapping("/member/{memberId}/chart")
    fun getAssetChartByMemberId(@PathVariable memberId: String): AssetChartResponse {
        return AssetChartResponse(
            assetCharts = service.getAssetChartData(memberId),
            baseResponse = BaseResponse.success()
        )
    }
}