package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.user.UserStatusJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
internal interface UserStatusJpaRepository : JpaRepository<UserStatusJpaEntity, String> {
    fun findAllByUserId(userId: String): List<UserStatusJpaEntity>
    fun findBySelectedTrue(): Optional<UserStatusJpaEntity>
    fun findAllBySelectedTrueAndUserIdIn(userIds: List<String>): List<UserStatusJpaEntity>
}