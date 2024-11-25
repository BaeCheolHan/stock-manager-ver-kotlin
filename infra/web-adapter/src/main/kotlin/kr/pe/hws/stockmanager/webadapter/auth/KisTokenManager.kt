package kr.pe.hws.stockmanager.webadapter.auth

import kr.pe.hws.stockmanager.redis.hash.RestKisToken
import kr.pe.hws.stockmanager.redis.repository.RestKisTokenRepository
import kr.pe.hws.stockmanager.webadapter.dto.KisApiRequests
import kr.pe.hws.stockmanager.webadapter.feign.client.KisApiFeignClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class KisTokenManager(
    private val kisApiFeignClient: KisApiFeignClient,
    private val restKisTokenRepository: RestKisTokenRepository,
    @Value("\${spring.api.kis.appKey}") private val appKey: String,
    @Value("\${spring.api.kis.app-secret}") private val appSecret: String,
) {

    fun getValidToken(): RestKisToken {
        return restKisTokenRepository.findAll().firstOrNull { it != null } ?: fetchAndSaveNewToken()
    }

    private fun fetchAndSaveNewToken(): RestKisToken {
        val newToken = fetchNewToken()
        return saveToken(newToken)
    }

    private fun fetchNewToken(): RestKisToken {
        val tokenResponse = kisApiFeignClient.getKisApiToken(
            KisApiRequests.KisTokenGenerateRequest(appKey, appSecret)
        )
        return RestKisToken(tokenResponse)
    }

    private fun saveToken(token: RestKisToken): RestKisToken {
        return token.also { restKisTokenRepository.save(it) }
    }
}