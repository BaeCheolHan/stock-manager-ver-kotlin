package kr.pe.hws.stockmanager.redis.repository

import kr.pe.hws.stockmanager.redis.hash.KrVolumeRankRedisEntity
import org.springframework.data.repository.CrudRepository

interface KrVolumeRankRepository: CrudRepository<KrVolumeRankRedisEntity, String> {
}