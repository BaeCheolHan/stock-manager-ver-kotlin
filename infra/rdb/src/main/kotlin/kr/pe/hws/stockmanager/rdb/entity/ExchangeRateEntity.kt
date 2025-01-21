package kr.pe.hws.stockmanager.rdb.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table
class ExchangeRateEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var code: String? = null,

    var currencyCode: String? = null,

    var currencyName: String? = null,

    var country: String? = null,

    var name: String? = null,

    var date: String? = null,

    var time: String? = null,

    var recurrenceCount: Int? = null,

    var basePrice: BigDecimal? = null,

    var openingPrice: BigDecimal? = null,

    var highPrice: BigDecimal? = null,

    var lowPrice: BigDecimal? = null,

    @Column(name = "\"change\"")
    var change: String? = null,

    var changePrice: BigDecimal? = null,

    var cashBuyingPrice: BigDecimal? = null,

    var cashSellingPrice: BigDecimal? = null,

    var ttBuyingPrice: BigDecimal? = null,

    var ttSellingPrice: BigDecimal? = null,

    var tcBuyingPrice: BigDecimal? = null,

    var fcSellingPrice: BigDecimal? = null,

    var exchangeCommission: BigDecimal? = null,

    var usDollarRate: BigDecimal? = null,

    var high52wPrice: BigDecimal? = null,

    var high52wDate: String? = null,

    var low52wPrice: BigDecimal? = null,

    var low52wDate: String? = null,

    var currencyUnit: Int? = null,

    var provider: String? = null,

    var timestamp: Long? = null,

    var createdAt: String? = null,

    var modifiedAt: String? = null,

    var signedChangePrice: BigDecimal? = null,

    var signedChangeRate: BigDecimal? = null,

    var changeRate: BigDecimal? = null
)