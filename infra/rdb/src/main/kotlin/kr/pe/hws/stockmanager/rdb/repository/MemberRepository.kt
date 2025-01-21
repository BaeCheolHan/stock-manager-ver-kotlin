package kr.pe.hws.stockmanager.rdb.repository

import kr.pe.hws.stockmanager.rdb.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<MemberEntity, String> {
}