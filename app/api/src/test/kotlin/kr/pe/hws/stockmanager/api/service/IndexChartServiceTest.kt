package kr.pe.hws.stockmanager.api.service

import kr.pe.hws.stockmanager.api.chart.service.ChartService
import kr.pe.hws.stockmanager.domain.kis.constants.IndexType
import kr.pe.hws.stockmanager.domain.kis.index.IndexChartDomain
import kr.pe.hws.stockmanager.redis.hash.ChartDetailRedisEntity
import kr.pe.hws.stockmanager.redis.hash.ChartSummaryRedisEntity
import kr.pe.hws.stockmanager.redis.hash.IndexChartRedisEntity
import kr.pe.hws.stockmanager.redis.repository.IndexChartRepository
import kr.pe.hws.stockmanager.webadapter.fetcher.KisApiFetcher
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.util.*
import org.mockito.kotlin.any

class ChartServiceTest {

    private val fetcher: KisApiFetcher = mock(KisApiFetcher::class.java)
    private val indexChartRepository: IndexChartRepository = mock(IndexChartRepository::class.java)
    private val service = ChartService(fetcher, indexChartRepository)

    private fun createMockRedisEntity(indexType: IndexType): IndexChartRedisEntity {
        return IndexChartRedisEntity(
            id = indexType.id,
            summary = ChartSummaryRedisEntity(
                currentPrice = BigDecimal("100.0"),
                highPrice = BigDecimal("120.0"),
                lowPrice = BigDecimal("90.0"),
                openPrice = BigDecimal("95.0"),
                previousClosePrice = BigDecimal("98.0"),
                tradingVolume = BigDecimal("10000.0"),
                priceChangeSign = "+",
                name = indexType.name
            ),
            details = listOf(
                ChartDetailRedisEntity(
                    date = "2024-01-01",
                    currentPrice = BigDecimal("100.0"),
                    openPrice = BigDecimal("95.0"),
                    highPrice = BigDecimal("120.0"),
                    lowPrice = BigDecimal("90.0"),
                    tradingVolume = BigDecimal("10000.0"),
                    modified = false
                )
            )
        )
    }

    private fun createMockIndexChart(indexType: IndexType): IndexChartDomain.IndexChart {
        return IndexChartDomain.IndexChart(
            indexType = indexType.name,
            summary = IndexChartDomain.ChartSummary(
                currentPrice = BigDecimal("100.0"),
                highPrice = BigDecimal("120.0"),
                lowPrice = BigDecimal("90.0"),
                openPrice = BigDecimal("95.0"),
                previousClosePrice = BigDecimal("98.0"),
                tradingVolume = BigDecimal("10000.0"),
                priceChangeSign = "+",
                name = indexType.name
            ),
            details = listOf(
                IndexChartDomain.ChartDetail(
                    date = "2024-01-01",
                    currentPrice = BigDecimal("100.0"),
                    openPrice = BigDecimal("95.0"),
                    highPrice = BigDecimal("120.0"),
                    lowPrice = BigDecimal("90.0"),
                    tradingVolume = BigDecimal("10000.0"),
                    modified = false
                )
            )
        )
    }

    @Test
    fun `should return charts from Redis if data exists`() {
        // Given: Mock Redis data for all indices
        val allIndexTypes = listOf(
            IndexType.KOSPI,
            IndexType.KOSDAQ,
            IndexType.SNP500,
            IndexType.NASDAQ,
            IndexType.DAW,
            IndexType.PHILADELPHIA
        )

        val mockRedisEntities = allIndexTypes.associateWith { createMockRedisEntity(it) }
        `when`(indexChartRepository.findById(any(String::class.java))).thenAnswer { invocation ->
            val id = invocation.getArgument<String>(0)
            Optional.ofNullable(mockRedisEntities[allIndexTypes.find { it.id == id }])
        }

        // When
        val result = service.getIndexCharts()

        // Then: All data is retrieved from Redis
        assertNotNull(result.kospiChart)
        assertNotNull(result.kosdaqChart)
        assertNotNull(result.snp500Chart)
        assertNotNull(result.nasdaqChart)
        assertNotNull(result.dawChart)
        assertNotNull(result.philadelphiaChart)

        // Verify Redis was queried, and no API calls were made
        verify(indexChartRepository, times(allIndexTypes.size)).findById(any(String::class.java))
        verify(fetcher, never()).fetchKrIndexChart(any())
        verify(fetcher, never()).fetchOverSeaIndexChart(any())
    }

