package org.chewing.v1.repository

import org.chewing.v1.jpaentity.chat.ChatRoomMemberEntity
import org.chewing.v1.jparepository.ChatRoomMemberJpaRepository
import org.chewing.v1.model.chat.ChatRoomMemberInfo
import org.springframework.stereotype.Repository

@Repository
internal class ChatRoomMemberRepositoryImpl(
    private val chatRoomMemberJpaRepository: ChatRoomMemberJpaRepository
) : ChatRoomMemberRepository {
    override fun readChatRoomMembers(roomId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberJpaRepository.findAllByChatRoomId(roomId).map {
            it.toRoomMember()
        }
    }

    override fun readChatRoomUsers(userId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberJpaRepository.findAllByUserIdAndDeletedFalse(userId).map {
            it.toRoomMember()
        }
    }

    override fun readChatRoomsMember(roomIds: List<String>, userId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberJpaRepository.findAllByChatRoomIdInAndUserId(roomIds, userId).map {
            it.toRoomMember()
        }
    }

    override fun readChatRoomMember(roomId: String, userId: String): ChatRoomMemberInfo? {
        return chatRoomMemberJpaRepository.findByChatRoomIdAndUserId(roomId, userId)?.toRoomMember()
    }

    override fun saveChatRoomMember(roomId: String, userId: String) {
        chatRoomMemberJpaRepository.save(ChatRoomMemberEntity.generate(userId, roomId))
    }

    override fun changeChatRoomFavorite(roomId: String, userId: String, isFavorite: Boolean) {
        chatRoomMemberJpaRepository.findByChatRoomIdAndUserId(roomId, userId)?.let {
            it.updateFavorite(isFavorite)
            chatRoomMemberJpaRepository.save(it)
        }
    }

    override fun removeChatRoomMembers(chatRoomIds: List<String>, userId: String): List<ChatRoomMemberInfo> {
        // 한 번만 조회한 후 상태 업데이트 및 변환 작업을 수행
        val entities = chatRoomMemberJpaRepository.findAllByChatRoomIdInAndUserId(chatRoomIds, userId)

        // 상태를 업데이트하면서, 동시에 변환 수행
        return entities.map {
            it.updateDelete()
            chatRoomMemberJpaRepository.save(it)
            it
        }.filter {
            it.isDeleted
        }.map {
            it.toRoomMember()
        }
    }

    override fun appendChatRoomMember(roomId: String, userId: String) {
        chatRoomMemberJpaRepository.findByUserIdAndChatRoomId(userId, roomId)?.let { entity ->
            entity.updateUnDelete()
            chatRoomMemberJpaRepository.save(entity)
        } ?: run {
            chatRoomMemberJpaRepository.save(ChatRoomMemberEntity.generate(userId, roomId))
        }
    }

    override fun readPersonalChatRoomId(userId: String, friendId: String): String? {
        chatRoomMemberJpaRepository.findCommonChatRoomIdByUserIds(listOf(userId, friendId), 2).let { entities ->
            return if (entities.isEmpty()) {
                null
            } else {
                entities.first()
            }
        }
    }

    override fun appendChatRoomMembers(roomId: String, userIds: List<String>) {
        userIds.map { ChatRoomMemberEntity.generate(it, roomId) }.let {
            chatRoomMemberJpaRepository.saveAll(it)
        }
    }

    override fun updateUnDelete(roomId: String, userId: String) {
        chatRoomMemberJpaRepository.findByChatRoomIdAndUserId(roomId, userId)?.let {
            it.updateUnDelete()
            chatRoomMemberJpaRepository.save(it)
        }
    }
}