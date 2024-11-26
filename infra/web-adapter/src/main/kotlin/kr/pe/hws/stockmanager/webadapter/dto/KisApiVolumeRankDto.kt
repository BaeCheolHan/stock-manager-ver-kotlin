package kr.pe.hws.stockmanager.webadapter.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.pe.hws.stockmanager.domain.kis.volumerank.KrVolumeRankDomain
import java.math.BigDecimal

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
        @JsonProperty("output") val details: List<KrVolumeRankDto> // 세부 정보
    )

    /**
     * 주식 거래량 랭킹 정보 DTO
     */
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class KrVolumeRankDto(
        val htsKorIsnm: String?,             // HTS 한글 종목명
        val mkscShrnIscd: String?,           // 단축 종목 코드
        val dataRank: Int?,                   // 데이터 순위
        val stckPrpr: BigDecimal?,            // 현재가
        val prdyVrssSign: String?,           // 전일 대비 부호
        val prdyVrss: BigDecimal?,            // 전일 대비
        val prdyCtrt: BigDecimal?,            // 전일 대비율
        val acmlVol: Long?,                   // 누적 거래량
        val prdyVol: Long?,                   // 전일 거래량
        val lstnStcn: Long?,                  // 상장 주수
        val avrgVol: Long?,                   // 평균 거래량
        val nBefrClprVrssPrprRate: BigDecimal?, // N일 전 종가 대비율
        val volInrt: BigDecimal?,             // 거래량 증가율
        val volTnrt: BigDecimal?,             // 거래량 회전율
        val ndayVolTnrt: BigDecimal?,        // N일 거래량 회전율
        val avrgTrPbmn: BigDecimal?,         // 평균 거래 대금
        val trPbmnTnrt: BigDecimal?,         // 거래 대금 회전율
        val ndayTrPbmnTnrt: BigDecimal?,    // N일 거래 대금 회전율
        val acmlTrPbmn: BigDecimal?          // 누적 거래 대금
    )


    fun KrVolumeRankDto.toDomain(): KrVolumeRankDomain {
        return KrVolumeRankDomain(
            stockName = this.htsKorIsnm ?: throw IllegalArgumentException("stockName cannot be null"),
            stockCode = this.mkscShrnIscd ?: throw IllegalArgumentException("stockCode cannot be null"),
            rank = this.dataRank ?: throw IllegalArgumentException("rank cannot be null"),
            currentPrice = this.stckPrpr ?: throw IllegalArgumentException("currentPrice cannot be null"),
            priceChangeSign = this.prdyVrssSign ?: throw IllegalArgumentException("priceChangeSign cannot be null"),
            priceChange = this.prdyVrss ?: throw IllegalArgumentException("priceChange cannot be null"),
            priceChangeRate = this.prdyCtrt ?: throw IllegalArgumentException("priceChangeRate cannot be null"),
            accumulatedVolume = this.acmlVol ?: throw IllegalArgumentException("accumulatedVolume cannot be null"),
            previousVolume = this.prdyVol ?: throw IllegalArgumentException("previousVolume cannot be null"),
            listedShares = this.lstnStcn ?: throw IllegalArgumentException("listedShares cannot be null"),
            averageVolume = this.avrgVol ?: throw IllegalArgumentException("averageVolume cannot be null"),
            nDayClosingPriceRate = this.nBefrClprVrssPrprRate ?: throw IllegalArgumentException("nDayClosingPriceRate cannot be null"),
            volumeIncreaseRate = this.volInrt ?: throw IllegalArgumentException("volumeIncreaseRate cannot be null"),
            volumeTurnoverRate = this.volTnrt ?: throw IllegalArgumentException("volumeTurnoverRate cannot be null"),
            nDayVolumeTurnoverRate = this.ndayVolTnrt ?: throw IllegalArgumentException("nDayVolumeTurnoverRate cannot be null"),
            averageTransactionAmount = this.avrgTrPbmn ?: throw IllegalArgumentException("averageTransactionAmount cannot be null"),
            transactionTurnoverRate = this.trPbmnTnrt ?: throw IllegalArgumentException("transactionTurnoverRate cannot be null"),
            nDayTransactionTurnoverRate = this.ndayTrPbmnTnrt ?: throw IllegalArgumentException("nDayTransactionTurnoverRate cannot be null"),
            accumulatedTransactionAmount = this.acmlTrPbmn ?: throw IllegalArgumentException("accumulatedTransactionAmount cannot be null")
        )
    }
}