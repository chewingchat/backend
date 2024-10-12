package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.chat.ChatRoomMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ChatRoomMemberJpaRepository : JpaRepository<ChatRoomMemberEntity, String> {
    fun findAllByChatRoomId(chatRoomId: String): List<ChatRoomMemberEntity>
    fun findAllByUserIdAndDeletedFalse(userId: String): List<ChatRoomMemberEntity>
    fun findByChatRoomIdAndUserId(chatRoomId: String, userId: String): ChatRoomMemberEntity?

    fun findByUserIdAndChatRoomId(userId: String, chatRoomId: String): ChatRoomMemberEntity?
    fun findAllByChatRoomIdInAndUserId(chatRoomIds: List<String>, userId: String): List<ChatRoomMemberEntity>

    @Query("""
        SELECT c.chatRoomId 
        FROM ChatRoomMemberEntity c 
        WHERE c.userId IN :userIds 
        GROUP BY c.chatRoomId 
        HAVING COUNT(DISTINCT c.userId) = :size
    """)
    fun findCommonChatRoomIdByUserIds(
        @Param("userIds") userIds: List<String>,
        @Param("size") size: Long
    ): List<String>
}