package kr.pe.hws.stockmanager.api.asset.service

import kr.pe.hws.stockmanager.api.asset.dto.AssetChartDto
import kr.pe.hws.stockmanager.rdb.persist.MemberDataService
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AssetService(
    private val memberDataService: MemberDataService
) {
    fun getAssetChartData(memberId: String): AssetChartDto {
        val dailyTotalInvestmentAmountEntity = memberDataService
            .findByIdOrElseThrow(memberId)
            .dailyTotalInvestmentAmounts

        return AssetChartDto(
            xaxisCategories = dailyTotalInvestmentAmountEntity.map { it.date.toString() },
            investmentAmountList = dailyTotalInvestmentAmountEntity.map { it.totalInvestmentAmount ?: BigDecimal.ZERO },
            evaluationAmountList = dailyTotalInvestmentAmountEntity.map { it.evaluationAmount ?: BigDecimal.ZERO }
        )
    }
}