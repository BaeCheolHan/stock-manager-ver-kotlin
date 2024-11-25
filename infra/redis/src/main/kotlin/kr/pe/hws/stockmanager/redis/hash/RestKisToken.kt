package kr.pe.hws.stockmanager.redis.hash

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash("RestKisToken")
class RestKisToken(
    @Id
    val access_token: String,
    val access_token_token_expired: String,
    val token_type: String,
    @TimeToLive
    val expires_in: Int,
)