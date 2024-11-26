package kr.pe.hws.stockmanager.api.service

import kr.pe.hws.stockmanager.api.volumerank.service.VolumeRankService
import kr.pe.hws.stockmanager.domain.kis.volumerank.KrVolumeRankDomain
import kr.pe.hws.stockmanager.redis.hash.KrVolumeRankRedisEntity
import kr.pe.hws.stockmanager.redis.repository.KrVolumeRankRepository
import kr.pe.hws.stockmanager.webadapter.fetcher.KisApiFetcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import java.math.BigDecimal

class KrVolumeRankServiceTest {
    private val fetcher: KisApiFetcher = mock(KisApiFetcher::class.java)
    private val krStockVolumeRankRepository: KrVolumeRankRepository = mock(KrVolumeRankRepository::class.java)
    private val service = VolumeRankService(fetcher, krStockVolumeRankRepository)

    private fun createMockRedisEntity(
        symbol: String = "STOCK_001",
        stockName: String = "Sample Stock",
        rank: Int = 1,
        currentPrice: BigDecimal = BigDecimal("1000.0"),
        priceChangeSign: String = "+",
        priceChange: BigDecimal = BigDecimal("10.0"),
        priceChangeRate: BigDecimal = BigDecimal("1.0"),
        accumulatedVolume: Long = 1000L,
        previousVolume: Long = 900L,
        listedShares: Long = 100000L,
        averageVolume: Long = 950L,
        nDayClosingPriceRate: BigDecimal = BigDecimal("1.2"),
        volumeIncreaseRate: BigDecimal = BigDecimal("2.0"),
        volumeTurnoverRate: BigDecimal = BigDecimal("0.5"),
        nDayVolumeTurnoverRate: BigDecimal = BigDecimal("0.6"),
        averageTransactionAmount: BigDecimal = BigDecimal("500000.0"),
        transactionTurnoverRate: BigDecimal = BigDecimal("0.8"),
        nDayTransactionTurnoverRate: BigDecimal = BigDecimal("0.9"),
        accumulatedTransactionAmount: BigDecimal = BigDecimal("5000000.0"),
        expiration: Int = 3
    ): KrVolumeRankRedisEntity {
        return KrVolumeRankRedisEntity(
            symbol = symbol,
            stockName = stockName,
            rank = rank,
            currentPrice = currentPrice,
            priceChangeSign = priceChangeSign,
            priceChange = priceChange,
            priceChangeRate = priceChangeRate,
            accumulatedVolume = accumulatedVolume,
            previousVolume = previousVolume,
            listedShares = listedShares,
            averageVolume = averageVolume,
            nDayClosingPriceRate = nDayClosingPriceRate,
            volumeIncreaseRate = volumeIncreaseRate,
            volumeTurnoverRate = volumeTurnoverRate,
            nDayVolumeTurnoverRate = nDayVolumeTurnoverRate,
            averageTransactionAmount = averageTransactionAmount,
            transactionTurnoverRate = transactionTurnoverRate,
            nDayTransactionTurnoverRate = nDayTransactionTurnoverRate,
            accumulatedTransactionAmount = accumulatedTransactionAmount,
            expiration = expiration
        )
    }

    fun createMockDomain(rank: Int): KrVolumeRankDomain {
        return KrVolumeRankDomain(
            stockName = "Stock $rank",
            stockCode = "STOCK_$rank",
            rank = rank,
            currentPrice = BigDecimal("1000.0"),
            priceChangeSign = "+",
            priceChange = BigDecimal("10.0"),
            priceChangeRate = BigDecimal("1.0"),
            accumulatedVolume = 1000L,
            previousVolume = 950L,
            listedShares = 10000L,
            averageVolume = 970L,
            nDayClosingPriceRate = BigDecimal("2.0"),
            volumeIncreaseRate = BigDecimal("5.0"),
            volumeTurnoverRate = BigDecimal("0.1"),
            nDayVolumeTurnoverRate = BigDecimal("0.2"),
            averageTransactionAmount = BigDecimal("150000.0"),
            transactionTurnoverRate = BigDecimal("0.5"),
            nDayTransactionTurnoverRate = BigDecimal("0.6"),
            accumulatedTransactionAmount = BigDecimal("6000000.0")
        )
    }

    @Test
    fun `should return data from Redis if exists`() {
        // Given: Redis contains data
        val redisEntities = listOf(
            createMockRedisEntity(symbol = "STOCK_001", rank = 1),
            createMockRedisEntity(symbol = "STOCK_002", rank = 2)
        )
        `when`(krStockVolumeRankRepository.findAll()).thenReturn(redisEntities)

        // When
        val result = service.getVolumeRanks()

        // Then
        assertEquals(2, result.size) // Redis에서 반환된 데이터가 두 개여야 함
        assertEquals(1, result[0].rank) // 첫 번째 데이터의 rank는 1이어야 함
        assertEquals(2, result[1].rank) // 두 번째 데이터의 rank는 2이어야 함

        // Verify no API call
        verify(fetcher, never()).fetchKrStockVolumeRank(any())
        verify(krStockVolumeRankRepository, never()).saveAll(any<List<KrVolumeRankRedisEntity>>())
    }

    @Test
    fun `should fetch data from API and save to Redis if Redis is empty`() {
        // Given: Redis is empty and API returns data
        `when`(krStockVolumeRankRepository.findAll()).thenReturn(emptyList())
        val apiResults = listOf(createMockDomain(rank = 1), createMockDomain(rank = 2))
        `when`(fetcher.fetchKrStockVolumeRank(eq("0000"))).thenReturn(apiResults)

        // When
        val result = service.getVolumeRanks()

        // Then
        assertEquals(2, result.size)
        assertEquals(1, result[0].rank)
        assertEquals(2, result[1].rank)

        // Verify API was called once
        verify(fetcher, times(1)).fetchKrStockVolumeRank(eq("0000"))

        // Verify Redis save was called once with the expected data
        verify(krStockVolumeRankRepository, times(1)).saveAll(any<List<KrVolumeRankRedisEntity>>())
    }

    @Test
    fun `should handle API fetch failure`() {
        // Given: Redis is empty and API fetch fails
        `when`(krStockVolumeRankRepository.findAll()).thenReturn(emptyList())
        `when`(fetcher.fetchKrStockVolumeRank(eq("0000"))).thenThrow(RuntimeException("API error"))

        // When
        val result = service.getVolumeRanks()

        // Then
        assertTrue(result.isEmpty())

        // Verify API call attempted and Redis save not invoked
        verify(fetcher, times(1)).fetchKrStockVolumeRank(eq("0000"))
        verify(krStockVolumeRankRepository, never()).saveAll(any<List<KrVolumeRankRedisEntity>>())
    }
}