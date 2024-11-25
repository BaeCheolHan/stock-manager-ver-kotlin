package kr.pe.hws.stockmanager.domain.kis.stock

import java.math.BigDecimal

/**
 * 공통 인터페이스: StockPrice
 * 공통적인 주식 데이터와 동작을 정의합니다.
 */
interface StockPrice {
    val symbol: String                // 종목 코드
    val currentPrice: BigDecimal?     // 현재가
    val openPrice: BigDecimal?        // 시가
    val highPrice: BigDecimal?        // 고가
    val lowPrice: BigDecimal?         // 저가
    val previousClosePrice: BigDecimal? // 전일 종가

    /**
     * 상승 여부를 계산합니다.
     * @return 현재가가 전일 종가보다 높으면 true, 낮으면 false
     */
    fun isPriceUp(): Boolean? {
        if (currentPrice != null && previousClosePrice != null) {
            return currentPrice!! > previousClosePrice
        }
        return null
    }

    /**
     * 하락 여부를 계산합니다.
     * @return 현재가가 전일 종가보다 낮으면 true, 높으면 false
     */
    fun isPriceDown(): Boolean? {
        if (currentPrice != null && previousClosePrice != null) {
            return currentPrice!! < previousClosePrice
        }
        return null
    }

    /**
     * 가격 변동 비율을 계산합니다.
     * @return 변동 비율 (예: -2.5% -> -0.025)
     */
    fun priceChangeRate(): BigDecimal? {
        if (currentPrice != null && previousClosePrice != null) {
            return (currentPrice!! - previousClosePrice!!) / previousClosePrice!! * BigDecimal(100)
        }
        return null
    }
}