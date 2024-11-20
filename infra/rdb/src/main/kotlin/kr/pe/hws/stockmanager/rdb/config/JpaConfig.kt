package kr.pe.hws.stockmanager.rdb.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(basePackages = ["kr.pe.hws.stockmanager.rdb"])
@EntityScan("kr.pe.hws.stockmanager.rdb")
class JpaConfig(
    val datasource: DataSource
)