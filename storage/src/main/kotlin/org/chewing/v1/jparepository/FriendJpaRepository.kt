package org.chewing.v1.jparepository

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
        LEFT JOIN FETCH u.statusEmoticon 
        WHERE f.id.userId = :userId
    """)
    fun findAllByUserIdWithStatus(@Param("userId") userId: String): List<FriendJpaEntity>

    fun deleteByUserIdAndFriendId(userId: String, friendId: String)

    @Query("""
        SELECT f 
        FROM FriendJpaEntity f 
        JOIN FETCH f.friend fu 
        LEFT JOIN FETCH fu.statusEmoticon
        WHERE f.id.userId = :userId AND f.id.friendId = :friendId
    """)
    fun findByUserIdAndFriendIdWithStatus(
        @Param("userId") userId: String,
        @Param("friendId") friendId: String
    ): FriendJpaEntity?

    fun existsByUserIdAndFriendId(userId: String, friendId: String): Boolean

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
