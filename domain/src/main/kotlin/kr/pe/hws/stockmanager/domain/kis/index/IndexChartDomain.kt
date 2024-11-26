
package kr.pe.hws.stockmanager.domain.kis.index

import java.math.BigDecimal

object IndexChartDomain {
    data class IndexChart(
        val indexType: String,
        val summary: ChartSummary,
        val details: List<ChartDetail>
    )

    data class ChartSummary(
        val currentPrice: BigDecimal?,
        val highPrice: BigDecimal?,
        val lowPrice: BigDecimal?,
        val openPrice: BigDecimal?,
        val previousClosePrice: BigDecimal?,
        val tradingVolume: BigDecimal?,
        val priceChangeSign: String?,
        val name: String?
    )

    data class ChartDetail(
        val date: String?,
        val currentPrice: BigDecimal?,
        val openPrice: BigDecimal?,
        val highPrice: BigDecimal?,
        val lowPrice: BigDecimal?,
        val tradingVolume: BigDecimal?,
        val modified: Boolean
    )

}