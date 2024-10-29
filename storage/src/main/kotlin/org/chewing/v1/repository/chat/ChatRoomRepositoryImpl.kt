package org.chewing.v1.repository.chat

import org.chewing.v1.jpaentity.chat.ChatRoomEntity
import org.chewing.v1.jparepository.chat.ChatRoomJpaRepository
import org.chewing.v1.model.chat.room.ChatRoomInfo
import org.springframework.stereotype.Repository


@Repository
/**
 * 채팅방 관련한 메소드만 들어갈게용
 * */
internal class ChatRoomRepositoryImpl(
    private val chatRoomJpaRepository: ChatRoomJpaRepository, // JPA 레포지토리

) : ChatRoomRepository {
    override fun readChatRoom(chatRoomId: String): ChatRoomInfo {
        return chatRoomJpaRepository.findByChatRoomId(chatRoomId)
            .orElse(null).toChatRoomInfo()
    }

    /**
     * 채팅 방 찾는 로직
     * */
    override fun findByChatRoomId(chatRoomId: String): ChatRoomInfo {
        return chatRoomJpaRepository.findByChatRoomId(chatRoomId).orElse(null).toChatRoomInfo()
    }

    override fun appendChatRoom(isGroup: Boolean): String {
        return chatRoomJpaRepository.save(ChatRoomEntity.generate(isGroup)).toChatRoomId()
    }

    override fun readChatRooms(chatRoomIds: List<String>): List<ChatRoomInfo> {
        return chatRoomJpaRepository.findByChatRoomIdIn(chatRoomIds).map { it.toChatRoomInfo() }
    }

}