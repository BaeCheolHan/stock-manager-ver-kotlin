package kr.pe.hws.stockmanager.common.exception

import kr.pe.hws.stockmanager.common.constants.ResponseCode

class StockManagerException : RuntimeException {

    val code: ResponseCode

    constructor(message: String, code: ResponseCode) : super(message) {
        this.code = code
    }

    constructor(code: ResponseCode) : super(code.message) {
        this.code = code
    }
}