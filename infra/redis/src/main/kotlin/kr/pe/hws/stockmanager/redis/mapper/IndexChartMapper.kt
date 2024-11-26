package kr.pe.hws.stockmanager.redis.mapper

import kr.pe.hws.stockmanager.domain.kis.index.IndexChartDomain
import kr.pe.hws.stockmanager.domain.kis.index.IndexChartDomain.IndexChart
import kr.pe.hws.stockmanager.redis.hash.ChartDetailRedisEntity
import kr.pe.hws.stockmanager.redis.hash.ChartSummaryRedisEntity
import kr.pe.hws.stockmanager.redis.hash.IndexChartRedisEntity

object IndexChartMapper {
    fun toRedisEntity(indexChart: IndexChart): IndexChartRedisEntity {
        return IndexChartRedisEntity(
            id = indexChart.indexType,
            summary = ChartSummaryRedisEntity(
                currentPrice = indexChart.summary.currentPrice,
                highPrice = indexChart.summary.highPrice,
                lowPrice = indexChart.summary.lowPrice,
                openPrice = indexChart.summary.openPrice,
                previousClosePrice = indexChart.summary.previousClosePrice,
                tradingVolume = indexChart.summary.tradingVolume,
                priceChangeSign = indexChart.summary.priceChangeSign,
                name = indexChart.summary.name
            ),
            details = indexChart.details.map { detail ->
                ChartDetailRedisEntity(
                    date = detail.date,
                    currentPrice = detail.currentPrice,
                    openPrice = detail.openPrice,
                    highPrice = detail.highPrice,
                    lowPrice = detail.lowPrice,
                    tradingVolume = detail.tradingVolume,
                    modified = detail.modified
                )
            }
        )
    }

    fun fromRedisEntity(redisEntity: IndexChartRedisEntity): IndexChart {
        val summary = redisEntity.summary
        return IndexChart(
            indexType = redisEntity.id,
            summary = IndexChartDomain.ChartSummary(
                currentPrice = summary.currentPrice,
                highPrice = summary.highPrice,
                lowPrice = summary.lowPrice,
                openPrice = summary.openPrice,
                previousClosePrice = summary.previousClosePrice,
                tradingVolume = summary.tradingVolume,
                priceChangeSign = summary.priceChangeSign,
                name = summary.name
            ),
            details = redisEntity.details.map { detail ->
                IndexChartDomain.ChartDetail(
                    date = detail.date,
                    currentPrice = detail.currentPrice,
                    openPrice = detail.openPrice,
                    highPrice = detail.highPrice,
                    lowPrice = detail.lowPrice,
                    tradingVolume = detail.tradingVolume,
                    modified = detail.modified
                )
            }
        )
    }
}