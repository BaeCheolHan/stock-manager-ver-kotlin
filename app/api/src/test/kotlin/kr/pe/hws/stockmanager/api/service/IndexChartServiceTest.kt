package kr.pe.hws.stockmanager.api.service

import kr.pe.hws.stockmanager.api.chart.service.ChartService
import kr.pe.hws.stockmanager.domain.kis.constants.IndexType
import kr.pe.hws.stockmanager.domain.kis.index.IndexChartDomain
import kr.pe.hws.stockmanager.redis.hash.ChartDetailRedisEntity
import kr.pe.hws.stockmanager.redis.hash.ChartSummaryRedisEntity
import kr.pe.hws.stockmanager.redis.hash.IndexChartRedisEntity
import kr.pe.hws.stockmanager.redis.mapper.IndexChartMapper
import kr.pe.hws.stockmanager.redis.repository.IndexChartRepository
import kr.pe.hws.stockmanager.webadapter.fetcher.KisApiFetcher
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.util.*

class ChartServiceTest {

    private val fetcher: KisApiFetcher = mock()
    private val indexChartRepository: IndexChartRepository = mock()
    private val service = ChartService(fetcher, indexChartRepository)

    @Test
    fun `should throw exception when API response is null`() {
        // Given: API 응답이 null
        `when`(indexChartRepository.findById("KOSPI")).thenReturn(Optional.empty())
        `when`(fetcher.fetchKrIndexChart(IndexType.KOSPI)).thenReturn(null)

        // When / Then: 예외 발생 확인
        val exception = assertThrows<IllegalStateException> {
            service.getIndexCharts()
        }
        assertEquals("API response for KOSPI was null", exception.message)
    }

