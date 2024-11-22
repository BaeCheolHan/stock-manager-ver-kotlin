package kr.pe.hws.stockmanager.domain.kis.constants

enum class IndexType (val id: String, val code: String, val national: National) {
    KOSPI("KOSPI", "0001", National.KR),
    KOSDAQ("KOSDAQ", "1001", National.KR),
    SNP500("SNP500", "SPX", National.US),
    NASDAQ("NASDAQ", "COMP", National.US),
    DAW("DAW", ".DJI", National.US),
    PHILADELPHIA("PHILADELPHIA", "SOX", National.US),
}