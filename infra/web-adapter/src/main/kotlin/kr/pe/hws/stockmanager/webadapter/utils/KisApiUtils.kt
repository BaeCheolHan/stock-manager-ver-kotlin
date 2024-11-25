package kr.pe.hws.stockmanager.webadapter.utils

import jakarta.servlet.http.HttpServletResponse
import kr.pe.hws.stockmanager.redis.hash.RestKisToken
import kr.pe.hws.stockmanager.redis.repository.RestKisTokenRepository
import kr.pe.hws.stockmanager.webadapter.dto.KisApiRequests
import kr.pe.hws.stockmanager.webadapter.feign.client.KisApiFeignClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class KisApiUtils(
    @Value("\${spring.api.kis.appKey}") private val appKey: String,
    @Value("\${spring.api.kis.app-secret}") private val appSecret: String,
    private val restKisTokenRepository: RestKisTokenRepository,
    private val kisApiFeignClient: KisApiFeignClient,
    private val response: HttpServletResponse
) {

    /**
     * 저장된 REST KIS Token을 가져오거나, 새로 생성하여 저장.
     */
    fun getRestKisToken(): RestKisToken {
        return restKisTokenRepository.findAll().filterNotNull().firstOrNull() ?: fetchAndSaveNewToken()
    }

    /**
     * 새 KIS API Token을 생성하여 저장.
     */
    private fun fetchAndSaveNewToken(): RestKisToken {
        val tokenResponse = kisApiFeignClient.getKisApiToken(
            KisApiRequests.KisTokenGenerateRequest(appKey, appSecret)
        )
        return RestKisToken(tokenResponse).also { restKisTokenRepository.save(it) }
    }

    /**
     * 기본 API 헤더 생성.
     * @param transactionId API Transaction ID
     */
    fun createApiHeaders(transactionId: String? = null): HttpHeaders {
        val token = getRestKisToken()
        return HttpHeaders().apply {
            add("authorization", "${token.token_type} ${token.access_token}")
            add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
            add("appkey", appKey)
            add("appsecret", appSecret)
            transactionId?.let { add("tr_id", it) }
        }
    }
}
