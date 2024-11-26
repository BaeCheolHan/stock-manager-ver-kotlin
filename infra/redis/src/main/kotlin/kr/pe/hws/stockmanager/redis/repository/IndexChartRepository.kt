package kr.pe.hws.stockmanager.redis.repository

import kr.pe.hws.stockmanager.redis.hash.IndexChartRedisEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IndexChartRepository: CrudRepository<IndexChartRedisEntity, String> {
}