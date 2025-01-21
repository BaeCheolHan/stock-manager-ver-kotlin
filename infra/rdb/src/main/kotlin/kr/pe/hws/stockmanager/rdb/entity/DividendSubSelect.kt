package kr.pe.hws.stockmanager.rdb.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Subselect
import org.hibernate.annotations.Synchronize
import java.math.BigDecimal

@Entity
@Subselect(
    "SELECT @ROWNUM:=@ROWNUM+1 AS id, member_id, symbol, SUM(dividend) AS total_dividend FROM dividend , (SELECT @ROWNUM:=0) AS R GROUP BY symbol, member_id"
)
@Immutable
@Synchronize("dividend")
class DividendSubSelect(

    @Id
    @GeneratedValue
    var id: Long? = null,

    var memberId: String? = null,

    var symbol: String? = null,

    @Column(name = "total_dividend")
    var totalDividend: BigDecimal? = null

)