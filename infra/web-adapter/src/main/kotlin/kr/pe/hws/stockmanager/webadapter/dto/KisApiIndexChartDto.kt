package kr.pe.hws.stockmanager.webadapter.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.pe.hws.stockmanager.domain.kis.constants.IndexType
import kr.pe.hws.stockmanager.domain.kis.index.IndexChart

object KisApiIndexChartDto {
    data class IndexChartPriceRequest(
        //
        /**
         * -------------- KR --------------
         * 조건 시장 분류 코드  :  업종 : U
         * -------------- KR --------------
         *
         * ----------- OVER_SEA -----------
         * FID 조건 시장 분류 코드, N: 해외지수, X 환율
         */
        val FID_COND_MRKT_DIV_CODE: String,
        /**
         * ---------------------------- KR ----------------------------
         * 업종 상세코드
         * 0001 : 종합
         * 0002 : 대형주
         * ...
         * kis developer 포탈 (FAQ : 종목정보 다운로드 - 업종코드 참조)
         *
         * idxcode.xlsx 참고
         * ---------------------------- KR ----------------------------
         *
         * ------------------------- OVER_SEA -------------------------
         * FID 입력 종목코드
         * 	종목코드
         * ※ 해외주식 마스터 코드 참조
         * (포럼 > FAQ > 종목정보 다운로드 > 해외주식)
         *
         * 해당 API로 미국주식 조회 시, 다우30, 나스닥100, S&P500 종목만 조회 가능합니다.
         * 더 많은 미국주식 종목 시세를 이용할 시에는, 해외주식기간별시세 API 사용 부탁드립니다.
         * ------------------------- OVER_SEA -------------------------
         */
        val FID_INPUT_ISCD: String,
        // 시작일자(YYYYMMDD)
        val FID_INPUT_DATE_1: String,
        // 	종료일자(YYYYMMDD)
        val FID_INPUT_DATE_2: String,
        // FID 기간 분류 코드 D:일, W:주, M:월, Y:년
        val FID_PERIOD_DIV_CODE: String,
    )

    sealed class IndexChartResponse {
        abstract val rtCd: String
        abstract val msgCd: String
        abstract val msg1: String
        abstract val summary: IndexChartSummary
        abstract val details: List<IndexChartDetail>
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class KrIndexChartResponse(
        @JsonProperty("rt_cd") override val rtCd: String,
        @JsonProperty("msg_cd") override val msgCd: String,
        @JsonProperty("msg1") override val msg1: String,
        @JsonProperty("output1") override val summary: KrChartSummary,
        @JsonProperty("output2") override val details: List<KrIndexChartDetail>,
    ) : IndexChartResponse()

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class OverSeaIndexChartResponse(
        @JsonProperty("rt_cd") override val rtCd: String,
        @JsonProperty("msg_cd") override val msgCd: String,
        @JsonProperty("msg1") override val msg1: String,
        @JsonProperty("output1") override val summary: OverSeaIndexChartSummary,
        @JsonProperty("output2") override val details: List<OverSeaIndexChartDetail>,
    ) : IndexChartResponse()

    sealed class IndexChartSummary {
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
    data class KrChartSummary(
        @JsonProperty("bstp_nmix_prpr") override val currentPrice: String?,
        @JsonProperty("bstp_nmix_hgpr") override val highPrice: String?,
        @JsonProperty("bstp_nmix_lwpr") override val lowPrice: String?,
        @JsonProperty("bstp_nmix_oprc") override val openPrice: String?,
        @JsonProperty("prdy_nmix") val prdyNmix: String?, // 전일 지수
        @JsonProperty("acml_vol") override val tradingVolume: String?,
        @JsonProperty("prdy_vrss_sign") override val priceChangeSign: String?,
        @JsonProperty("hts_kor_isnm") override val name: String?,
    ) : IndexChartSummary()

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class OverSeaIndexChartSummary(
        @JsonProperty("ovrs_nmix_prpr") override val currentPrice: String?,
        @JsonProperty("ovrs_prod_hgpr") override val highPrice: String?,
        @JsonProperty("ovrs_prod_lwpr") override val lowPrice: String?,
        @JsonProperty("ovrs_prod_oprc") override val openPrice: String?,
        @JsonProperty("acml_vol") override val tradingVolume: String?,
        @JsonProperty("prdy_vrss_sign") override val priceChangeSign: String?,
        @JsonProperty("hts_kor_isnm") override val name: String?,
    ) : IndexChartSummary()

    sealed class IndexChartDetail {
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
    data class KrIndexChartDetail(
        @JsonProperty("stck_bsop_date") override val date: String?,
        @JsonProperty("bstp_nmix_prpr") override val currentPrice: String?,
        @JsonProperty("bstp_nmix_oprc") override val openPrice: String?,
        @JsonProperty("bstp_nmix_hgpr") override val highPrice: String?,
        @JsonProperty("bstp_nmix_lwpr") override val lowPrice: String?,
        @JsonProperty("acml_vol") override val tradingVolume: String?,
        @JsonProperty("mod_yn") val modYn: String?,
    ) : IndexChartDetail() {
        override val modified: Boolean
            get() = modYn == "Y"
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class OverSeaIndexChartDetail(
        @JsonProperty("stck_bsop_date") override val date: String?,
        @JsonProperty("ovrs_nmix_prpr") override val currentPrice: String?,
        @JsonProperty("ovrs_nmix_oprc") override val openPrice: String?,
        @JsonProperty("ovrs_nmix_hgpr") override val highPrice: String?,
        @JsonProperty("ovrs_nmix_lwpr") override val lowPrice: String?,
        @JsonProperty("acml_vol") override val tradingVolume: String?,
        @JsonProperty("mod_yn") val modYn: String?,
    ) : IndexChartDetail() {
        override val modified: Boolean
            get() = modYn == "Y"
    }

    fun IndexChartResponse.toDomain(indexType: IndexType): IndexChart.IndexChart {
        return IndexChart.IndexChart(
            responseCode = rtCd,
            messageCode = msgCd,
            message = msg1,
            indexType = indexType.id,
            summary = summary.toDomain(),
            details = details.map { it.toDomain() },
        )
    }

    fun IndexChartSummary.toDomain(): IndexChart.ChartSummary {
        return IndexChart.ChartSummary(
            currentPrice = currentPrice,
            highPrice = highPrice,
            lowPrice = lowPrice,
            openPrice = openPrice,
            previousClosePrice = if (this is KrChartSummary) prdyNmix else null,
            tradingVolume = tradingVolume,
            priceChangeSign = priceChangeSign,
            name = name,
        )
    }

    fun IndexChartDetail.toDomain(): IndexChart.ChartDetail {
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