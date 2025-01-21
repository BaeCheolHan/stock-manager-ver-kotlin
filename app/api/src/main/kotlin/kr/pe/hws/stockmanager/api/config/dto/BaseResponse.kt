package kr.pe.hws.stockmanager.api.config.dto

import kr.pe.hws.stockmanager.common.constants.ResponseCode

data class BaseResponse(
    val code: ResponseCode,
    val message: String
) {
    companion object {
        fun success(message: String = ResponseCode.SUCCESS.message): BaseResponse {
            return BaseResponse(
                code = ResponseCode.SUCCESS,
                message = message
            )
        }

        fun error(message: String = ResponseCode.INTERNAL_ERROR.message): BaseResponse {
            return BaseResponse(
                code = ResponseCode.INTERNAL_ERROR,
                message = message
            )
        }
    }
}
