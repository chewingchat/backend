package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.FriendJpaEntity
import org.chewing.v1.jpaentity.SortJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SortJpaRepository : JpaRepository<SortJpaEntity, String> {
    fun findByUserId(userId: String): SortJpaEntity
}