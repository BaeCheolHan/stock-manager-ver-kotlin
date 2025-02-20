package kr.pe.hws.stockmanager.api.utils.mapper

import kr.pe.hws.stockmanager.api.volumerank.dto.VolumeRankDto
import kr.pe.hws.stockmanager.domain.kis.volumerank.KrVolumeRankDomain

object KrVolumeRankMapper {
    fun toResponse(domain: KrVolumeRankDomain): VolumeRankDto {
        return VolumeRankDto(
            stockName = domain.stockName,
            stockCode = domain.stockCode,
            rank = domain.rank,
            currentPrice = domain.currentPrice,
            priceChangeSign = domain.priceChangeSign,
            priceChange = domain.priceChange,
            priceChangeRate = domain.priceChangeRate,
            accumulatedVolume = domain.accumulatedVolume,
            listedShares = domain.listedShares,
            volumeIncreaseRate = domain.volumeIncreaseRate,
            volumeTurnoverRate = domain.volumeTurnoverRate,
            averageTransactionAmount = domain.averageTransactionAmount,
            transactionTurnoverRate = domain.transactionTurnoverRate
        )
    }
}