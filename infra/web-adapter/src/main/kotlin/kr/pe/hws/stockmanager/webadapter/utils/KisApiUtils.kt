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
    @Value("\${spring.api.kis.appKey}")
    var appKey: String,
    @Value("\${spring.api.kis.app-secret}")
    val appSecret: String,
    val restKisTokenRepository: RestKisTokenRepository,
    val kisApiFeignClient: KisApiFeignClient,
    private val response: HttpServletResponse,
) {

    fun getRestKisToken(): RestKisToken {
        val token = restKisTokenRepository.findAll()
            .firstOrNull { it != null } // 가장 첫 번째 유효한 토큰을 찾음

        return token ?: fetchAndSaveNewToken()
    }

    private fun fetchAndSaveNewToken(): RestKisToken {
        val response = kisApiFeignClient.getKisApiToken(
            KisApiRequests.KisTokenGenerateRequest(
                appKey,
                appSecret
            )
        )

        val token = RestKisToken(response)
        restKisTokenRepository.save(token)
        return token
    }

    fun getDefaultApiHeader(transactionId: String?): HttpHeaders {
        val header = HttpHeaders()
        val token = getRestKisToken()

        header.add("authorization", """${token.token_type} ${token.access_token}""")
        header.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
        header.add("appkey", appKey)
        header.add("appsecret", appSecret)

        transactionId?.let {
            header.add("tr_id", it)
        }

        return header
    }
}
