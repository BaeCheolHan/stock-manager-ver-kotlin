package kr.pe.hws.stockmanager.webadapter.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.pe.hws.stockmanager.webadapter.dto.KisApiIndexChartDto.IndexChartDetail
import kr.pe.hws.stockmanager.webadapter.dto.KisApiIndexChartDto.IndexChartSummary

object KisApiVolumeRankDto {
    data class KrVolumeRankRequest(
        // 조건 시장 분류 코드(J)
        val FID_COND_MRKT_DIV_CODE: String,
        // 조건 화면 분류 코드 (20171)
        val FID_COND_SCR_DIV_CODE: String,
        // 입력 종목코드  0000(전체) 기타(업종코드)
        val FID_INPUT_ISCD: String,
        // 분류 구분 코드  	0(전체) 1(보통주) 2(우선주)
        val FID_DIV_CLS_CODE: String,
        // 소속 구분 코드     0:평균거래량 1:거래증가율 2:평균거래회전율 3:거래금액순 4:평균거래금액회전율
        val FID_BLNG_CLS_CODE: String,
        // 대상 구분 코드 	1 or 0 9자리 (차례대로 증거금 30% 40% 50% 60% 100% 신용보증금 30% 40% 50% 60%)
        //ex) "111111111"
        val FID_TRGT_CLS_CODE: String,
        // 대상 제외 구분 코드 	1 or 0 6자리 (차례대로 투자위험/경고/주의 관리종목 정리매매 불성실공시 우선주 거래정지)
        //ex) "000000"
        val FID_TRGT_EXLS_CLS_CODE: String,
        /**
         * 입력 가격1
         * 가격 ~
         * ex) "0"
         *
         * 전체 가격 대상 조회 시 FID_INPUT_PRICE_1, FID_INPUT_PRICE_2 모두 ""(공란) 입력
         */
        val FID_INPUT_PRICE_1: String,
        /**
         * 입력 가격2
         * 가격 ~
         * ex) "0"
         *
         * 전체 가격 대상 조회 시 FID_INPUT_PRICE_1, FID_INPUT_PRICE_2 모두 ""(공란) 입력
         */
        val FID_INPUT_PRICE_2: String,
        /**
         * 거래량 수
         * 	거래량 ~
         * ex) "100000"
         *
         * 전체 거래량 대상 조회 시 FID_VOL_CNT ""(공란) 입력
         */
        val FID_VOL_CNT: String,
        // 입력 날짜1 	""(공란) 입력
        val FID_INPUT_DATE_1: String,
    )

    data class KrVolumeRankResponse (
        @JsonProperty("rt_cd") val rtCd: String,
        @JsonProperty("msg_cd") val msgCd: String,
        @JsonProperty("msg1") val msg1: String,
        @JsonProperty("output") val details: List<KrStockVolumeRank>
    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class KrStockVolumeRank(
        // HTS 한글 종목명
        @JsonProperty("hts_kor_isnm")
        val htsKorIsnm: String? = null,
        // 유가증권 단축 종목코드
        @JsonProperty("mksc_shrn_iscd")
        val mkscShrnIscd: String? = null,
        // 데이터 순위
        @JsonProperty("data_rank")
        val dataRank: String? = null,
        // 주식 현재가
        @JsonProperty("stck_prpr")
        val stckPrpr: String? = null,
        // 전일 대비 부호
        @JsonProperty("prdy_vrss_sign")
        val prdyVrssSign: String? = null,
        // 전일 대비
        @JsonProperty("prdy_vrss")
        val prdyVrss: String? = null,
        // 전일 대비율
        @JsonProperty("prdy_ctrt")
        val prdyCtrt: String? = null,
        // 누적 거래량
        @JsonProperty("acml_vol")
        val acmlVol: String? = null,
        // 전일 거래량
        @JsonProperty("prdy_vol")
        val prdyVol: String? = null,
        // 상장 주수
        @JsonProperty("lstn_stcn")
        val lstnStcn: String? = null,
        // 평균 거래량
        @JsonProperty("avrg_vol")
        val avrgVol: String? = null,
        // N일전종가대비현재가대비율
        @JsonProperty("n_befr_clpr_vrss_prpr_rate")
        val nBefrClprVrssPrprRate: String? = null,
        // 거래량 증가율
        @JsonProperty("vol_inrt")
        val volInrt: String? = null,
        // 거래량 회전율
        @JsonProperty("vol_tnrt")
        val volTnrt: String? = null,
        // N일 거래량 회전율
        @JsonProperty("nday_vol_tnrt")
        val ndayVolTnrt: String? = null,
        // 평균 거래 대금
        @JsonProperty("avrg_tr_pbmn")
        val avrgTrPbmn: String? = null,
        // 거래대금 회전율
        @JsonProperty("tr_pbmn_tnrt")
        val trPbmnTnrt: String? = null,
        // N일 거래대금 회전율
        @JsonProperty("nday_tr_pbmn_tnrt")
        val ndayTrPbmnTnrt: String? = null,
        // 누적 거래 대금
        @JsonProperty("acml_tr_pbmn")
        val acmlTrPbmn: String? = null,
        // 국가
        val national: String = "KR"
    )
}