    @Test
    fun `should fetch from API and save to Redis if data does not exist`() {
        // Given: Redis is empty and API provides data
        `when`(indexChartRepository.findById(anyString())).thenReturn(Optional.empty())

        val allIndexTypes = listOf(
            IndexType.KOSPI,
            IndexType.KOSDAQ,
            IndexType.SNP500,
            IndexType.NASDAQ,
            IndexType.DAW,
            IndexType.PHILADELPHIA
        )

        val mockApiResponses = allIndexTypes.associateWith { createMockIndexChart(it) }
        `when`(fetcher.fetchKrIndexChart(any())).thenAnswer { invocation ->
            val indexType = invocation.getArgument<IndexType>(0)
            mockApiResponses[indexType]
        }
        `when`(fetcher.fetchOverSeaIndexChart(any())).thenAnswer { invocation ->
            val indexType = invocation.getArgument<IndexType>(0)
            mockApiResponses[indexType]
        }

        // When
        val result = service.getIndexCharts()

        // Then: All data is fetched from API and saved to Redis
        assertNotNull(result.kospiChart)
        assertNotNull(result.kosdaqChart)
        assertNotNull(result.snp500Chart)
        assertNotNull(result.nasdaqChart)
        assertNotNull(result.dawChart)
        assertNotNull(result.philadelphiaChart)

        // Verify API was called for all indices and Redis saved the data
        verify(indexChartRepository, times(allIndexTypes.size)).findById(anyString())
        verify(fetcher, times(2)).fetchKrIndexChart(any())
        verify(fetcher, times(4)).fetchOverSeaIndexChart(any())
        verify(indexChartRepository, times(allIndexTypes.size)).save(any())
    }

    @Test
    fun `should handle mixed Redis and API data sources`() {
        // Given: Some data in Redis, others fetched from API
        val redisIndexTypes = listOf(IndexType.KOSPI, IndexType.KOSDAQ)
        val apiIndexTypes = listOf(IndexType.SNP500, IndexType.NASDAQ, IndexType.DAW, IndexType.PHILADELPHIA)

        val mockRedisEntities = redisIndexTypes.associateWith { createMockRedisEntity(it) }
        `when`(indexChartRepository.findById(anyString())).thenAnswer { invocation ->
            val id = invocation.getArgument<String>(0)
            Optional.ofNullable(mockRedisEntities[redisIndexTypes.find { it.id == id }])
        }

        val mockApiResponses = apiIndexTypes.associateWith { createMockIndexChart(it) }
        `when`(fetcher.fetchKrIndexChart(any())).thenAnswer { invocation ->
            val indexType = invocation.getArgument<IndexType>(0)
            mockApiResponses[indexType]
        }
        `when`(fetcher.fetchOverSeaIndexChart(any())).thenAnswer { invocation ->
            val indexType = invocation.getArgument<IndexType>(0)
            mockApiResponses[indexType]
        }

        // When
        val result = service.getIndexCharts()

        // Then: Redis and API data are correctly combined
        assertNotNull(result.kospiChart)
        assertNotNull(result.kosdaqChart)
        assertNotNull(result.snp500Chart)
        assertNotNull(result.nasdaqChart)
        assertNotNull(result.dawChart)
        assertNotNull(result.philadelphiaChart)

        // Verify Redis and API were both used
        verify(indexChartRepository, times(6)).findById(anyString())
        verify(fetcher, times(4)).fetchOverSeaIndexChart(any())
        verify(fetcher, never()).fetchKrIndexChart(IndexType.KOSPI)
        verify(indexChartRepository, times(4)).save(any())
    }
}