package kr.pe.hws.stockmanager.redis.hash

import kr.pe.hws.stockmanager.api.kis.DailyIndexChartPriceWrapper
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

//@RedisHash("krIndexChart")
//class KrIndexChart(
//    @Id
//    val code: String,
//    val data: DailyIndexChartPriceWrapper.KrDailyIndexChart,
//)