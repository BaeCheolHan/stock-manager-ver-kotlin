package kr.pe.hws.stockmanager.redis.mapper

import kr.pe.hws.stockmanager.domain.kis.volumerank.KrVolumeRankDomain
import kr.pe.hws.stockmanager.redis.hash.KrVolumeRankRedisEntity

object KrVolumeRankRedisMapper {
    // Domain → Redis Entity
    fun toRedisEntity(domain: KrVolumeRankDomain): KrVolumeRankRedisEntity {
        return KrVolumeRankRedisEntity(
            symbol = domain.stockCode,
            stockName = domain.stockName,
            rank = domain.rank,
            currentPrice = domain.currentPrice,
            priceChangeSign = domain.priceChangeSign,
            priceChange = domain.priceChange,
            priceChangeRate = domain.priceChangeRate,
            accumulatedVolume = domain.accumulatedVolume,
            previousVolume = domain.previousVolume,
            listedShares = domain.listedShares,
            averageVolume = domain.averageVolume,
            nDayClosingPriceRate = domain.nDayClosingPriceRate,
            volumeIncreaseRate = domain.volumeIncreaseRate,
            volumeTurnoverRate = domain.volumeTurnoverRate,
            nDayVolumeTurnoverRate = domain.nDayVolumeTurnoverRate,
            averageTransactionAmount = domain.averageTransactionAmount,
            transactionTurnoverRate = domain.transactionTurnoverRate,
            nDayTransactionTurnoverRate = domain.nDayTransactionTurnoverRate,
            accumulatedTransactionAmount = domain.accumulatedTransactionAmount
        )
    }

    // Redis Entity → Domain
    fun fromRedisEntity(entity: KrVolumeRankRedisEntity): KrVolumeRankDomain {
        return KrVolumeRankDomain(
            stockCode = entity.symbol,
            stockName = entity.stockName,
            rank = entity.rank,
            currentPrice = entity.currentPrice,
            priceChangeSign = entity.priceChangeSign,
            priceChange = entity.priceChange,
            priceChangeRate = entity.priceChangeRate,
            accumulatedVolume = entity.accumulatedVolume,
            previousVolume = entity.previousVolume,
            listedShares = entity.listedShares,
            averageVolume = entity.averageVolume,
            nDayClosingPriceRate = entity.nDayClosingPriceRate,
            volumeIncreaseRate = entity.volumeIncreaseRate,
            volumeTurnoverRate = entity.volumeTurnoverRate,
            nDayVolumeTurnoverRate = entity.nDayVolumeTurnoverRate,
            averageTransactionAmount = entity.averageTransactionAmount,
            transactionTurnoverRate = entity.transactionTurnoverRate,
            nDayTransactionTurnoverRate = entity.nDayTransactionTurnoverRate,
            accumulatedTransactionAmount = entity.accumulatedTransactionAmount
        )
    }

}