
package kr.pe.hws.stockmanager.domain.kis.index

object IndexChart {
    data class IndexChart(
        val responseCode: String,
        val messageCode: String,
        val message: String,
        val indexType: String,
        val summary: ChartSummary,
        val details: List<ChartDetail>
    )

    data class ChartSummary(
        val currentPrice: String?,
        val highPrice: String?,
        val lowPrice: String?,
        val openPrice: String?,
        val previousClosePrice: String?,
        val tradingVolume: String?,
        val priceChangeSign: String?,
        val name: String?
    )

    data class ChartDetail(
        val date: String?,
        val currentPrice: String?,
        val openPrice: String?,
        val highPrice: String?,
        val lowPrice: String?,
        val tradingVolume: String?,
        val modified: Boolean
    )
}