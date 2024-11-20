package kr.pe.hws.stockmanager.redis.repository

import kr.pe.hws.stockmanager.redis.hash.RestKisToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RestKisTokenRepository : CrudRepository<RestKisToken, String>
