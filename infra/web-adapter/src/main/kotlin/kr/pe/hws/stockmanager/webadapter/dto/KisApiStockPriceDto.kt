package kr.pe.hws.stockmanager.webadapter.dto

object KisApiStockPriceDto {
    data class KrStockPriceRequest(
        val fid_cond_mrkt_div_code: String,
        val fid_input_iscd: String,
    )

    data class OverSeaStockPriceRequest(
        val AUTH: String,
        val EXCD: String,
        val SYMB: String,
    )
}