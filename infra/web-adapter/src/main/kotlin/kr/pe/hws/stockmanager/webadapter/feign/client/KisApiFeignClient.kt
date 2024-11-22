package kr.pe.hws.stockmanager.webadapter.feign.client

import kr.pe.hws.stockmanager.domain.token.ApiToken
import kr.pe.hws.stockmanager.webadapter.config.StockManagerFeignClientConfig
import kr.pe.hws.stockmanager.webadapter.dto.KisApiIndexChartDto
import kr.pe.hws.stockmanager.webadapter.dto.KisApiRequests
import kr.pe.hws.stockmanager.webadapter.dto.KisApiStockPriceDto
import kr.pe.hws.stockmanager.webadapter.dto.KisApiVolumeRankDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "kisApiFeignClient",
    url = "https://openapi.koreainvestment.com:9443",
    configuration = [StockManagerFeignClientConfig::class],
)
interface KisApiFeignClient {
    @PostMapping("/oauth2/tokenP", consumes = ["application/json"])
    fun getKisApiToken(@RequestBody param: KisApiRequests.KisTokenGenerateRequest): ApiToken.KisToken

    // 해외 개별 주식 상세
//    @GetMapping("/uapi/overseas-price/v1/quotations/price-detail")
//    fun getOverSeaStockPrice(
//        @RequestHeader header: HttpHeaders,
//        @SpringQueryMap param: KisApiStockPriceDto.OverSeaStockPriceRequest,
//    ): KisApiStockPriceDto.OverSeaNowStockPriceResponse
    @GetMapping("/uapi/overseas-price/v1/quotations/price-detail")
    fun getOverSeaStockPrice(
        @RequestHeader header: HttpHeaders,
        @SpringQueryMap param: KisApiStockPriceDto.OverSeaStockPriceRequest,
    ): String
    // 국내 개별 주식 상세
    @GetMapping("/uapi/domestic-stock/v1/quotations/inquire-price")
    fun getKrStockPrice(
        @RequestHeader header: HttpHeaders,
        @SpringQueryMap param: KisApiStockPriceDto.KrStockPriceRequest,
    )

    // 국내 지수 api
    @GetMapping("/uapi/domestic-stock/v1/quotations/inquire-daily-indexchartprice")
    fun getKrInquireDailyIndexChart(
        @RequestHeader header: HttpHeaders,
        @SpringQueryMap param: KisApiIndexChartDto.IndexChartPriceRequest,
    ): KisApiIndexChartDto.KrIndexChartResponse

    // 해외 지수 api
    @GetMapping("/uapi/overseas-price/v1/quotations/inquire-daily-chartprice")
    fun getOverSeaInquireDailyChart(
        @RequestHeader header: HttpHeaders,
        @SpringQueryMap param: KisApiIndexChartDto.IndexChartPriceRequest,
    ): KisApiIndexChartDto.OverSeaIndexChartResponse

    // 주식 거래량 순위 api
    @GetMapping("uapi/domestic-stock/v1/quotations/volume-rank")
    fun getVolumeRank(@RequestHeader header: HttpHeaders, @SpringQueryMap param: KisApiVolumeRankDto.KrVolumeRankRequest
    ): KisApiVolumeRankDto.KrVolumeRankResponse

}


