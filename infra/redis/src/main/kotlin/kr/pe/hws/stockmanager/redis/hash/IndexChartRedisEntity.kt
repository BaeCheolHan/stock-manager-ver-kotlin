package kr.pe.hws.stockmanager.redis.hash

import java.io.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

@RedisHash("IndexChart")
data class IndexChartRedisEntity(
    @Id
    val id: String, // Redis Key로 사용할 ID (예: indexType)
    val summary: ChartSummaryRedisEntity,
    val details: List<ChartDetailRedisEntity>,

    @TimeToLive(unit = TimeUnit.MINUTES)
    val expiration: Int = 3,
) : Serializable

data class ChartSummaryRedisEntity(
    val currentPrice: BigDecimal?,
    val highPrice: BigDecimal?,
    val lowPrice: BigDecimal?,
    val openPrice: BigDecimal?,
    val previousClosePrice: BigDecimal?,
    val tradingVolume: BigDecimal?,
    val priceChangeSign: String?,
    @Indexed // 인덱스를 추가하여 Redis에서 검색 가능하게 설정
    val name: String?
) : Serializable

data class ChartDetailRedisEntity(
    val date: String?,
    val currentPrice: BigDecimal?,
    val openPrice: BigDecimal?,
    val highPrice: BigDecimal?,
    val lowPrice: BigDecimal?,
    val tradingVolume: BigDecimal?,
    val modified: Boolean
) : Serializable