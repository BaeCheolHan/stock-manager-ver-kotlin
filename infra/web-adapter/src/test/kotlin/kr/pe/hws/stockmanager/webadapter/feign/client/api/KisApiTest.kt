package kr.pe.hws.stockmanager.webadapter.feign.client.api

import kr.pe.hws.stockmanager.api.InternalApiApplication
import kr.pe.hws.stockmanager.api.kis.index.IndexType
import kr.pe.hws.stockmanager.webadapter.fetcher.KisApiFetcher
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
        println(chart)
    }

    @Test
    fun getKosdaqIndexChart() {
        val chart = fetcher.fetchKrIndexChart(IndexType.KOSDAQ)
        println(chart)
    }

    @Test
    fun getSnpIndexChart() {
        val chart = fetcher.fetchOverSeaIndexChart(IndexType.SNP500)
        println(chart)
    }

}