package kr.pe.hws.stockmanager.api.service

import kr.pe.hws.stockmanager.api.chart.service.ChartService
import kr.pe.hws.stockmanager.domain.kis.constants.IndexType
import kr.pe.hws.stockmanager.domain.kis.index.IndexChartDomain
import kr.pe.hws.stockmanager.redis.hash.ChartDetailRedisEntity
import kr.pe.hws.stockmanager.redis.hash.ChartSummaryRedisEntity
import kr.pe.hws.stockmanager.redis.hash.IndexChartRedisEntity
import kr.pe.hws.stockmanager.redis.repository.IndexChartRepository
import kr.pe.hws.stockmanager.webadapter.fetcher.KisApiFetcher
import org.junit.jupiter.api.Assertions.assertEquals
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
    fun `should return charts from Redis if data exists`() {
        // Given: Mock Redis data
        val mockRedisEntity = createMockRedisEntity(IndexType.KOSPI)
        `when`(indexChartRepository.findById("KOSPI")).thenReturn(Optional.of(mockRedisEntity))

        // When
        val result = service.getIndexCharts()

        // Then
        assertNotNull(result.kospiChart)
        assertEquals(BigDecimal("100.0"), result.kospiChart.summary.currentPrice)
        verify(fetcher, never()).fetchKrIndexChart(IndexType.KOSPI) // API 호출되지 않음
    }

    @Test
    fun `should fetch from API and save to Redis if data does not exist`() {
        // Given: Redis 데이터 없음
        `when`(indexChartRepository.findById("KOSPI")).thenReturn(Optional.empty())

        val mockIndexChart = createMockIndexChart(IndexType.KOSPI)
        `when`(fetcher.fetchKrIndexChart(IndexType.KOSPI)).thenReturn(mockIndexChart)

        // When
        val result = service.getIndexCharts()

        // Then
        assertNotNull(result.kospiChart)
        assertEquals(BigDecimal("100.0"), result.kospiChart.summary.currentPrice)

        // Verify: API 호출 및 Redis 저장 여부 확인
        verify(fetcher, times(1)).fetchKrIndexChart(IndexType.KOSPI)
        verify(indexChartRepository, times(1)).save(any())
    }

    @Test
    fun `should fetch all charts asynchronously`() {
        // Given: Redis 데이터 설정 및 Mock API 반환값 설정
        val mockRedisEntity = createMockRedisEntity(IndexType.KOSPI)
        `when`(indexChartRepository.findById(anyString())).thenReturn(Optional.empty())

        val mockIndexChart = createMockIndexChart(IndexType.KOSPI)
        `when`(fetcher.fetchKrIndexChart(IndexType.KOSPI)).thenReturn(mockIndexChart)
        `when`(fetcher.fetchOverSeaIndexChart(any())).thenReturn(mockIndexChart)

        // When
        val result = service.getIndexChartsAsync()

        // Then
        assertNotNull(result.kospiChart)
        assertEquals(BigDecimal("100.0"), result.kospiChart.summary.currentPrice)

        // Verify: 각 IndexType에 대해 호출 및 저장 여부 확인
        verify(fetcher, times(1)).fetchKrIndexChart(IndexType.KOSPI)
        verify(fetcher, atLeastOnce()).fetchOverSeaIndexChart(any())
        verify(indexChartRepository, atLeastOnce()).save(any())
    }
}