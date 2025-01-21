package kr.pe.hws.stockmanager.rdb.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*


@Entity
@Table(name = "personal_setting")
class PersonalSettingEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var member: MemberEntity? = null,

    var defaultBankAccountId: Long? = null
)