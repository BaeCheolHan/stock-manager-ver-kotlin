package kr.pe.hws.stockmanager.api.chart.service

import kr.pe.hws.stockmanager.api.chart.dto.IndexChartResponseDto
import kr.pe.hws.stockmanager.api.config.dto.BaseResponse
import kr.pe.hws.stockmanager.common.logger.LogHelper.getLogger
import kr.pe.hws.stockmanager.domain.kis.constants.IndexType
import kr.pe.hws.stockmanager.domain.kis.index.IndexChartDomain
import kr.pe.hws.stockmanager.redis.mapper.IndexChartRedisMapper
import kr.pe.hws.stockmanager.redis.repository.IndexChartRepository
import kr.pe.hws.stockmanager.webadapter.fetcher.KisApiFetcher
import org.springframework.stereotype.Service

@Service
class ChartService(
    private val fetcher: KisApiFetcher,
    private val indexChartRepository: IndexChartRepository
) {
    private val log = getLogger<ChartService>()

    fun getIndexCharts(chartType: String): IndexChartResponseDto {
        val indexTypes = listOf(
            IndexType.KOSPI,
            IndexType.KOSDAQ,
            IndexType.SNP500,
            IndexType.NASDAQ,
            IndexType.DAW,
            IndexType.PHILADELPHIA
        )

        // 모든 지수를 처리하여 Map으로 저장
        val indexCharts = indexTypes.associateWith { indexType -> getOrFetchIndexChart(indexType, chartType) }

        // 결과를 DTO로 변환
        return IndexChartResponseDto(
            kospiChart = indexCharts[IndexType.KOSPI]!!,
            kosdaqChart = indexCharts[IndexType.KOSDAQ]!!,
            snp500Chart = indexCharts[IndexType.SNP500]!!,
            nasdaqChart = indexCharts[IndexType.NASDAQ]!!,
            dawChart = indexCharts[IndexType.DAW]!!,
            philadelphiaChart = indexCharts[IndexType.PHILADELPHIA]!!,
            baseResponse = BaseResponse.success()
        )
    }

    // Redis에서 가져오거나 API 호출 후 저장하는 메서드
    private fun getOrFetchIndexChart(indexType: IndexType, chartType: String): IndexChartDomain.IndexChart {
        return indexChartRepository.findById(indexType.id)
            .map(IndexChartRedisMapper::fromRedisEntity)
            .orElseGet { fetchAndSaveNewIndexChart(indexType, chartType) }
    }

    // Redis에 저장하고 API 호출을 처리하는 메서드
    private fun fetchAndSaveNewIndexChart(indexType: IndexType, chartType: String): IndexChartDomain.IndexChart {
        val response = fetchIndexChartByType(indexType, chartType)
            ?: throw IllegalStateException("API response for $indexType was null")

        val redisEntity = IndexChartRedisMapper.toRedisEntity(response, chartType)
        try {
            indexChartRepository.save(redisEntity)
        } catch (ex: Exception) {
            log.error("Failed to save index chart for $indexType: ${ex.message}", ex)
        }

        return response
    }

    // IndexType에 따른 API 호출
    private fun fetchIndexChartByType(indexType: IndexType, chartType: String): IndexChartDomain.IndexChart? {
        return when (indexType) {
            IndexType.KOSPI, IndexType.KOSDAQ -> fetcher.fetchKrIndexChart(indexType, chartType)
            IndexType.SNP500, IndexType.NASDAQ, IndexType.DAW, IndexType.PHILADELPHIA -> fetcher.fetchOverSeaIndexChart(indexType, chartType)
        }
    }
}