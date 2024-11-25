package kr.pe.hws.stockmanager.webadapter.utils

import kr.pe.hws.stockmanager.webadapter.auth.KisTokenManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class KisApiUtils(
    @Value("\${spring.api.kis.appKey}") private val appKey: String,
    @Value("\${spring.api.kis.app-secret}") private val appSecret: String,
    private val kisTokenManager: KisTokenManager
) {

    fun createApiHeaders(transactionId: String? = null): HttpHeaders {
        val token = kisTokenManager.getValidToken()
        return HttpHeaders().apply {
            add("authorization", "${token.token_type} ${token.access_token}")
            add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
            add("appkey", appKey)
            add("appsecret", appSecret)
            transactionId?.let { add("tr_id", it) }
        }
    }
}
