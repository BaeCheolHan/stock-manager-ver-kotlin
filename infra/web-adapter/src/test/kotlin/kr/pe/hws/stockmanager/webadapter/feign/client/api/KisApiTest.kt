package kr.pe.hws.stockmanager.webadapter.feign.client.api

import kr.pe.hws.stockmanager.domain.InternalApiApplication
import kr.pe.hws.stockmanager.domain.kis.constants.IndexType
import kr.pe.hws.stockmanager.webadapter.fetcher.KisApiFetcher
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest(classes = [InternalApiApplication::class])
class KisApiTest {

    @Autowired
    private lateinit var fetcher: KisApiFetcher


    @Test
    fun getKrIndexChart() {
        val chart = fetcher.fetchKrIndexChart(IndexType.KOSPI)
        Assertions.assertNotNull(chart)
    }

    @Test
    fun getSnpIndexChart() {
        val chart = fetcher.fetchOverSeaIndexChart(IndexType.SNP500)
        Assertions.assertNotNull(chart)
    }

    @Test
    fun getKrStockVolumeRank() {
        val data = fetcher.fetchKrStockVolumeRank("0000")
        println(data)
        Assertions.assertNotNull(data)
    }

    @Test
    fun getOverSeaStockPrice() {
        val data = fetcher.fetchOverSeaNowStockPrice("NAS", "AAPL")
        println(data)
        Assertions.assertNotNull(data)
    }

}