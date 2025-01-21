package kr.pe.hws.stockmanager.rdb.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "login_history")
@EntityListeners(AuditingEntityListener::class)
class LoginHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var member: MemberEntity? = null,

    @CreatedDate
    var loginAt: LocalDateTime? = null

)
