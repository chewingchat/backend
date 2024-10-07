package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.friend.UserSearchJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface UserSearchJpaRepository : JpaRepository<UserSearchJpaEntity, String> {
    fun findAllByUserIdOrderByCreatedAt(userId: String): List<UserSearchJpaEntity>
}