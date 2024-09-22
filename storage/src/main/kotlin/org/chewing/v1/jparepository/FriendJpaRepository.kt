package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.friend.FriendId
import org.chewing.v1.jpaentity.friend.FriendJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
@Repository
internal interface FriendJpaRepository : JpaRepository<FriendJpaEntity, String> {

    fun deleteById(friendId: FriendId)
    fun existsById(friendId: FriendId): Boolean
    fun findById(friendId: FriendId): FriendJpaEntity?
    fun findAllByIdUserId(userId: String): List<FriendJpaEntity>
    fun findAllByIdUserIdInAndIdUserId(friendIds: List<String>, userId: String): List<FriendJpaEntity>

}
