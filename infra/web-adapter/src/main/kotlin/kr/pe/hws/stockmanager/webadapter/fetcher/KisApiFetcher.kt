package kr.pe.hws.stockmanager.webadapter.fetcher

import kr.pe.hws.stockmanager.common.logger.LogHelper.getLogger
import kr.pe.hws.stockmanager.domain.kis.constants.IndexType
import kr.pe.hws.stockmanager.domain.kis.index.IndexChartDomain
import kr.pe.hws.stockmanager.domain.kis.stock.KrStockPrice
import kr.pe.hws.stockmanager.domain.kis.stock.OverSeaStockPrice
import kr.pe.hws.stockmanager.domain.kis.volumerank.KrVolumeRankDomain
import kr.pe.hws.stockmanager.webadapter.constants.KisApiTransactionId
import kr.pe.hws.stockmanager.webadapter.dto.KisApiIndexChartDto
import kr.pe.hws.stockmanager.webadapter.dto.KisApiIndexChartDto.toDomain
import kr.pe.hws.stockmanager.webadapter.dto.KisApiStockPriceDto
import kr.pe.hws.stockmanager.webadapter.dto.KisApiStockPriceDto.toDomain
import kr.pe.hws.stockmanager.webadapter.dto.KisApiVolumeRankDto
import kr.pe.hws.stockmanager.webadapter.dto.KisApiVolumeRankDto.toDomain
import kr.pe.hws.stockmanager.webadapter.feign.client.KisApiFeignClient
import kr.pe.hws.stockmanager.webadapter.utils.KisApiUtils
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class KisApiFetcher(
    private val kisApiUtils: KisApiUtils,
    private val kisApiFeignClient: KisApiFeignClient
) {
    private val log = getLogger<KisApiFetcher>()

    fun fetchKrIndexChart(indexType: IndexType, chartType: String): IndexChartDomain.IndexChart {
        return fetchAndHandle(
            transactionId = KisApiTransactionId.KR_INDEX_CHART_PRICE.getTransactionId(),
            request = { createIndexChartRequest("U", indexType.code, chartType) },
            apiCall = kisApiFeignClient::getKrInquireDailyIndexChart
        ) { response ->
            (response as KisApiIndexChartDto.KrIndexChartResponse).toDomain(indexType)
        }
    }

    fun fetchOverSeaIndexChart(indexType: IndexType, chartType: String): IndexChartDomain.IndexChart {
        return fetchAndHandle(
            transactionId = KisApiTransactionId.OVER_SEA_INDEX_CHART_PRICE.getTransactionId(),
            request = { createIndexChartRequest("N", indexType.code, chartType) },
            apiCall = kisApiFeignClient::getKrInquireDailyIndexChart
        ) { response ->
            (response as KisApiIndexChartDto.KrIndexChartResponse).toDomain(indexType)
        }
    }

    fun fetchKrStockVolumeRank(itemCode: String): List<KrVolumeRankDomain> {
        return fetchAndHandle(
            transactionId = KisApiTransactionId.KR_VOLUME_RANK.getTransactionId(),
            request = { createVolumeRankRequest(itemCode) },
            apiCall = kisApiFeignClient::getVolumeRank
        ) { response ->
            (response as KisApiVolumeRankDto.KrVolumeRankResponse).details.map { it.toDomain() }
        }
    }

    fun fetchKrNowStockPrice(symbol: String): KrStockPrice {
        return fetchAndHandle(
            transactionId = KisApiTransactionId.KR_STOCK_PRICE.getTransactionId(),
            request = { createKrStockPriceRequest(symbol) },
            apiCall = kisApiFeignClient::getKrStockPrice
        ) { response ->
            (response as KisApiStockPriceDto.KrNowStockPriceResponse).details.toDomain()
        }
    }

    fun fetchOverSeaNowStockPrice(market: String, symbol: String): OverSeaStockPrice {
        return fetchAndHandle(
            transactionId = KisApiTransactionId.OVER_SEA_STOCK_PRICE.getTransactionId(),
            request = { createOverSeaStockPriceRequest(market, symbol) },
            apiCall = kisApiFeignClient::getOverSeaStockPrice
        ) { response ->
            (response as KisApiStockPriceDto.OverSeaNowStockPriceResponse).details.toDomain()
        }
    }

    private inline fun <R, T> fetchAndHandle(
        transactionId: String,
        additionalHeaders: Map<String, String> = emptyMap(),
        request: () -> R,
        apiCall: (HttpHeaders, R) -> Any,
        responseMapper: (Any) -> T
    ): T {
        val headers = createHeaders(transactionId, additionalHeaders)
        val requestBody = request()
        return try {
            val response = apiCall(headers, requestBody)
            responseMapper(response)
        } catch (e: Exception) {
            log.error("Failed to fetch data for transactionId=$transactionId: ${e.message}", e)
            throw RuntimeException("API fetch failed for transactionId=$transactionId", e)
        }
    }

    private fun createHeaders(transactionId: String, additionalHeaders: Map<String, String> = emptyMap()): HttpHeaders {
        val headers = kisApiUtils.createApiHeaders(transactionId)
        additionalHeaders.forEach { (key, value) -> headers[key] = value }
        return headers
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

    private fun createKrStockPriceRequest(symbol: String): KisApiStockPriceDto.KrStockPriceRequest {
        return KisApiStockPriceDto.KrStockPriceRequest(
            fid_cond_mrkt_div_code = "J",
            fid_input_iscd = symbol
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