package org.chewing.v1.repository

import org.chewing.v1.jpaentity.chat.ChatRoomMemberEntity
import org.chewing.v1.jparepository.ChatRoomMemberJpaRepository
import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.ChatNumber
import org.springframework.stereotype.Repository

@Repository
internal class ChatRoomMemberRepositoryImpl(
    private val chatRoomMemberJpaRepository: ChatRoomMemberJpaRepository
) : ChatRoomMemberRepository {
    override fun readChatRoomMembers(chatRoomId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberJpaRepository.findAllByChatRoomId(chatRoomId).map {
            it.toRoomMember()
        }
    }

    override fun readChatRoomUsers(userId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberJpaRepository.findAllByUserIdAndDeletedFalse(userId).map {
            it.toRoomMember()
        }
    }

    override fun readChatRoomsMember(chatRoomIds: List<String>, userId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberJpaRepository.findAllByChatRoomIdInAndUserId(chatRoomIds, userId).map {
            it.toRoomMember()
        }
    }

    override fun readChatRoomMember(chatRoomId: String, userId: String): ChatRoomMemberInfo? {
        return chatRoomMemberJpaRepository.findByChatRoomIdAndUserId(chatRoomId, userId)?.toRoomMember()
    }

    override fun saveChatRoomMember(chatRoomId: String, userId: String) {
        chatRoomMemberJpaRepository.save(ChatRoomMemberEntity.generate(userId, chatRoomId))
    }

    override fun changeChatRoomFavorite(chatRoomId: String, userId: String, isFavorite: Boolean) {
        chatRoomMemberJpaRepository.findByChatRoomIdAndUserId(chatRoomId, userId)?.let {
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
            it.deleted
        }.map {
            it.toRoomMember()
        }
    }

    override fun appendChatRoomMember(chatRoomId: String, userId: String) {
        chatRoomMemberJpaRepository.findByUserIdAndChatRoomId(userId, chatRoomId)?.let { entity ->
            entity.updateUnDelete()
            chatRoomMemberJpaRepository.save(entity)
        } ?: run {
            chatRoomMemberJpaRepository.save(ChatRoomMemberEntity.generate(userId, chatRoomId))
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

    override fun appendChatRoomMembers(chatRoomId: String, userIds: List<String>) {
        userIds.map { ChatRoomMemberEntity.generate(it, chatRoomId) }.let {
            chatRoomMemberJpaRepository.saveAll(it)
        }
    }

    override fun updateUnDelete(chatRoomId: String, userId: String) {
        chatRoomMemberJpaRepository.findByChatRoomIdAndUserId(chatRoomId, userId)?.let {
            it.updateUnDelete()
            chatRoomMemberJpaRepository.save(it)
        }
    }

    override fun updateRead(userId: String, number: ChatNumber) {
        chatRoomMemberJpaRepository.findByUserIdAndChatRoomId(userId, number.chatRoomId)?.let {
            it.updateRead(number)
            chatRoomMemberJpaRepository.save(it)
        }
    }
}