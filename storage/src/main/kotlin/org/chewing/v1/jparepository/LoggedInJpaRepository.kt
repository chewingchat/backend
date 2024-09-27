package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.LoggedInEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
internal interface LoggedInJpaRepository : JpaRepository<LoggedInEntity, String>{
    fun deleteByAuthId(authId: String)

}