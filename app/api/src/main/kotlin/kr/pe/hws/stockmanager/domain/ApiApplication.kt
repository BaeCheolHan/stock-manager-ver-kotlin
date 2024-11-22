package kr.pe.hws.stockmanager.domain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = ["kr.pe.hws.stockmanager"])
@EnableFeignClients(basePackages = ["kr.pe.hws.stockmanager"])
class InternalApiApplication

fun main(args: Array<String>) {
    runApplication<InternalApiApplication>(*args)
}
