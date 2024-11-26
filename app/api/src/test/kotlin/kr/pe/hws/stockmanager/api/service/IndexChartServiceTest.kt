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

class ChartServiceTest {

    private val fetcher: KisApiFetcher = mock()
    private val indexChartRepository: IndexChartRepository = mock()
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
    fun `should return all index charts from Redis if data exists`() {
        // Given: 모든 지수의 Redis 데이터를 설정
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

            // When
            val result = service.getIndexCharts()

            // Then: 모든 지수 데이터가 반환되었는지 확인
            assertNotNull(result.kospiChart)
            assertNotNull(result.kosdaqChart)
            assertNotNull(result.snp500Chart)
            assertNotNull(result.nasdaqChart)
            assertNotNull(result.dawChart)
            assertNotNull(result.philadelphiaChart)

            // Verify: Redis만 조회하고 API는 호출되지 않음
            verify(indexChartRepository, times(allIndexTypes.size)).findById(anyString())
            verify(fetcher, never()).fetchKrIndexChart(any())
            verify(fetcher, never()).fetchOverSeaIndexChart(any())
        }
    }

    @Test
    fun `should fetch from API and save to Redis if data does not exist`() {
        // Given: Redis 데이터가 없는 경우와 API 응답 설정
        val allIndexTypes = listOf(
            IndexType.KOSPI,
            IndexType.KOSDAQ,
            IndexType.SNP500,
            IndexType.NASDAQ,
            IndexType.DAW,
            IndexType.PHILADELPHIA
        )

        // Redis Mock 설정
        `when`(indexChartRepository.findById(any(String::class.java))).thenReturn(Optional.empty())

        // API Mock 설정
        val mockApiResponses = allIndexTypes.associateWith { createMockIndexChart(it) }
        `when`(fetcher.fetchKrIndexChart(any(IndexType::class.java))).thenAnswer { invocation ->
            val indexType = invocation.getArgument<IndexType>(0)
            mockApiResponses[indexType]
        }
        `when`(fetcher.fetchKrIndexChart(any(IndexType::class.java))).thenAnswer { invocation ->
            val indexType = invocation.getArgument<IndexType>(0)
            mockApiResponses[indexType]
        }

        // When: API 호출 및 데이터 반환
        val result = service.getIndexCharts()

        // Then: 모든 지수 데이터가 API를 통해 반환되었는지 확인
        assertNotNull(result.kospiChart)
        assertNotNull(result.kosdaqChart)
        assertNotNull(result.snp500Chart)
        assertNotNull(result.nasdaqChart)
        assertNotNull(result.dawChart)
        assertNotNull(result.philadelphiaChart)

        // Verify: Redis 조회 실패 시 API 호출 및 Redis 저장 여부 확인
        verify(indexChartRepository, times(allIndexTypes.size)).findById(any())
        verify(fetcher, times(2)).fetchKrIndexChart(any())
        verify(fetcher, times(4)).fetchOverSeaIndexChart(any())
        verify(indexChartRepository, times(allIndexTypes.size)).save(any())
    }

    @Test
    fun `should handle mixed Redis and API data sources`() {
        // Given: 일부 데이터는 Redis에서, 일부 데이터는 API에서 가져오는 상황 설정
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

        // Then: Redis와 API 데이터를 적절히 가져왔는지 검증
        assertNotNull(result.kospiChart)
        assertNotNull(result.kosdaqChart)
        assertNotNull(result.snp500Chart)
        assertNotNull(result.nasdaqChart)
        assertNotNull(result.dawChart)
        assertNotNull(result.philadelphiaChart)

        // Verify: Redis에서 2개, API에서 4개 데이터 가져옴
        verify(indexChartRepository, times(6)).findById(anyString())
        verify(fetcher, times(4)).fetchOverSeaIndexChart(any())
        verify(fetcher, never()).fetchKrIndexChart(IndexType.KOSPI) // API 호출되지 않음
        verify(indexChartRepository, times(4)).save(any())
    }
}
