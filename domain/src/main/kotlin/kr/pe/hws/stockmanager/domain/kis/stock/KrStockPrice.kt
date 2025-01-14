package kr.pe.hws.stockmanager.domain.kis.stock

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Domain 객체: 국내 주식 가격
 */
data class KrStockPrice(
    // 기본 정보
    val symbol: String,          // 종목 코드
    val currentPrice: BigDecimal?, // 현재가
    val marketName: String?,     // 시장명
    val sectorName: String?,     // 업종명

    // 가격 정보
    val openPrice: BigDecimal?,  // 시가
    val highPrice: BigDecimal?,  // 최고가
    val lowPrice: BigDecimal?,   // 최저가
    val upperLimitPrice: BigDecimal?, // 상한가
    val lowerLimitPrice: BigDecimal?, // 하한가
    val previousClosePrice: BigDecimal?, // 기준가
    val weightedAveragePrice: BigDecimal?, // 가중 평균 가격

    // 거래 정보
    val accumulatedAmount: Long?,       // 누적 거래 대금
    val accumulatedVolume: Long?,       // 누적 거래량
    val previousVolumeChangeRate: BigDecimal?, // 전일 대비 거래량 비율

    // 지표 정보
    val per: BigDecimal?,               // PER
    val pbr: BigDecimal?,               // PBR
    val eps: BigDecimal?,               // EPS
    val bps: BigDecimal?,               // BPS

    // 250일 최고/최저 정보
    val highest250DayPrice: BigDecimal?, // 250일 최고가
    val highest250DayDate: String?,      // 250일 최고가 일자
    val lowest250DayPrice: BigDecimal?,  // 250일 최저가
    val lowest250DayDate: String?,       // 250일 최저가 일자

    // 연중 최고/최저 정보
    val highestYearPrice: BigDecimal?,   // 연중 최고가
    val highestYearDate: String?,        // 연중 최고가 일자
    val lowestYearPrice: BigDecimal?,    // 연중 최저가
    val lowestYearDate: String?,         // 연중 최저가 일자

    // 52주 최고/최저 정보
    val highest52WeekPrice: BigDecimal?, // 52주 최고가
    val highest52WeekDate: String?,      // 52주 최고가 일자
    val lowest52WeekPrice: BigDecimal?,  // 52주 최저가
    val lowest52WeekDate: String?,       // 52주 최저가 일자

    // 외국인 정보
    val foreignHoldingsRate: BigDecimal?, // 외국인 소진율
    val foreignNetBuyingVolume: Long?,   // 외국인 순매수 수량
    val programTradingNetBuyingVolume: Long?, // 프로그램매매 순매수 수량

    // 기타 정보
    val tradable: Boolean,            // 거래 가능 여부
    val shortSellingAllowed: Boolean, // 공매도 가능 여부
    val sectorCode: String?,          // 업종 코드
    val nominalPrice: BigDecimal?,    // 액면가
    val creditAvailable: Boolean      // 신용 가능 여부
) {
    /**
     * 상승 여부를 계산합니다.
     * @return 현재가가 기준가보다 높으면 true, 낮으면 false
     */
    fun isPriceUp(): Boolean? {
        if (currentPrice != null && previousClosePrice != null) {
            return currentPrice > previousClosePrice
        }
        return null
    }

    /**
     * 하락 여부를 계산합니다.
     * @return 현재가가 기준가보다 낮으면 true, 높으면 false
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
            if (previousClosePrice.compareTo(BigDecimal.ZERO) == 0) {
                return null // 전일 종가가 0이면 비율 계산 불가
            }
            return currentPrice
                .subtract(previousClosePrice) // (현재가 - 전일 종가)
                .divide(previousClosePrice, 4, RoundingMode.HALF_UP) // / 전일 종가 (소수점 4자리 반올림)
                .multiply(BigDecimal(100)) // * 100 (백분율 계산)
        }
        return null
    }

    /**
     * 52주 최고가 여부를 확인합니다.
     * @return 현재가가 52주 최고가와 동일하면 true, 그렇지 않으면 false
     */
    fun isAt52WeekHigh(): Boolean? {
        if (currentPrice != null && highest52WeekPrice != null) {
            return currentPrice == highest52WeekPrice
        }
        return null
    }

    /**
     * 52주 최저가 여부를 확인합니다.
     * @return 현재가가 52주 최저가와 동일하면 true, 그렇지 않으면 false
     */
    fun isAt52WeekLow(): Boolean? {
        if (currentPrice != null && lowest52WeekPrice != null) {
            return currentPrice == lowest52WeekPrice
        }
        return null
    }
}