package kr.pe.hws.stockmanager.webadapter.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.pe.hws.stockmanager.domain.kis.stock.OverSeaStockPrice
import java.math.BigDecimal

object KisApiStockPriceDto {

    /**
     * 국내 주식 가격 요청 DTO
     */
    data class KrStockPriceRequest(
        // 시장 분류 코드
        val fid_cond_mrkt_div_code: String,
        // 종목 코드
        val fid_input_iscd: String,
    )

    /**
     * 해외 주식 가격 요청 DTO
     */
    data class OverSeaStockPriceRequest(
        // 인증 키
        val AUTH: String,
        // 시장 코드 (예: NAS)
        val EXCD: String,
        // 종목 코드 (예: AAPL)
        val SYMB: String,
    )

    /**
     * 해외 실시간 주식 가격 응답 DTO
     */
    data class OverSeaNowStockPriceResponse(
        @JsonProperty("rt_cd") val rtCd: String, // 응답 코드
        @JsonProperty("msg_cd") val msgCd: String, // 메시지 코드
        @JsonProperty("msg1") val msg1: String,   // 메시지
        @JsonProperty("output") val details: OverSeaNowStockPriceDto // 세부 정보
    )

    /**
     * 해외 실시간 주식 가격 상세 정보
     */
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class OverSeaNowStockPriceDto(
        // 실시간조회종목코드
        var symbol: String?, // 예: DNASAAPL
        val rsym: String?, // 조회된 종목명
        val zdiv: Int?, // 소수점 자리수
        val curr: String?, // 통화 코드
        val vnit: BigDecimal?, // 매매단위

        // 가격 정보
        val open: BigDecimal?, // 시가
        val high: BigDecimal?, // 고가
        val low: BigDecimal?, // 저가
        val last: BigDecimal?, // 현재가
        val base: BigDecimal?, // 전일 종가

        // 거래 정보
        val pvol: Long?, // 전일 거래량
        val pamt: BigDecimal?, // 전일 거래대금
        val tvol: BigDecimal?, // 거래량
        val tamt: BigDecimal?, // 거래대금

        // 52주 최고/최저
        val h52p: BigDecimal?, // 52주 최고가
        val h52d: String?,     // 52주 최고일자
        val l52p: BigDecimal?, // 52주 최저가
        val l52d: String?,     // 52주 최저일자

        // 지표 정보
        val perx: BigDecimal?, // PER
        val pbrx: BigDecimal?, // PBR
        val epsx: BigDecimal?, // EPS
        val bpsx: BigDecimal?, // BPS

        // 기업 정보
        val shar: Long?,       // 상장 주수
        val mcap: BigDecimal?, // 자본금
        val tomv: BigDecimal?, // 시가총액

        // 환산 가격
        val tXprc: BigDecimal?, // 원환산당일가격
        val tXdif: BigDecimal?, // 원환산당일대비
        val tXrat: BigDecimal?, // 원환산당일등락
        val pXprc: BigDecimal?, // 원환산전일가격
        val pXdif: BigDecimal?, // 원환산전일대비
        val pXrat: BigDecimal?, // 원환산전일등락

        // 환율 정보
        val tRate: BigDecimal?, // 당일환율
        val pRate: BigDecimal?, // 전일환율
        val tXsgn: String?,     // 원환산당일기호
        val pXsng: String?,     // 원환산전일기호

        // 추가 정보
        val eOrdyn: String?,    // 거래 가능 여부
        val eHogau: BigDecimal?, // 호가 단위
        val eIcod: String?,      // 업종(섹터)
        val eParp: BigDecimal?,  // 액면가
        val etypNm: BigDecimal?  // ETP 분류명
    )

    fun KisApiStockPriceDto.OverSeaNowStockPriceDto.toDomain(): OverSeaStockPrice {
        return OverSeaStockPrice(
            symbol = this.symbol ?: throw IllegalArgumentException("Symbol cannot be null"),
            name = this.rsym ?: "Unknown", // 종목명이 없을 경우 기본값
            market = extractMarketFromSymbol(this.rsym), // rsym에서 시장 코드 추출
            openPrice = this.open?.stripTrailingZeros(),
            highPrice = this.high?.stripTrailingZeros(),
            lowPrice = this.low?.stripTrailingZeros(),
            currentPrice = this.last?.stripTrailingZeros(),
            previousClosePrice = this.base?.stripTrailingZeros(),
            tradingVolume = this.tvol?.stripTrailingZeros(),
            tradingAmount = this.tamt?.stripTrailingZeros(),
            previousVolume = this.pvol,
            previousAmount = this.pamt?.stripTrailingZeros(),
            high52WeekPrice = this.h52p?.stripTrailingZeros(),
            high52WeekDate = this.h52d,
            low52WeekPrice = this.l52p?.stripTrailingZeros(),
            low52WeekDate = this.l52d,
            per = this.perx?.stripTrailingZeros(),
            pbr = this.pbrx?.stripTrailingZeros(),
            eps = this.epsx?.stripTrailingZeros(),
            bps = this.bpsx?.stripTrailingZeros(),
            convertedPrice = this.tXprc?.stripTrailingZeros(),
            convertedDiff = this.tXdif?.stripTrailingZeros(),
            convertedRate = this.tXrat?.stripTrailingZeros(),
            previousConvertedPrice = this.pXprc?.stripTrailingZeros(),
            previousConvertedDiff = this.pXdif?.stripTrailingZeros(),
            previousConvertedRate = this.pXrat?.stripTrailingZeros(),
            todayRate = this.tRate?.stripTrailingZeros(),
            previousRate = this.pRate?.stripTrailingZeros(),
            tradable = this.eOrdyn == "매매 가능", // "매매 가능" -> true
            sectorCode = this.eIcod,
            nominalPrice = this.eParp?.stripTrailingZeros()
        )
    }

    /**
     * rsym에서 시장 코드를 추출
     * @param rsym 조회된 종목명 (예: DNASAAPL)
     * @return 시장 코드 (예: NAS)
     */
    private fun extractMarketFromSymbol(rsym: String?): String {
        if (rsym != null && rsym.length > 3) {
            return rsym.substring(1, 4) // 예: DNASAAPL에서 NAS 추출
        }
        return "Unknown" // 유효하지 않은 rsym 처리
    }
}