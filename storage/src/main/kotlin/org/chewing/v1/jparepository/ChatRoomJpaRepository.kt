package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.chat.ChatRoomEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
internal interface ChatRoomJpaRepository : JpaRepository<ChatRoomEntity, String> {

    // chatRoomId를 통해 채팅방 정보를 조회하는 메서드
    fun findByChatRoomId(chatRoomId: String): Optional<ChatRoomEntity>

    fun findByChatRoomIdIn(chatRoomIds: List<String>): List<ChatRoomEntity>
    /**
     * 채팅방 검색이 피그마 상으로는 채팅방 참여자 검색인 거 같아서 제거 할게용
     * */

//    @Query("""
//        SELECT c FROM ChatRoomEntity c
//        WHERE c.latestMessage LIKE %:keyword%
//        OR EXISTS (SELECT f FROM c.chatFriends f WHERE f.friendFirstName LIKE %:keyword% OR f.friendLastName LIKE %:keyword%)
//    """)
//    fun searchByKeyword(@Param("keyword") keyword: String): List<ChatRoom>
}