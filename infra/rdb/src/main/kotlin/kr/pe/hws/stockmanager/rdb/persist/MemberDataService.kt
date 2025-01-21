package kr.pe.hws.stockmanager.rdb.persist

import kr.pe.hws.stockmanager.common.constants.ResponseCode
import kr.pe.hws.stockmanager.common.exception.StockManagerException
import kr.pe.hws.stockmanager.rdb.entity.MemberEntity
import kr.pe.hws.stockmanager.rdb.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberDataService(private val repository: MemberRepository) {

    fun findByIdOrElseThrow(memberId: String): MemberEntity {
        return repository.findById(memberId).orElseThrow { StockManagerException(ResponseCode.NOT_FOUND_ID) }
    }
}