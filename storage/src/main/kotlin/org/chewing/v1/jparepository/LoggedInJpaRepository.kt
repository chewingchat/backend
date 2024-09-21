package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.LoggedInEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LoggedInJpaRepository : JpaRepository<LoggedInEntity, String>{
    fun findByAuthAuthId(authId: String): LoggedInEntity?
    // 추가한 부분
    // 특정 사용자의 로그인 정보를 찾는 메서드
    fun findByUserId(loggedInId: String): Optional<LoggedInEntity>
    // 추기
    fun deleteByUserId(userId: String)

}