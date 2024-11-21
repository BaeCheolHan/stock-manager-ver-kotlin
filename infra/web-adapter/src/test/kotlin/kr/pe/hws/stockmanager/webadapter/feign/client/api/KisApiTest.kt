package kr.pe.hws.stockmanager.webadapter.feign.client.api

import kr.pe.hws.stockmanager.api.InternalApiApplication
import kr.pe.hws.stockmanager.api.kis.DailyIndexChartPriceWrapper.toDomain
import kr.pe.hws.stockmanager.api.kis.index.IndexType
import kr.pe.hws.stockmanager.redis.hash.RestKisToken
import kr.pe.hws.stockmanager.redis.repository.RestKisTokenRepository
import kr.pe.hws.stockmanager.webadapter.constants.KisApiTransactionId
import kr.pe.hws.stockmanager.webadapter.feign.client.KisApiFeignClient
import kr.pe.hws.stockmanager.webadapter.feign.client.KisApiRequest
import kr.pe.hws.stockmanager.webadapter.utils.KisApiUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.test.Test

@SpringBootTest(classes = [InternalApiApplication::class])
class KisApiTest {
    @Autowired
    private lateinit var restKisTokenRepositoryMocked: RestKisTokenRepository

    @Autowired
    private lateinit var kisApiFeignClient: KisApiFeignClient

    @Autowired
    private lateinit var kisApiUtils: KisApiUtils

    @Test
    fun getKrIndexChart() {
        val headers = kisApiUtils.getDefaultApiHeader(KisApiTransactionId.KR_INDEX_CHART_PRICE.getTransactionId())
        val request = KisApiRequest.DailyIndexChartPriceRequest(
            FID_COND_MRKT_DIV_CODE = "U",
            FID_INPUT_ISCD = IndexType.KOSPI.code,
            FID_INPUT_DATE_1 = KisApiRequest.DailyIndexChartPriceRequest.getFormattedDate(
                LocalDate.now().minusYears(100L).minusDays(1L)
            ),
            FID_INPUT_DATE_2 = KisApiRequest.DailyIndexChartPriceRequest.getFormattedDate(LocalDate.now()),
            FID_PERIOD_DIV_CODE = "D"
        )

        val res = kisApiFeignClient.getKrInquireDailyIndexChart(headers, request)
        val chart = res.toDomain(IndexType.KOSPI)
        println(chart)
    }

    @Test
    fun getSnpIndexChart() {
        val headers = kisApiUtils.getDefaultApiHeader(KisApiTransactionId.OVER_SEA_INDEX_CHART_PRICE.getTransactionId())
        val request = KisApiRequest.DailyIndexChartPriceRequest(
            "N",
            IndexType.SNP500.code,
            LocalDate.now().minusYears(100L).minusDays(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd")),
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
            "D",
        )

        val resp = kisApiFeignClient.getOverSeaInquireDailyChart(headers, request)
        val chart = resp.toDomain(IndexType.SNP500)
        println(chart)

    }

    private fun getKisToken() : RestKisToken {
        val token = restKisTokenRepositoryMocked.findAll()
            .filterNotNull() // null 값을 제거
            .firstOrNull()   // 첫 번째 요소를 반환하거나 null 반환
        println(token.toString())
        return token!!
    }
}