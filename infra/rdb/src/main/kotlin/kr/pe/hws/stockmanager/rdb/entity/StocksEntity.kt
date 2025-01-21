package kr.pe.hws.stockmanager.rdb.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "stocks")
class StocksEntity(

    // 종목 코드
    @Id
    var symbol: String,

    // KOSPI, KOSDAQ...
    var code: String? = null,

    // 종목명
    @NotNull
    var name: String,

    // 국가코드
    @NotNull
    var national: String,

    // 통화 구분
    @NotNull
    var currency: String

)