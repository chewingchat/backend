package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.FriendSearchJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FriendSearchJpaRepository : JpaRepository<FriendSearchJpaEntity, String> {
    fun findAllByUserId(userId: String): List<FriendSearchJpaEntity>
}