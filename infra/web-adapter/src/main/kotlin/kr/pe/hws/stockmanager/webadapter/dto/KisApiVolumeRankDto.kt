package kr.pe.hws.stockmanager.webadapter.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.pe.hws.stockmanager.domain.kis.stock.StockVolumeRank

object KisApiVolumeRankDto {

    /**
     * 국내 거래량 랭킹 요청 DTO
     */
    data class KrVolumeRankRequest(
        // 조건 시장 분류 코드 (예: J)
        val FID_COND_MRKT_DIV_CODE: String,
        // 조건 화면 분류 코드 (예: 20171)
        val FID_COND_SCR_DIV_CODE: String,
        // 입력 종목코드: 0000(전체), 기타(업종코드)
        val FID_INPUT_ISCD: String,
        // 분류 구분 코드: 0(전체), 1(보통주), 2(우선주)
        val FID_DIV_CLS_CODE: String,
        // 소속 구분 코드:
        // 0: 평균거래량, 1: 거래증가율, 2: 평균거래회전율, 3: 거래금액순, 4: 평균거래금액회전율
        val FID_BLNG_CLS_CODE: String,
        // 대상 구분 코드:
        // 1 or 0 9자리 (차례대로 증거금 30%, 40%, 50%, 60%, 100%, 신용보증금 30%, 40%, 50%, 60%)
        val FID_TRGT_CLS_CODE: String,
        // 대상 제외 구분 코드:
        // 1 or 0 6자리 (투자위험/경고/주의, 관리종목, 정리매매, 불성실공시, 우선주, 거래정지)
        val FID_TRGT_EXLS_CLS_CODE: String,
        // 입력 가격 범위 (가격 ~ 가격)
        val FID_INPUT_PRICE_1: String,
        val FID_INPUT_PRICE_2: String,
        // 거래량 조건
        val FID_VOL_CNT: String,
        // 입력 날짜1
        val FID_INPUT_DATE_1: String,
    )

    /**
     * 국내 거래량 랭킹 응답 DTO
     */
    data class KrVolumeRankResponse(
        @JsonProperty("rt_cd") val rtCd: String,   // 응답 코드
        @JsonProperty("msg_cd") val msgCd: String, // 메시지 코드
        @JsonProperty("msg1") val msg1: String,    // 메시지
        @JsonProperty("output") val details: List<KrStockVolumeRankDto> // 세부 정보
    )

    /**
     * 국내 주식 거래량 랭킹 정보 DTO
     */
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class KrStockVolumeRankDto(
        @JsonProperty("hts_kor_isnm") val htsKorIsnm: String?, // HTS 한글 종목명
        @JsonProperty("mksc_shrn_iscd") val mkscShrnIscd: String?, // 유가증권 단축 종목코드
        @JsonProperty("data_rank") val dataRank: String?, // 데이터 순위
        @JsonProperty("stck_prpr") val stckPrpr: String?, // 주식 현재가
        @JsonProperty("prdy_vrss_sign") val prdyVrssSign: String?, // 전일 대비 부호
        @JsonProperty("prdy_vrss") val prdyVrss: String?, // 전일 대비
        @JsonProperty("prdy_ctrt") val prdyCtrt: String?, // 전일 대비율
        @JsonProperty("acml_vol") val acmlVol: String?, // 누적 거래량
        @JsonProperty("prdy_vol") val prdyVol: String?, // 전일 거래량
        @JsonProperty("lstn_stcn") val lstnStcn: String?, // 상장 주수
        @JsonProperty("avrg_vol") val avrgVol: String?, // 평균 거래량
        @JsonProperty("n_befr_clpr_vrss_prpr_rate") val nBefrClprVrssPrprRate: String?, // N일전 종가 대비율
        @JsonProperty("vol_inrt") val volInrt: String?, // 거래량 증가율
        @JsonProperty("vol_tnrt") val volTnrt: String?, // 거래량 회전율
        @JsonProperty("nday_vol_tnrt") val ndayVolTnrt: String?, // N일 거래량 회전율
        @JsonProperty("avrg_tr_pbmn") val avrgTrPbmn: String?, // 평균 거래 대금
        @JsonProperty("tr_pbmn_tnrt") val trPbmnTnrt: String?, // 거래대금 회전율
        @JsonProperty("nday_tr_pbmn_tnrt") val ndayTrPbmnTnrt: String?, // N일 거래대금 회전율
        @JsonProperty("acml_tr_pbmn") val acmlTrPbmn: String?, // 누적 거래 대금
        val national: String = "KR" // 국가 (기본값: KR)
    )

    fun KisApiVolumeRankDto.KrStockVolumeRankDto.toDomain(): StockVolumeRank {
        return StockVolumeRank(
            stockName = htsKorIsnm,
            stockCode = mkscShrnIscd,
            rank = dataRank,
            currentPrice = stckPrpr,
            priceChangeSign = prdyVrssSign,
            priceChange = prdyVrss,
            priceChangeRate = prdyCtrt,
            accumulatedVolume = acmlVol,
            previousVolume = prdyVol,
            listedShares = lstnStcn,
            averageVolume = avrgVol,
            nDayClosingPriceRate = nBefrClprVrssPrprRate,
            volumeIncreaseRate = volInrt,
            volumeTurnoverRate = volTnrt,
            nDayVolumeTurnoverRate = ndayVolTnrt,
            averageTransactionAmount = avrgTrPbmn,
            transactionTurnoverRate = trPbmnTnrt,
            nDayTransactionTurnoverRate = ndayTrPbmnTnrt,
            accumulatedTransactionAmount = acmlTrPbmn,
            country = national
        )
    }
}