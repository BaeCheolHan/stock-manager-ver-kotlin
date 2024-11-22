package kr.pe.hws.stockmanager.webadapter.fetcher

import kr.pe.hws.stockmanager.domain.kis.constants.IndexType
import kr.pe.hws.stockmanager.domain.kis.index.IndexChart
import kr.pe.hws.stockmanager.webadapter.constants.KisApiTransactionId
import kr.pe.hws.stockmanager.webadapter.dto.KisApiIndexChartDto
import kr.pe.hws.stockmanager.webadapter.dto.KisApiIndexChartDto.toDomain
import kr.pe.hws.stockmanager.webadapter.dto.KisApiRequests
import kr.pe.hws.stockmanager.webadapter.dto.KisApiVolumeRankDto
import kr.pe.hws.stockmanager.webadapter.feign.client.KisApiFeignClient
import kr.pe.hws.stockmanager.webadapter.utils.KisApiUtils
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class KisApiFetcher (
    private val kisApiUtils: KisApiUtils,
    private val kisApiFeignClient: KisApiFeignClient,
) {

    fun fetchKrIndexChart(indexType: IndexType): IndexChart.IndexChart {
        val headers = kisApiUtils.getDefaultApiHeader(KisApiTransactionId.KR_INDEX_CHART_PRICE.getTransactionId())
        val request = KisApiIndexChartDto.IndexChartPriceRequest(
            FID_COND_MRKT_DIV_CODE = "U",
            FID_INPUT_ISCD = indexType.code,
            FID_INPUT_DATE_1 =
            LocalDate.now().minusYears(100L).minusDays(1L).formatToDate("yyyyMMdd")
            ,
            FID_INPUT_DATE_2 = LocalDate.now().formatToDate("yyyyMMdd"),
            FID_PERIOD_DIV_CODE = "D"
        )


        val res = kisApiFeignClient.getKrInquireDailyIndexChart(headers, request)
        return res.toDomain(indexType)
    }

    fun fetchOverSeaIndexChart(indexType: IndexType): IndexChart.IndexChart {
        val headers = kisApiUtils.getDefaultApiHeader(KisApiTransactionId.OVER_SEA_INDEX_CHART_PRICE.getTransactionId())
        val request = KisApiIndexChartDto.IndexChartPriceRequest(
            FID_COND_MRKT_DIV_CODE = "N",
            FID_INPUT_ISCD = indexType.code,
            FID_INPUT_DATE_1 =
                LocalDate.now().minusYears(100L).minusDays(1L).formatToDate("yyyyMMdd")
            ,
            FID_INPUT_DATE_2 = LocalDate.now().formatToDate("yyyyMMdd"),
            FID_PERIOD_DIV_CODE = "D"
        )

        val resp = kisApiFeignClient.getOverSeaInquireDailyChart(headers, request)
        return resp.toDomain(indexType)
    }

    fun fetchKrStockVolumeRank(itemCode: String): KisApiVolumeRankDto.KrVolumeRankResponse {
        val headers = kisApiUtils.getDefaultApiHeader(KisApiTransactionId.KR_VOLUME_RANK.getTransactionId())

        val request = KisApiVolumeRankDto.KrVolumeRankRequest(
            FID_COND_MRKT_DIV_CODE = "J",
            FID_COND_SCR_DIV_CODE = "20171",
            FID_INPUT_ISCD = itemCode,
            FID_DIV_CLS_CODE = "0",
            FID_BLNG_CLS_CODE = "0",
            FID_TRGT_CLS_CODE = "111111111",
            FID_TRGT_EXLS_CLS_CODE = "000000",
            FID_INPUT_PRICE_1= "0",
            FID_INPUT_PRICE_2= "0",
            FID_VOL_CNT = "0",
            FID_INPUT_DATE_1 = "0"
        )

        val resp = kisApiFeignClient.getVolumeRank(headers, request)
        return resp
    }

    fun LocalDate.formatToDate(pattern: String): String {
        return this.format(DateTimeFormatter.ofPattern(pattern))
    }
}