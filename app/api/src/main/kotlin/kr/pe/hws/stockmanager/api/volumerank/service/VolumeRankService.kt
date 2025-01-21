package kr.pe.hws.stockmanager.api.volumerank.service

import kr.pe.hws.stockmanager.api.utils.mapper.KrVolumeRankMapper
import kr.pe.hws.stockmanager.api.volumerank.dto.VolumeRankDto
import kr.pe.hws.stockmanager.common.logger.LogHelper.getLogger
import kr.pe.hws.stockmanager.domain.kis.volumerank.KrVolumeRankDomain
import kr.pe.hws.stockmanager.redis.mapper.KrVolumeRankRedisMapper
import kr.pe.hws.stockmanager.redis.repository.KrVolumeRankRepository
import kr.pe.hws.stockmanager.webadapter.fetcher.KisApiFetcher
import org.springframework.stereotype.Service

@Service
class VolumeRankService(
    private val fetcher: KisApiFetcher,
    private val krVolumeRankRepository: KrVolumeRankRepository
) {
    private val log = getLogger<VolumeRankService>()

    fun getVolumeRanks(itemCode: String = "0000"): List<VolumeRankDto> {
        val redisEntities = krVolumeRankRepository.findAll().filterNotNull().toList()

        return if (redisEntities.isNotEmpty()) {
            redisEntities
                .map(KrVolumeRankRedisMapper::fromRedisEntity) // Redis Entity → Domain 변환
                .map(KrVolumeRankMapper::toResponse) // Domain → Response 변환
                .sortedBy { it.rank } // rank 기준으로 정렬
        } else {
            log.info("No entries found in Redis. Fetching from API...")
            fetchAndCacheVolumeRanks(itemCode)
        }
    }

    private fun fetchAndCacheVolumeRanks(itemCode: String): List<VolumeRankDto> {
        val apiResults = runCatching { fetcher.fetchKrStockVolumeRank(itemCode) }
            .getOrElse {
                log.error("Error fetching volume ranks from API: ${it.message}", it)
                return emptyList()
            }

        if (apiResults.isNotEmpty()) {
            saveVolumeRanksToRedis(apiResults)
        } else {
            log.warn("No volume ranks received from API.")
        }

        return apiResults.map(KrVolumeRankMapper::toResponse).sortedBy { it.rank }
    }

    private fun saveVolumeRanksToRedis(domains: List<KrVolumeRankDomain>) {
        val redisEntities = domains.map(KrVolumeRankRedisMapper::toRedisEntity)
        try {
            krVolumeRankRepository.saveAll(redisEntities)
        } catch (ex: Exception) {
            log.error("Error saving volume ranks to Redis: ${ex.message}", ex)
            throw RuntimeException("Failed to save volume ranks to Redis", ex)
        }
    }
}