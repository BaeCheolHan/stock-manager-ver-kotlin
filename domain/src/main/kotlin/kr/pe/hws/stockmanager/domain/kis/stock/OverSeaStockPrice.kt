package kr.pe.hws.stockmanager.domain.kis.stock

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

data class OverSeaStockPrice(
    // 기본 정보
    val symbol: String,       // 종목 코드 (예: AAPL)
    val stockName: String,         // 조회된 종목명
    val market: String,       // 시장 코드 (예: NAS, NYS)

    // 가격 정보
    val openPrice: BigDecimal?, // 시가
    val highPrice: BigDecimal?, // 고가
    val lowPrice: BigDecimal?,  // 저가
    val currentPrice: BigDecimal?, // 현재가
    val previousClosePrice: BigDecimal?, // 전일 종가

    // 거래 정보
    val tradingVolume: BigDecimal?,    // 현재 거래량
    val tradingAmount: BigDecimal?,    // 현재 거래대금
    val previousVolume: Long?,         // 전일 거래량
    val previousAmount: BigDecimal?,   // 전일 거래대금

    // 52주 최고/최저
    val high52WeekPrice: BigDecimal?,  // 52주 최고가
    val high52WeekDate: String?,       // 52주 최고일자
    val low52WeekPrice: BigDecimal?,   // 52주 최저가
    val low52WeekDate: String?,        // 52주 최저일자

    // 지표 정보
    val per: BigDecimal?,              // PER
    val pbr: BigDecimal?,              // PBR
    val eps: BigDecimal?,              // EPS
    val bps: BigDecimal?,              // BPS

    // 환산 가격 정보
    val convertedPrice: BigDecimal?,   // 원환산 당일 가격
    val convertedDiff: BigDecimal?,    // 원환산 당일 대비
    val convertedRate: BigDecimal?,    // 원환산 당일 등락
    val previousConvertedPrice: BigDecimal?, // 원환산 전일 가격
    val previousConvertedDiff: BigDecimal?,  // 원환산 전일 대비
    val previousConvertedRate: BigDecimal?,  // 원환산 전일 등락

    // 환율 정보
    val todayRate: BigDecimal?,        // 당일 환율
    val previousRate: BigDecimal?,     // 전일 환율

    // 추가 정보
    val tradable: Boolean,             // 거래 가능 여부
    val sectorCode: String?,           // 업종 코드
    val nominalPrice: BigDecimal?      // 액면가
) {
    /**
     * 상승 여부를 계산합니다.
     * @return 현재가가 전일 종가보다 높으면 true, 낮으면 false
     */
    fun isPriceUp(): Boolean? {
        if (currentPrice != null && previousClosePrice != null) {
            return currentPrice > previousClosePrice
        }
        return null
    }

    /**
     * 하락 여부를 계산합니다.
     * @return 현재가가 전일 종가보다 낮으면 true, 높으면 false
     */
    fun isPriceDown(): Boolean? {
        if (currentPrice != null && previousClosePrice != null) {
            return currentPrice < previousClosePrice
        }
        return null
    }

    /**
     * 가격 변동 비율을 계산합니다.
     * @return 변동 비율 (예: -2.5% -> -0.025)
     */
    fun priceChangeRate(): BigDecimal? {
        if (currentPrice != null && previousClosePrice != null) {
            return currentPrice
                .subtract(previousClosePrice) // 현재가 - 전일 종가
                .divide(previousClosePrice, 4, RoundingMode.HALF_UP) // 나누기 (소수점 4자리, 반올림)
                .multiply(BigDecimal(100)) // 100을 곱해 비율 계산
        }
        return null
    }

    /**
     * 원환산 가격 변동 비율을 계산합니다.
     * @return 원환산 변동 비율
     */
    fun convertedPriceChangeRate(): BigDecimal? {
        if (convertedPrice != null && previousConvertedPrice != null) {
            val mathContext = MathContext(4, RoundingMode.HALF_UP) // 소수점 4자리, 반올림
            return convertedPrice.subtract(previousConvertedPrice)
                .divide(previousConvertedPrice, mathContext)
                .multiply(BigDecimal(100))
        }
        return null
    }
    /**
     * 52주 최고가 여부를 확인합니다.
     * @return 현재가가 52주 최고가와 동일하면 true, 그렇지 않으면 false
     */
    fun isAt52WeekHigh(): Boolean? {
        if (currentPrice != null && high52WeekPrice != null) {
            return currentPrice == high52WeekPrice
        }
        return null
    }

    /**
     * 52주 최저가 여부를 확인합니다.
     * @return 현재가가 52주 최저가와 동일하면 true, 그렇지 않으면 false
     */
    fun isAt52WeekLow(): Boolean? {
        if (currentPrice != null && low52WeekPrice != null) {
            return currentPrice == low52WeekPrice
        }
        return null
    }
}