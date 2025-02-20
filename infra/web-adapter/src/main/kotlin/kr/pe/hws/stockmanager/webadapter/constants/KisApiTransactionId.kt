package kr.pe.hws.stockmanager.webadapter.constants

enum class KisApiTransactionId(private val transactionId: String) {
    OVER_SEA_INDEX_CHART_PRICE ("FHKST03030100"),
    KR_INDEX_CHART_PRICE ("FHKUP03500100"),
    OVERSEA_STOCK_DETAIL_PRICE ("HHDFS76200200"),
    KR_VOLUME_RANK ("FHPST01710000"),
    KR_STOCK_PRICE ("FHKST01010100"),
    OVER_SEA_STOCK_PRICE ("HHDFS76200200");


    fun getTransactionId(): String = transactionId

    companion object {
        fun fromTransactionId(transactionId: String): KisApiTransactionId? {
            return values().find { it.transactionId == transactionId }
        }
    }
}
