package kr.pe.hws.stockmanager.rdb.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import kr.pe.hws.stockmanager.rdb.constants.Bank
import kr.pe.hws.stockmanager.rdb.entity.base.BaseTimeEntity

@Entity
@Table(name = "bank_account")
class BankAccountEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var memo: String? = null,

    var alias: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var bank: Bank,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var member: MemberEntity? = null,

    @JsonManagedReference
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var stocks: MutableList<StockEntity> = mutableListOf(),

    @JsonManagedReference
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var myStockSnapShot: MutableList<MyStockSnapShotEntity> = mutableListOf(),

    @JsonManagedReference
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var depositWithdrawalHistories: MutableList<DepositWithdrawalHistoryEntity> = mutableListOf(),

    @OneToOne(mappedBy = "bankAccount", orphanRemoval = true, fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var personalBankAccountSetting: PersonalBankAccountSettingEntity? = null

) : BaseTimeEntity()
