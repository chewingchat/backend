package org.chewing.v1.jparepository

import ChatRoomEntity
import org.chewing.v1.model.chat.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatRoomJpaRepository : JpaRepository<ChatRoomEntity, String> {

    // chatRoomId를 통해 채팅방 정보를 조회하는 메서드
    fun findByChatRoomId(chatRoomId: String): Optional<ChatRoomEntity>

    @Query("""
        SELECT c FROM ChatRoom c 
        WHERE c.latestMessage LIKE %:keyword% 
        OR EXISTS (SELECT f FROM c.chatFriends f WHERE f.friendFirstName LIKE %:keyword% OR f.friendLastName LIKE %:keyword%)
    """)
    fun searchByKeyword(@Param("keyword") keyword: String): List<ChatRoom>
}