    @Test
    fun `should return charts from Redis if data exists`() {
        // Given: Mock Redis entity with summary and details
        val mockSummary = ChartSummaryRedisEntity(
            currentPrice = BigDecimal("100.0"),
            highPrice = BigDecimal("120.0"),
            lowPrice = BigDecimal("90.0"),
            openPrice = BigDecimal("95.0"),
            previousClosePrice = BigDecimal("98.0"),
            tradingVolume = BigDecimal("10000.0"),
            priceChangeSign = "+",
            name = "KOSPI"
        )

        val mockDetails = listOf(
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

        val mockRedisEntityKOSPI = IndexChartRedisEntity(
            id = "KOSPI",
            summary = mockSummary,
            details = mockDetails
        )
        val mockRedisEntityKOSDAQ = IndexChartRedisEntity(
            id = "KOSDAQ",
            summary = mockSummary,
            details = mockDetails
        )

        // Mock Redis 데이터 설정
        `when`(indexChartRepository.findById("KOSPI")).thenReturn(Optional.of(mockRedisEntityKOSPI))
        `when`(indexChartRepository.findById("KOSDAQ")).thenReturn(Optional.of(mockRedisEntityKOSDAQ))

        // Mock API 반환값 설정 (필요 없는 상태, 하지만 방어적으로 추가 가능)
        `when`(fetcher.fetchKrIndexChart(IndexType.KOSPI)).thenReturn(
            IndexChartDomain.IndexChart(
                indexType = "KOSPI",
                summary = IndexChartDomain.ChartSummary(
                    currentPrice = BigDecimal("100.0"),
                    highPrice = BigDecimal("120.0"),
                    lowPrice = BigDecimal("90.0"),
                    openPrice = BigDecimal("95.0"),
                    previousClosePrice = BigDecimal("98.0"),
                    tradingVolume = BigDecimal("10000.0"),
                    priceChangeSign = "+",
                    name = "KOSPI"
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
        )

        // Mock API 반환값 설정 (필요 없는 상태, 하지만 방어적으로 추가 가능)
        `when`(fetcher.fetchKrIndexChart(IndexType.KOSDAQ)).thenReturn(
            IndexChartDomain.IndexChart(
                indexType = "KOSDAQ",
                summary = IndexChartDomain.ChartSummary(
                    currentPrice = BigDecimal("100.0"),
                    highPrice = BigDecimal("120.0"),
                    lowPrice = BigDecimal("90.0"),
                    openPrice = BigDecimal("95.0"),
                    previousClosePrice = BigDecimal("98.0"),
                    tradingVolume = BigDecimal("10000.0"),
                    priceChangeSign = "+",
                    name = "KOSDAQ"
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
        )

        // Expected 결과 생성
        val expectedChartKOSPI = IndexChartMapper.fromRedisEntity(mockRedisEntityKOSPI)

        // When: Service retrieves the index chart
        val result = service.getIndexCharts()

        // Then: Ensure the Redis data is correctly returned
        assertNotNull(result.kospi) // Redis에서 반환된 데이터가 null이 아님을 검증
        assertEquals(expectedChartKOSPI.summary.currentPrice, result.kospi.summary.currentPrice) // 데이터 일치 검증

        // API 호출 여부 검증: Redis 데이터가 있을 때는 API 호출이 발생하지 않아야 함
        verify(fetcher, never()).fetchKrIndexChart(IndexType.KOSPI)
    }

    @Test
    fun `should fetch and save chart if Redis data is missing`() {
        // Given: No data in Redis and fetcher returns a valid response
        `when`(indexChartRepository.findById("KOSPI")).thenReturn(Optional.empty())

        val fetchedChart = IndexChartDomain.IndexChart(
            indexType = "KOSPI",
            summary = IndexChartDomain.ChartSummary(
                currentPrice = BigDecimal("100.0"),
                highPrice = BigDecimal("120.0"),
                lowPrice = BigDecimal("90.0"),
                openPrice = BigDecimal("95.0"),
                previousClosePrice = BigDecimal("98.0"),
                tradingVolume = BigDecimal("10000.0"),
                priceChangeSign = "+",
                name = "KOSPI"
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

        `when`(fetcher.fetchKrIndexChart(IndexType.KOSPI)).thenReturn(fetchedChart)

        // When: Service retrieves the index chart
        val result = service.getIndexCharts()

        // Then: Ensure the data is fetched and saved to Redis
        assertNotNull(result.kospi)
        assertEquals(fetchedChart.summary.currentPrice, result.kospi.summary.currentPrice)
        verify(indexChartRepository).save(any()) // Ensure Redis save was called
    }

    @Test
    fun `should handle all IndexTypes properly`() {
        // Given: Mock data for multiple index types
        val mockChart = IndexChartDomain.IndexChart(
            indexType = "KOSDAQ",
            summary = IndexChartDomain.ChartSummary(
                currentPrice = BigDecimal("150.0"),
                highPrice = BigDecimal("160.0"),
                lowPrice = BigDecimal("140.0"),
                openPrice = BigDecimal("145.0"),
                previousClosePrice = BigDecimal("148.0"),
                tradingVolume = BigDecimal("5000.0"),
                priceChangeSign = "+",
                name = "KOSDAQ"
            ),
            details = emptyList()
        )

        `when`(indexChartRepository.findById(anyString())).thenReturn(Optional.empty())
        `when`(fetcher.fetchKrIndexChart(any())).thenReturn(mockChart)

        // When: Service retrieves all index charts
        val result = service.getIndexCharts()

        // Then: Ensure all charts are handled properly
        assertNotNull(result.kospi)
        assertNotNull(result.kosdaq)
        verify(fetcher, atLeastOnce()).fetchKrIndexChart(any())
    }

    @Test
    fun `should throw exception if API fails`() {
        // Given: Fetcher throws an exception
        `when`(indexChartRepository.findById("KOSPI")).thenReturn(Optional.empty())
        `when`(fetcher.fetchKrIndexChart(IndexType.KOSPI))
            .thenThrow(RuntimeException("API failure"))

        // When / Then: Expect exception
        val exception = assertThrows<RuntimeException> {
            service.getIndexCharts()
        }
        assertEquals("API failure", exception.message)
    }
}