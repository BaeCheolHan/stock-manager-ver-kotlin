package kr.pe.hws.stockmanager.api.utils.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LogHelper {
    inline fun <reified T> getLogger(): Logger = LoggerFactory.getLogger(T::class.java)
}