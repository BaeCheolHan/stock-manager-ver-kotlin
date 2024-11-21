package kr.pe.hws.stockmanager.webadapter.feign.client.api

import kr.pe.hws.stockmanager.api.InternalApiApplication
import kr.pe.hws.stockmanager.api.kis.index.IndexType
import kr.pe.hws.stockmanager.redis.repository.RestKisTokenRepository
import kr.pe.hws.stockmanager.webadapter.feign.client.KisApiFeignClient
import kr.pe.hws.stockmanager.webadapter.fetcher.KisApiFetcher
import kr.pe.hws.stockmanager.webadapter.utils.KisApiUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest(classes = [InternalApiApplication::class])
class KisApiTest {
    @Autowired
    private lateinit var restKisTokenRepositoryMocked: RestKisTokenRepository

    @Autowired
    private lateinit var fetcher: KisApiFetcher

    @Autowired
    private lateinit var kisApiFeignClient: KisApiFeignClient

    @Autowired
    private lateinit var kisApiUtils: KisApiUtils

    @Test
    fun getKrIndexChart() {
        val chart = fetcher.fetchKrIndexChart(IndexType.KOSPI)
        println(chart)
    }

    @Test
    fun getSnpIndexChart() {
        val chart = fetcher.fetchOverSeaIndexChart(IndexType.SNP500)
        println(chart)
    }

}