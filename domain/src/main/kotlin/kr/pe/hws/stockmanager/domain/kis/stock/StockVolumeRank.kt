package kr.pe.hws.stockmanager.domain.kis.stock

/**
 * 개별 주식 거래량 랭킹 정보 Domain 객체
 */
data class StockVolumeRank(
    val stockName: String?,             // HTS 한글 종목명
    val stockCode: String?,             // 단축 종목 코드
    val rank: String?,                  // 데이터 순위
    val currentPrice: String?,          // 현재가
    val priceChangeSign: String?,       // 전일 대비 부호
    val priceChange: String?,           // 전일 대비
    val priceChangeRate: String?,       // 전일 대비율
    val accumulatedVolume: String?,     // 누적 거래량
    val previousVolume: String?,        // 전일 거래량
    val listedShares: String?,          // 상장 주수
    val averageVolume: String?,         // 평균 거래량
    val nDayClosingPriceRate: String?,  // N일 전 종가 대비율
    val volumeIncreaseRate: String?,    // 거래량 증가율
    val volumeTurnoverRate: String?,    // 거래량 회전율
    val nDayVolumeTurnoverRate: String?, // N일 거래량 회전율
    val averageTransactionAmount: String?, // 평균 거래 대금
    val transactionTurnoverRate: String?,  // 거래 대금 회전율
    val nDayTransactionTurnoverRate: String?, // N일 거래 대금 회전율
    val accumulatedTransactionAmount: String?, // 누적 거래 대금
    val country: String = "KR"         // 국가 (기본값 KR)
)
