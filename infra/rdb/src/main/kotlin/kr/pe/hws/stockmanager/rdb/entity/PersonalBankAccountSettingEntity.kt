package kr.pe.hws.stockmanager.rdb.entity

import jakarta.persistence.*
import kr.pe.hws.stockmanager.rdb.entity.base.BaseTimeEntity

@Entity
@Table(name = "personal_bank_account_setting")
class PersonalBankAccountSettingEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var bankAccount: BankAccountEntity? = null,

    var defaultNational: String? = null,

    var defaultCode: String? = null

) : BaseTimeEntity()