package kr.pe.hws.stockmanager.webadapter.dto

object KisApiRequests {
    data class KisTokenGenerateRequest(
        val grant_type: String,
        val appkey: String,
        val appsecret: String,
    ) {
        constructor(appKey: String, appSecret: String) : this("client_credentials", appKey, appSecret)
    }

}