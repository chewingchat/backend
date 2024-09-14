package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.user.UserStatusJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserStatusJpaRepository : JpaRepository<UserStatusJpaEntity, String> {
    @Query("SELECT u FROM UserStatusJpaEntity u JOIN FETCH u.user JOIN FETCH u.emoticon WHERE u.user.userId = :userId AND u.selected = true")
    fun findSelectedUserStatusWithUser(userId: String): Optional<UserStatusJpaEntity>

    @Query("SELECT u FROM UserStatusJpaEntity u JOIN FETCH u.emoticon WHERE u.user.userId = :userId")
    fun findAllByUserId(userId: String): List<UserStatusJpaEntity>
    @Query("SELECT u FROM UserStatusJpaEntity u WHERE u.user.userId = :userId AND u.selected = true")
    fun findSelectedUserStatus(userId: String): Optional<UserStatusJpaEntity>

    @Query("SELECT u FROM UserStatusJpaEntity u JOIN FETCH u.user JOIN FETCH u.emoticon WHERE u.user.userId IN :userIds AND u.selected = true")
    fun findSelectedUsersStatusWithUser(userIds: List<String>): List<UserStatusJpaEntity>
}