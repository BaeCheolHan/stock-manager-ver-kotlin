package kr.pe.hws.stockmanager.rdb.entity

import jakarta.persistence.*
import kr.pe.hws.stockmanager.rdb.entity.base.BaseTimeEntity
import java.math.BigDecimal

@Entity
@Table(name = "dividend")
class DividendEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var memberId: String? = null,

    var year: Int? = null,

    var month: Int? = null,

    var day: Int? = null,

    var symbol: String? = null,

    @Column(nullable = false, precision = 24, scale = 6)
    var dividend: BigDecimal

) : BaseTimeEntity()
