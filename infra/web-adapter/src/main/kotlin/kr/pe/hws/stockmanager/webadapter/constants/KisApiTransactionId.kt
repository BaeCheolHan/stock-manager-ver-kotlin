package kr.pe.hws.stockmanager.webadapter.constants

enum class KisApiTransactionId(private val transactionId: String) {
    OVER_SEA_INDEX_CHART_PRICE ("FHKST03030100"),
    KR_INDEX_CHART_PRICE ("FHKUP03500100"),
    OVERSEA_STOCK_DETAIL_PRICE ("HHDFS76200200"),
    KR_VOLUME_RANK ("FHPST01710000");

    fun getTransactionId(): String = transactionId

    companion object {
        fun fromTransactionId(transactionId: String): KisApiTransactionId? {
            return values().find { it.transactionId == transactionId }
        }
    }
}
