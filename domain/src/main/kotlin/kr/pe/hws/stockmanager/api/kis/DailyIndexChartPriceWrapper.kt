package kr.pe.hws.stockmanager.api.kis

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.pe.hws.stockmanager.api.kis.index.IndexChart
import kr.pe.hws.stockmanager.api.kis.index.IndexType
object DailyIndexChartPriceWrapper {

    sealed class DailyIndexChartResponse {
        abstract val rtCd: String
        abstract val msgCd: String
        abstract val msg1: String
        abstract val summary: DailyIndexChartSummary
        abstract val details: List<DailyIndexChartDetail>
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class KrDailyIndexChartResponse(
        @JsonProperty("rt_cd") override val rtCd: String,
        @JsonProperty("msg_cd") override val msgCd: String,
        @JsonProperty("msg1") override val msg1: String,
        @JsonProperty("output1") override val summary: KrDailyIndexChartSummary,
        @JsonProperty("output2") override val details: List<KrDailyIndexChartDetail>,
    ) : DailyIndexChartResponse()

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class OverSeaDailyIndexChartResponse(
        @JsonProperty("rt_cd") override val rtCd: String,
        @JsonProperty("msg_cd") override val msgCd: String,
        @JsonProperty("msg1") override val msg1: String,
        @JsonProperty("output1") override val summary: OverSeaDailyIndexChartSummary,
        @JsonProperty("output2") override val details: List<OverSeaDailyIndexChartDetail>,
    ) : DailyIndexChartResponse()

    sealed class DailyIndexChartSummary {
        abstract val currentPrice: String?
        abstract val highPrice: String?
        abstract val lowPrice: String?
        abstract val openPrice: String?
        abstract val tradingVolume: String?
        abstract val priceChangeSign: String?
        abstract val name: String?
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class KrDailyIndexChartSummary(
        @JsonProperty("bstp_nmix_prpr") override val currentPrice: String?,
        @JsonProperty("bstp_nmix_hgpr") override val highPrice: String?,
        @JsonProperty("bstp_nmix_lwpr") override val lowPrice: String?,
        @JsonProperty("bstp_nmix_oprc") override val openPrice: String?,
        @JsonProperty("prdy_nmix") val prdyNmix: String?, // 전일 지수
        @JsonProperty("acml_vol") override val tradingVolume: String?,
        @JsonProperty("prdy_vrss_sign") override val priceChangeSign: String?,
        @JsonProperty("hts_kor_isnm") override val name: String?,
    ) : DailyIndexChartSummary()

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class OverSeaDailyIndexChartSummary(
        @JsonProperty("ovrs_nmix_prpr") override val currentPrice: String?,
        @JsonProperty("ovrs_prod_hgpr") override val highPrice: String?,
        @JsonProperty("ovrs_prod_lwpr") override val lowPrice: String?,
        @JsonProperty("ovrs_prod_oprc") override val openPrice: String?,
        @JsonProperty("acml_vol") override val tradingVolume: String?,
        @JsonProperty("prdy_vrss_sign") override val priceChangeSign: String?,
        @JsonProperty("hts_kor_isnm") override val name: String?,
    ) : DailyIndexChartSummary()

    sealed class DailyIndexChartDetail {
        abstract val date: String?
        abstract val currentPrice: String?
        abstract val openPrice: String?
        abstract val highPrice: String?
        abstract val lowPrice: String?
        abstract val tradingVolume: String?
        abstract val modified: Boolean
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class KrDailyIndexChartDetail(
        @JsonProperty("stck_bsop_date") override val date: String?,
        @JsonProperty("bstp_nmix_prpr") override val currentPrice: String?,
        @JsonProperty("bstp_nmix_oprc") override val openPrice: String?,
        @JsonProperty("bstp_nmix_hgpr") override val highPrice: String?,
        @JsonProperty("bstp_nmix_lwpr") override val lowPrice: String?,
        @JsonProperty("acml_vol") override val tradingVolume: String?,
        @JsonProperty("mod_yn") val modYn: String?,
    ) : DailyIndexChartDetail() {
        override val modified: Boolean
            get() = modYn == "Y"
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class OverSeaDailyIndexChartDetail(
        @JsonProperty("stck_bsop_date") override val date: String?,
        @JsonProperty("ovrs_nmix_prpr") override val currentPrice: String?,
        @JsonProperty("ovrs_nmix_oprc") override val openPrice: String?,
        @JsonProperty("ovrs_nmix_hgpr") override val highPrice: String?,
        @JsonProperty("ovrs_nmix_lwpr") override val lowPrice: String?,
        @JsonProperty("acml_vol") override val tradingVolume: String?,
        @JsonProperty("mod_yn") val modYn: String?,
    ) : DailyIndexChartDetail() {
        override val modified: Boolean
            get() = modYn == "Y"
    }

    fun DailyIndexChartResponse.toDomain(indexType: IndexType): IndexChart.DailyIndexChart<IndexChart.ChartSummary> {
        return IndexChart.DailyIndexChart(
            responseCode = rtCd,
            messageCode = msgCd,
            message = msg1,
            indexType = indexType,
            summary = summary.toDomain(),
            details = details.map { it.toDomain() },
        )
    }

    fun DailyIndexChartSummary.toDomain(): IndexChart.ChartSummary {
        return IndexChart.ChartSummary(
            currentPrice = currentPrice,
            highPrice = highPrice,
            lowPrice = lowPrice,
            openPrice = openPrice,
            previousClosePrice = if (this is KrDailyIndexChartSummary) prdyNmix else null,
            tradingVolume = tradingVolume,
            priceChangeSign = priceChangeSign,
            name = name,
        )
    }

    fun DailyIndexChartDetail.toDomain(): IndexChart.ChartDetail {
        return IndexChart.ChartDetail(
            date = date,
            currentPrice = currentPrice,
            openPrice = openPrice,
            highPrice = highPrice,
            lowPrice = lowPrice,
            tradingVolume = tradingVolume,
            modified = modified,
        )
    }
}
