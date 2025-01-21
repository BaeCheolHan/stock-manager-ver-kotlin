package kr.pe.hws.stockmanager.rdb.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import kr.pe.hws.stockmanager.rdb.entity.base.BaseTimeEntity
import java.math.BigDecimal

@Entity
@Table(name = "stock")
class StockEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var symbol: String,

    @Column(nullable = false, precision = 24, scale = 6)
    var quantity: BigDecimal,

    @Column(nullable = false, precision = 24, scale = 6)
    var price: BigDecimal,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var bankAccount: BankAccountEntity? = null

) : BaseTimeEntity()