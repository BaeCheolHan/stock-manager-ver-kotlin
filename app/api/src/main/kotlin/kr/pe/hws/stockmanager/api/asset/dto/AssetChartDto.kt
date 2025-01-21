package kr.pe.hws.stockmanager.api.asset.dto

import java.math.BigDecimal

data class AssetChartDto (
    val xaxisCategories: List<String>,
    val investmentAmountList: List<BigDecimal>,
    val evaluationAmountList: List<BigDecimal>
)