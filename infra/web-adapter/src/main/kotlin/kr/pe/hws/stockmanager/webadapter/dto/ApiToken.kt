package kr.pe.hws.stockmanager.webadapter.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

object ApiToken {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    open class BaseToken(
        val accessToken: String,
        val tokenType: String,
        val expiresIn: Int
    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class KakaoToken(
        val refreshToken: String,
        val idToken: String,
        val scope: String,
        val refreshTokenExpiresIn: Int // 변경: String → Int
    ) : BaseToken(accessToken = "", tokenType = "", expiresIn = 0) // BaseToken을 상속하여 공통 필드 사용

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class GoogleToken(
        val scope: String,
        val idToken: String
    ) : BaseToken(accessToken = "", tokenType = "", expiresIn = 0)

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class KisToken(
        val accessTokenExpiresAt: Long, // 변경: 의미 명확히 함
    ) : BaseToken(accessToken = "", tokenType = "", expiresIn = 0)
}