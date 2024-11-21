package kr.pe.hws.stockmanager.webadapter.fetcher

import kr.pe.hws.stockmanager.api.kis.DailyIndexChartPriceWrapper.toDomain
import kr.pe.hws.stockmanager.api.kis.index.IndexChart
import kr.pe.hws.stockmanager.api.kis.index.IndexType
import kr.pe.hws.stockmanager.webadapter.constants.KisApiTransactionId
import kr.pe.hws.stockmanager.webadapter.feign.client.KisApiFeignClient
import kr.pe.hws.stockmanager.webadapter.feign.client.KisApiRequest
import kr.pe.hws.stockmanager.webadapter.utils.KisApiUtils
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class KisApiFetcher (
    private val kisApiUtils: KisApiUtils,
    private val kisApiFeignClient: KisApiFeignClient,
) {

    fun fetchKrIndexChart(indexType: IndexType) : IndexChart.DailyIndexChart {
        val headers = kisApiUtils.getDefaultApiHeader(KisApiTransactionId.KR_INDEX_CHART_PRICE.getTransactionId())
        val request = KisApiRequest.DailyIndexChartPriceRequest(
            FID_COND_MRKT_DIV_CODE = "U",
            FID_INPUT_ISCD = indexType.code,
            FID_INPUT_DATE_1 =
            LocalDate.now().minusYears(100L).minusDays(1L).formatToDate()
            ,
            FID_INPUT_DATE_2 = LocalDate.now().formatToDate(),
            FID_PERIOD_DIV_CODE = "D"
        )


        val res = kisApiFeignClient.getKrInquireDailyIndexChart(headers, request)
        return res.toDomain(indexType)
    }

    fun fetchOverSeaIndexChart(indexType: IndexType) : IndexChart.DailyIndexChart {
        val headers = kisApiUtils.getDefaultApiHeader(KisApiTransactionId.OVER_SEA_INDEX_CHART_PRICE.getTransactionId())
        val request = KisApiRequest.DailyIndexChartPriceRequest(
            FID_COND_MRKT_DIV_CODE = "N",
            FID_INPUT_ISCD = indexType.code,
            FID_INPUT_DATE_1 =
                LocalDate.now().minusYears(100L).minusDays(1L).formatToDate()
            ,
            FID_INPUT_DATE_2 = LocalDate.now().formatToDate(),
            FID_PERIOD_DIV_CODE = "D"
        )

        val resp = kisApiFeignClient.getOverSeaInquireDailyChart(headers, request)
        return resp.toDomain(indexType)
    }

    fun LocalDate.formatToDate(): String {
        return this.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    }
}