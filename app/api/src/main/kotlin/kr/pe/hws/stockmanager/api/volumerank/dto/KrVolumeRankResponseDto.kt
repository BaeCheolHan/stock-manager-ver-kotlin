package kr.pe.hws.stockmanager.api.volumerank.dto

import java.math.BigDecimal

/**
 * 주식 거래량 랭킹 정보 Response DTO
 */
data class KrVolumeRankResponseDto(
    val stockName: String,                   // 종목명
    val stockCode: String,                   // 종목 코드
    val rank: Int,                           // 순위
    val currentPrice: BigDecimal,            // 현재가
    val priceChangeSign: String,             // 전일 대비 부호
    val priceChange: BigDecimal,             // 전일 대비 가격 변화
    val priceChangeRate: BigDecimal,         // 전일 대비율
    val accumulatedVolume: Long,             // 누적 거래량
    val listedShares: Long,                  // 상장 주수
    val volumeIncreaseRate: BigDecimal,      // 거래량 증가율
    val volumeTurnoverRate: BigDecimal,      // 거래량 회전율
    val averageTransactionAmount: BigDecimal,// 평균 거래 대금
    val transactionTurnoverRate: BigDecimal  // 거래 대금 회전율
)