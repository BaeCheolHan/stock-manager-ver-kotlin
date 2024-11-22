package kr.pe.hws.stockmanager.webadapter.fetcher

import kr.pe.hws.stockmanager.domain.kis.constants.IndexType
import kr.pe.hws.stockmanager.domain.kis.index.IndexChart
import kr.pe.hws.stockmanager.domain.kis.stock.OverSeaStockPrice
import kr.pe.hws.stockmanager.domain.kis.stock.StockVolumeRank
import kr.pe.hws.stockmanager.webadapter.constants.KisApiTransactionId
import kr.pe.hws.stockmanager.webadapter.dto.KisApiIndexChartDto
import kr.pe.hws.stockmanager.webadapter.dto.KisApiIndexChartDto.toDomain
import kr.pe.hws.stockmanager.webadapter.dto.KisApiStockPriceDto
import kr.pe.hws.stockmanager.webadapter.dto.KisApiStockPriceDto.toDomain
import kr.pe.hws.stockmanager.webadapter.dto.KisApiVolumeRankDto
import kr.pe.hws.stockmanager.webadapter.dto.KisApiVolumeRankDto.toDomain
import kr.pe.hws.stockmanager.webadapter.feign.client.KisApiFeignClient
import kr.pe.hws.stockmanager.webadapter.utils.KisApiUtils
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class KisApiFetcher(
    private val kisApiUtils: KisApiUtils,
    private val kisApiFeignClient: KisApiFeignClient
) {

    fun fetchKrIndexChart(indexType: IndexType): IndexChart.IndexChart {
        val headers = kisApiUtils.createApiHeaders(KisApiTransactionId.KR_INDEX_CHART_PRICE.getTransactionId())
        val request = createIndexChartRequest("U", indexType.code, "D")
        val response = kisApiFeignClient.getKrInquireDailyIndexChart(headers, request)
        return response.toDomain(indexType)
    }

    fun fetchOverSeaIndexChart(indexType: IndexType): IndexChart.IndexChart {
        val headers = kisApiUtils.createApiHeaders(KisApiTransactionId.OVER_SEA_INDEX_CHART_PRICE.getTransactionId())
        val request = createIndexChartRequest("N", indexType.code, "D")
        val response = kisApiFeignClient.getOverSeaInquireDailyChart(headers, request)
        return response.toDomain(indexType)
    }

    fun fetchKrStockVolumeRank(itemCode: String): List<StockVolumeRank> {
        val headers = kisApiUtils.createApiHeaders(KisApiTransactionId.KR_VOLUME_RANK.getTransactionId())
        val request = createVolumeRankRequest(itemCode)
        return try {
            val response = kisApiFeignClient.getVolumeRank(headers, request)
            response.details.map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchOverSeaNowStockPrice(market: String, symbol: String) {
        val headers = kisApiUtils.createApiHeaders(KisApiTransactionId.KR_VOLUME_RANK.getTransactionId())
        headers.add("custtype", "P")
        val request = createOverSeaStockPriceRequest(market, symbol)
        val response = kisApiFeignClient.getOverSeaStockPrice(headers, request)
        println(response)
//        return response.details.toDomain()
    }

    private fun createIndexChartRequest(marketCode: String, indexCode: String, period: String): KisApiIndexChartDto.IndexChartPriceRequest {
        val today = LocalDate.now()
        return KisApiIndexChartDto.IndexChartPriceRequest(
            FID_COND_MRKT_DIV_CODE = marketCode,
            FID_INPUT_ISCD = indexCode,
            FID_INPUT_DATE_1 = today.minusYears(100).minusDays(1).formatToDate("yyyyMMdd"),
            FID_INPUT_DATE_2 = today.formatToDate("yyyyMMdd"),
            FID_PERIOD_DIV_CODE = period
        )
    }

    private fun createVolumeRankRequest(itemCode: String): KisApiVolumeRankDto.KrVolumeRankRequest {
        return KisApiVolumeRankDto.KrVolumeRankRequest(
            FID_COND_MRKT_DIV_CODE = "J",
            FID_COND_SCR_DIV_CODE = "20171",
            FID_INPUT_ISCD = itemCode,
            FID_DIV_CLS_CODE = "0",
            FID_BLNG_CLS_CODE = "0",
            FID_TRGT_CLS_CODE = "111111111",
            FID_TRGT_EXLS_CLS_CODE = "000000",
            FID_INPUT_PRICE_1 = "0",
            FID_INPUT_PRICE_2 = "0",
            FID_VOL_CNT = "0",
            FID_INPUT_DATE_1 = "0"
        )
    }

    private fun createOverSeaStockPriceRequest(market: String, symbol: String): KisApiStockPriceDto.OverSeaStockPriceRequest {
        return KisApiStockPriceDto.OverSeaStockPriceRequest(
            AUTH = "",
            EXCD = market,
            SYMB = symbol
        )
    }

    private fun LocalDate.formatToDate(pattern: String): String {
        return this.format(DateTimeFormatter.ofPattern(pattern))
    }
}