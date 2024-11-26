package kr.pe.hws.stockmanager.redis.hash

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.io.Serializable
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

/**
 * 주식 거래량 랭킹 정보 Redis Entity
 */
@RedisHash("KrStockVolumeRank")
data class KrVolumeRankRedisEntity(
    @Id val symbol: String,                // Redis Key로 사용할 필드 (stockCode 대체)
    val stockName: String,                 // HTS 한글 종목명
    val rank: Int,                         // 데이터 순위
    val currentPrice: BigDecimal,          // 현재가
    val priceChangeSign: String,           // 전일 대비 부호
    val priceChange: BigDecimal,           // 전일 대비
    val priceChangeRate: BigDecimal,       // 전일 대비율
    val accumulatedVolume: Long,           // 누적 거래량
    val previousVolume: Long,              // 전일 거래량
    val listedShares: Long,                // 상장 주수
    val averageVolume: Long,               // 평균 거래량
    val nDayClosingPriceRate: BigDecimal,  // N일 전 종가 대비율
    val volumeIncreaseRate: BigDecimal,    // 거래량 증가율
    val volumeTurnoverRate: BigDecimal,    // 거래량 회전율
    val nDayVolumeTurnoverRate: BigDecimal,// N일 거래량 회전율
    val averageTransactionAmount: BigDecimal, // 평균 거래 대금
    val transactionTurnoverRate: BigDecimal,  // 거래 대금 회전율
    val nDayTransactionTurnoverRate: BigDecimal, // N일 거래 대금 회전율
    val accumulatedTransactionAmount: BigDecimal, // 누적 거래 대금
    @TimeToLive(unit = TimeUnit.MINUTES)
    val expiration: Int = 3,
) : Serializable