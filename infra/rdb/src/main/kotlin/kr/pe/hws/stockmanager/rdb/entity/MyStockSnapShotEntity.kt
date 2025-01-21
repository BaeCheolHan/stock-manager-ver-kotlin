package kr.pe.hws.stockmanager.rdb.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "my_stock_snap_shot")
class MyStockSnapShotEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var symbol: String,

    @Column(nullable = false, precision = 24, scale = 6)
    var quantity: BigDecimal,

    @Column(nullable = false, precision = 24, scale = 6)
    var averPrice: BigDecimal,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var bankAccount: BankAccountEntity? = null

)
