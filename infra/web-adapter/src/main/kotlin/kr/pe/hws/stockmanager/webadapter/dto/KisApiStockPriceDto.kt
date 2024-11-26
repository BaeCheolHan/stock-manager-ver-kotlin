package kr.pe.hws.stockmanager.webadapter.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.pe.hws.stockmanager.domain.kis.stock.KrStockPrice
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
        var AUTH: String = "",
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
        @JsonProperty("output") val details: OverSeaStockPriceDto // 세부 정보
    )

    /**
     * 국내 실시간 주식 가격 응답 DTO
     */
    data class KrNowStockPriceResponse(
        @JsonProperty("rt_cd") val rtCd: String, // 응답 코드
        @JsonProperty("msg_cd") val msgCd: String, // 메시지 코드
        @JsonProperty("msg1") val msg1: String, // 메시지
        @JsonProperty("output") val details: KrStockPriceDto // 세부 정보
    )

    /**
     * 국내 주식 가격 상세 정보 DTO
     */
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class KrStockPriceDto(
        // 주식 단축 종목코드
        val stckShrnIscd: String?,
        // 주식 현재가
        val stckPrpr: BigDecimal?,
        // 종목 상태 구분 코드
        // 00 : 그외
        // 51 : 관리종목
        // 52 : 투자의견
        // 53 : 투자경고
        // 54 : 투자주의
        // 55 : 신용가능
        // 57 : 증거금 100%
        // 58 : 거래정지
        // 59 : 단기과열
        val iscdStatClsCode: String?,
        // 증거금 비율
        val margRate: BigDecimal?,
        // 대표 시장 한글 명
        val rprsMrktKorName: String?,
        // 업종 한글 종목명
        val bstpKorIsnm: String?,
        // 임시 정지 여부 (Y: 정지, N: 정지 아님)
        val tempStopYn: String?,
        // 시가 범위 연장 여부
        val oprcRangContYn: String?,
        // 종가 범위 연장 여부
        val clprRangContYn: String?,
        // 신용 가능 여부
        val crdtAbleYn: String?,
        // 보증금 비율 구분 코드
        val grmnRateClsCode: String?,
        // ELW 발행 여부
        val elwPblcYn: String?,
        // 전일 대비
        val prdyVrss: BigDecimal?,
        // 전일 대비 부호 (1: 상한, 2: 상승, 3: 보합, 4: 하한, 5: 하락)
        val prdyVrssSign: String?,
        // 전일 대비율
        val prdyCtrt: BigDecimal?,
        // 누적 거래 대금
        val acmlTrPbmn: Long?,
        // 누적 거래량
        val acmlVol: Long?,
        // 전일 대비 거래량 비율
        val prdyVrssVolRate: BigDecimal?,
        // 주식 시가
        val stckOprc: BigDecimal?,
        // 주식 최고가
        val stckHgpr: BigDecimal?,
        // 주식 최저가
        val stckLwpr: BigDecimal?,
        // 주식 상한가
        val stckMxpr: BigDecimal?,
        // 주식 하한가
        val stckLlam: BigDecimal?,
        // 주식 기준가
        val stckSdpr: BigDecimal?,
        // 가중 평균 주식 가격
        val wghnAvrgStckPrc: BigDecimal?,
        // HTS 외국인 소진율
        val htsFrgnEhrt: BigDecimal?,
        // 외국인 순매수 수량
        val frgnNtbyQty: Long?,
        // 프로그램매매 순매수 수량
        val pgtrNtbyQty: Long?,
        // 피벗 2차 디저항 가격
        val pvtScndDmrsPrc: Long?,
        // 피벗 1차 디저항 가격
        val pvtFrstDmrsPrc: Long?,
        // 피벗 포인트 값
        val pvtPontVal: Long?,
        // 피벗 1차 디지지 가격
        val pvtFrstDmspPrc: Long?,
        // 피벗 2차 디지지 가격
        val pvtScndDmspPrc: Long?,
        // 디저항 값
        val dmrsVal: Long?,
        // 디지지 값
        val dmspVal: Long?,
        // 자본금
        val cpfn: Long?,
        // 제한 폭 가격
        val rstcWdthPrc: Long?,
        // 주식 액면가
        val stckFcam: BigDecimal?,
        // 주식 대용가
        val stckSspr: BigDecimal?,
        // 호가 단위
        val asprUnit: BigDecimal?,
        // HTS 매매 수량 단위 값
        val htsDealQtyUnitVal: BigDecimal?,
        // 상장 주수
        val lstnStcn: Long?,
        // HTS 시가총액
        val htsAvls: Long?,
        // PER
        val per: BigDecimal?,
        // PBR
        val pbr: BigDecimal?,
        // 결산 월
        val stacMonth: String?,
        // 거래량 회전율
        val volTnrt: BigDecimal?,
        // EPS
        val eps: BigDecimal?,
        // BPS
        val bps: BigDecimal?,
        // 250일 최고가
        val d250Hgpr: BigDecimal?,
        // 250일 최고가 일자
        val d250HgprDate: String?,
        // 250일 최고가 대비 현재가 비율
        val d250HgprVrssPrprRate: BigDecimal?,
        // 250일 최저가
        val d250Lwpr: BigDecimal?,
        // 250일 최저가 일자
        val d250LwprDate: String?,
        // 250일 최저가 대비 현재가 비율
        val d250LwprVrssPrprRate: BigDecimal?,
        // 연중 최고가
        val stckDryyHgpr: BigDecimal?,
        // 연중 최고가 대비 현재가 비율
        val dryyHgprVrssPrprRate: BigDecimal?,
        // 연중 최고가 일자
        val dryyHgprDate: String?,
        // 연중 최저가
        val stckDryyLwpr: BigDecimal?,
        // 연중 최저가 대비 현재가 비율
        val dryyLwprVrssPrprRate: BigDecimal?,
        // 연중 최저가 일자
        val dryyLwprDate: String?,
        // 52주일 최고가
        val w52Hgpr: BigDecimal?,
        // 52주일 최고가 대비 현재가 대비
        val w52HgprVrssPrprCtrt: BigDecimal?,
        // 52주일 최고가 일자
        val w52HgprDate: String?,
        // 52주일 최저가
        val w52Lwpr: BigDecimal?,
        // 52주일 최저가 대비 현재가 대비
        val w52LwprVrssPrprCtrt: BigDecimal?,
        // 52주일 최저가 일자
        val w52LwprDate: String?,
        // 전체 융자 잔고 비율
        val wholLoanRmndRate: BigDecimal?,
        // 공매도 가능 여부
        val sstsYn: String?,
        // 액면가 통화명
        val fcamCnnm: String?,
        // 자본금 통화명
        val cpfnCnnm: String?,
        // 외국인 보유 수량
        val frgnHldnQty: Long?,
        // VI 적용 구분 코드
        val viClsCode: String?,
        // 시간외 단일가 VI 적용 구분 코드
        val ovtmViClsCode: String?,
        // 최종 공매도 체결 수량
        val lastSstsCntgQty: Long?,
        // 투자 유의 여부
        val invtCafulYn: String?,
        // 시장 경고 코드
        val mrktWarnClsCode: String?,
        // 단기 과열 여부
        val shortOverYn: String?,
        // ?
        val sltrYn: String?
    )

    /**
     * 해외 실시간 주식 가격 상세 정보
     */
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class OverSeaStockPriceDto(
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

    fun KrStockPriceDto.toDomain(): KrStockPrice {
        return KrStockPrice(
            symbol = this.stckShrnIscd ?: throw IllegalArgumentException("Symbol cannot be null"),
            currentPrice = this.stckPrpr,
            marketName = this.rprsMrktKorName,
            sectorName = this.bstpKorIsnm,

            // 가격 정보
            openPrice = this.stckOprc,
            highPrice = this.stckHgpr,
            lowPrice = this.stckLwpr,
            upperLimitPrice = this.stckMxpr,
            lowerLimitPrice = this.stckLlam,
            previousClosePrice = this.stckSdpr,
            weightedAveragePrice = this.wghnAvrgStckPrc,

            // 거래 정보
            accumulatedAmount = this.acmlTrPbmn,
            accumulatedVolume = this.acmlVol,
            previousVolumeChangeRate = this.prdyVrssVolRate,

            // 지표 정보
            per = this.per,
            pbr = this.pbr,
            eps = this.eps,
            bps = this.bps,

            // 250일 최고/최저 정보
            highest250DayPrice = this.d250Hgpr,
            highest250DayDate = this.d250HgprDate,
            lowest250DayPrice = this.d250Lwpr,
            lowest250DayDate = this.d250LwprDate,

            // 연중 최고/최저 정보
            highestYearPrice = this.stckDryyHgpr,
            highestYearDate = this.dryyHgprDate,
            lowestYearPrice = this.stckDryyLwpr,
            lowestYearDate = this.dryyLwprDate,

            // 52주 최고/최저 정보
            highest52WeekPrice = this.w52Hgpr,
            highest52WeekDate = this.w52HgprDate,
            lowest52WeekPrice = this.w52Lwpr,
            lowest52WeekDate = this.w52LwprDate,

            // 외국인 정보
            foreignHoldingsRate = this.htsFrgnEhrt,
            foreignNetBuyingVolume = this.frgnNtbyQty,
            programTradingNetBuyingVolume = this.pgtrNtbyQty,

            // 기타 정보
            tradable = this.crdtAbleYn == "Y",           // "Y" -> true, "N" -> false
            shortSellingAllowed = this.sstsYn == "Y",    // "Y" -> true, "N" -> false
            sectorCode = this.bstpKorIsnm,
            nominalPrice = this.stckFcam,
            creditAvailable = this.crdtAbleYn == "Y"     // "Y" -> true, "N" -> false
        )
    }

    fun OverSeaStockPriceDto.toDomain(): OverSeaStockPrice {
        return OverSeaStockPrice(
            symbol = extractSymbolFromRsym(this.rsym) ?: throw IllegalArgumentException("Symbol cannot be null"),
            stockName = this.rsym ?: "Unknown", // 종목명이 없을 경우 기본값
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

    /**
     * rsym에서 Symbol 값을 추출합니다.
     * @param rsym 조회된 종목명 (예: DNASAAPL)
     * @return 종목 코드 (예: AAPL)
     */
    private fun extractSymbolFromRsym(rsym: String?): String {
        if (rsym != null && rsym.length > 4) {
            return rsym.substring(4) // 예: DNASAAPL에서 AAPL 추출
        }
        throw IllegalArgumentException("Symbol cannot be resolved from rsym")
    }
}