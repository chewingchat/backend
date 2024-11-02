package org.chewing.v1.repository.chat

import org.chewing.v1.jpaentity.chat.ChatRoomMemberId
import org.chewing.v1.jpaentity.chat.PersonalChatRoomMemberJpaEntity
import org.chewing.v1.jparepository.chat.PersonalChatRoomMemberJpaRepository
import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.ChatNumber
import org.springframework.stereotype.Repository

@Repository
internal class PersonalChatRoomMemberRepositoryImpl(
    private val personalChatRoomMemberJpaRepository: PersonalChatRoomMemberJpaRepository
) : PersonalChatRoomMemberRepository {
    override fun readFriend(chatRoomId: String, userId: String): ChatRoomMemberInfo? {
        return personalChatRoomMemberJpaRepository.findById(ChatRoomMemberId(chatRoomId, userId)).orElse(null)
            ?.toRoomFriend()
    }

    override fun readIdIfExist(userId: String, friendId: String): String? {
        return personalChatRoomMemberJpaRepository.findPersonalChatRoomId(userId, friendId)
    }

    override fun reads(userId: String): List<ChatRoomMemberInfo> {
        return personalChatRoomMemberJpaRepository.findAllByIdUserId(userId).flatMap {
            listOf(it.toRoomOwned(), it.toRoomFriend())
        }
    }

    override fun appendIfNotExist(chatRoomId: String, userId: String, friendId: String, number: ChatNumber) {
        personalChatRoomMemberJpaRepository.findById(ChatRoomMemberId(chatRoomId, friendId)).orElseGet {
            val entity = PersonalChatRoomMemberJpaEntity.generate(
                friendId,
                userId,
                chatRoomId,
                number
            )
            personalChatRoomMemberJpaRepository.save(entity)
        }
    }

    override fun updateRead(userId: String, number: ChatNumber) {
        personalChatRoomMemberJpaRepository.findById(ChatRoomMemberId(number.chatRoomId, userId)).ifPresent {
            it.updateRead(number)
        }
    }

    override fun updateFavorite(chatRoomId: String, userId: String, isFavorite: Boolean) {
        personalChatRoomMemberJpaRepository.findById(ChatRoomMemberId(chatRoomId, userId)).ifPresent {
            it.updateFavorite(isFavorite)
        }
    }

    override fun removes(chatRoomIds: List<String>, userId: String) {
        val chatRoomMemberIds = chatRoomIds.map { ChatRoomMemberId(it, userId) }
        personalChatRoomMemberJpaRepository.deleteAllById(chatRoomMemberIds)
    }
}