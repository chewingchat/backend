package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.LoggedInEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LoggedInJpaRepository : JpaRepository<LoggedInEntity, String>{
    fun findByDeviceId(deviceId: String): LoggedInEntity?
    fun findByUserId(userId: String): LoggedInEntity?
}