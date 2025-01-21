package kr.pe.hws.stockmanager.rdb.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import kr.pe.hws.stockmanager.rdb.constants.SnsType
import kr.pe.hws.stockmanager.rdb.entity.base.BaseTimeEntity

@Entity
@Table(name = "member")
class MemberEntity (

    @Id
    var id: String,

    @Enumerated(EnumType.STRING)
    var snsType: SnsType,

    var email: String?,

    var nickName: String?,

    var password: String?,

    @JsonManagedReference
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var bankAccount: MutableList<BankAccountEntity> = mutableListOf(),

    @JsonManagedReference
    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var personalSetting: PersonalSettingEntity? = null,

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    var dailyTotalInvestmentAmounts: MutableList<DailyTotalInvestmentAmountEntity> = mutableListOf(),

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var loginHistories: MutableList<LoginHistoryEntity> = mutableListOf(),

    var loginId: String?
) : BaseTimeEntity()