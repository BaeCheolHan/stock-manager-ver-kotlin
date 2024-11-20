package kr.pe.hws.stockmanager.redis.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EntityScan("kr.pe.hws.stockmanager.redis")
@EnableRedisRepositories(basePackages = ["kr.pe.hws.stockmanager.redis"])
class RedisConfiguration(
    @Value("\${spring.data.redis.host}")
    val redisHost: String,
    @Value("\${spring.data.redis.port}")
    val redisPort: Int
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(redisHost, redisPort)
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory

        // BasicPolymorphicTypeValidator 사용
        val typeValidator = BasicPolymorphicTypeValidator.builder()
            .allowIfSubType(Any::class.java) // 모든 타입 허용
            .build()

        val objectMapper = ObjectMapper()
            .findAndRegisterModules() // JavaTimeModule 및 기타 모듈 자동 등록
            .registerModule(KotlinModule.Builder().build())
            .activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL)

        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        return template
    }
}
