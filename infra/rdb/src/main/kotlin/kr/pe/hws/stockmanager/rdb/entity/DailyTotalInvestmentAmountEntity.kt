package kr.pe.hws.stockmanager.rdb.entity

import jakarta.persistence.*
import kr.pe.hws.stockmanager.rdb.entity.base.BaseTimeEntity
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "daily_total_investment_amount")
class DailyTotalInvestmentAmountEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var member: MemberEntity? = null,

    var date: LocalDate? = null,

    var totalInvestmentAmount: BigDecimal? = null,

    var evaluationAmount: BigDecimal? = null

) : BaseTimeEntity()
