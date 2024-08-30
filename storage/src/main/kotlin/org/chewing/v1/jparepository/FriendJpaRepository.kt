package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.FriendJpaEntity
import org.chewing.v1.jpaentity.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
@Repository
interface FriendJpaRepository: JpaRepository<FriendJpaEntity, String> {
    fun findAllByUserId(userId: String): List<FriendJpaEntity>
    fun deleteByUserIdAndFriendId(userId: String, friendId: String)
    fun findByUserIdAndFriendId(userId: String, friendId: String): FriendJpaEntity?
}