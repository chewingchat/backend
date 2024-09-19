package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.friend.FriendId
import org.chewing.v1.jpaentity.friend.FriendJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
@Repository
interface FriendJpaRepository : JpaRepository<FriendJpaEntity, String> {

    @Query("""
        SELECT f 
        FROM FriendJpaEntity f 
        JOIN FETCH f.friend u 
        WHERE f.id.userId = :userId
    """)
    fun findAllByUserId(@Param("userId") userId: String): List<FriendJpaEntity>

    fun deleteById(friendId: FriendId)
    fun existsById(friendId: FriendId): Boolean
    fun findById(friendId: FriendId): FriendJpaEntity?
    fun findAllByFriendUserIdInAndUserUserId(friendIds: List<String>, userId: String): List<FriendJpaEntity>
    @Query("""
        SELECT f 
        FROM FriendJpaEntity f 
        JOIN FETCH f.friend fu 
        WHERE f.id.userId = :userId AND f.id.friendId = :friendId
    """)
    fun findByUserIdAndFriendId(
        @Param("userId") userId: String,
        @Param("friendId") friendId: String
    ): FriendJpaEntity?

}
