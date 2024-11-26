package kr.pe.hws.stockmanager.api.volumerank.service

import kr.pe.hws.stockmanager.api.utils.mapper.KrVolumeRankMapper
import kr.pe.hws.stockmanager.api.volumerank.dto.KrVolumeRankResponseDto
import kr.pe.hws.stockmanager.common.LogHelper.getLogger
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


    fun getVolumeRanks(): List<KrVolumeRankResponseDto> {
        val redisEntities = krVolumeRankRepository.findAll().filterNotNull().toList()

        return if (redisEntities.isNotEmpty()) {
            redisEntities
                .map(KrVolumeRankRedisMapper::fromRedisEntity) // Redis Entity → Domain 변환
                .map(KrVolumeRankMapper::toResponse) // Domain → Response 변환
                .sortedBy { it.rank } // rank 기준으로 정렬
        } else {
            fetchAndCacheVolumeRanks() ?: emptyList()
        }
    }

    private fun fetchAndCacheVolumeRanks(): List<KrVolumeRankResponseDto>? {
        val apiResults = runCatching { fetcher.fetchKrStockVolumeRank("0000") }
            .getOrElse {
                log.error("Error fetching volume ranks from API: ${it.message}")
                return null
            }

        if (apiResults.isNotEmpty()) {
            saveVolumeRanksToRedis(apiResults)
        }

        return apiResults.map(KrVolumeRankMapper::toResponse).sortedBy { it.rank }
    }

    private fun saveVolumeRanksToRedis(domains: List<KrVolumeRankDomain>) {
        val redisEntities = domains.map(KrVolumeRankRedisMapper::toRedisEntity)
        krVolumeRankRepository.saveAll(redisEntities)
    }
}