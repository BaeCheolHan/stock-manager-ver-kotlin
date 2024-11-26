package kr.pe.hws.stockmanager.webadapter.feign.client.api

import kr.pe.hws.stockmanager.api.InternalApiApplication
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
    fun getKospiIndexChart() {
        val chart = fetcher.fetchKrIndexChart(IndexType.KOSPI)
        Assertions.assertNotNull(chart)
        println(chart)
    }

    @Test
    fun getKosdaqIndexChart() {
        val chart = fetcher.fetchKrIndexChart(IndexType.KOSDAQ)
        Assertions.assertNotNull(chart)
        println(chart)
    }

    @Test
    fun getSnpIndexChart() {
        val chart = fetcher.fetchOverSeaIndexChart(IndexType.SNP500)
        Assertions.assertNotNull(chart)
        println(chart)
    }

    @Test
    fun getNasdaqIndexChart() {
        val chart = fetcher.fetchOverSeaIndexChart(IndexType.NASDAQ)
        Assertions.assertNotNull(chart)
        println(chart)
    }

    @Test
    fun getPhiladelphiaIndexChart() {
        val chart = fetcher.fetchOverSeaIndexChart(IndexType.PHILADELPHIA)
        Assertions.assertNotNull(chart)
        println(chart)
    }

    @Test
    fun getKrVolumeRank() {
        val data = fetcher.fetchKrStockVolumeRank("0000")
        Assertions.assertNotNull(data)
        println(data)
    }

    @Test
    fun getKrStockPrice() {
        val data = fetcher.fetchKrNowStockPrice("005930")
        Assertions.assertNotNull(data)
        println(data)
    }

    @Test
    fun getOverSeaStockPrice() {
        val data = fetcher.fetchOverSeaNowStockPrice("NYS", "O")
        Assertions.assertNotNull(data)
        println(data)
    }

}