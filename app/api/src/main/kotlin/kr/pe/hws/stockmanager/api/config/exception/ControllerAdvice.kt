package kr.pe.hws.stockmanager.api.config.exception

import kr.pe.hws.stockmanager.api.chart.service.ChartService
import kr.pe.hws.stockmanager.api.config.dto.BaseResponse
import kr.pe.hws.stockmanager.common.constants.ErrorLevel
import kr.pe.hws.stockmanager.common.constants.ResponseCode
import kr.pe.hws.stockmanager.common.exception.StockManagerException
import kr.pe.hws.stockmanager.common.logger.LogHelper.getLogger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {

    private val log = getLogger<ControllerAdvice>()

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInternalServerError(e: Exception): ResponseEntity<BaseResponse> {
        log.error(e.message, e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(BaseResponse(ResponseCode.INTERNAL_ERROR, ResponseCode.INTERNAL_ERROR.message))
    }

    @ExceptionHandler(StockManagerException::class)
    fun handleStockManagerException(e: StockManagerException): ResponseEntity<BaseResponse> {
        val status = when (e.code.errorLevel) {
            ErrorLevel.WARN -> HttpStatus.OK
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity.status(status)
            .body(BaseResponse(ResponseCode.INTERNAL_ERROR, e.message ?: "Unknown error"))
    }
}
