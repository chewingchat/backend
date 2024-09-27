package org.chewing.v1.repository

import org.chewing.v1.model.chat.ChatMessage
import org.chewing.v1.model.chat.ChatMessageLog
import org.chewing.v1.mongoentity.ChatMessageMongoEntity
import org.chewing.v1.mongorepository.ChatMessageMongoRepository
import org.springframework.stereotype.Repository


@Repository
class ChatMessageRepositoryImpl(
    private val chatMessageMongoRepository: ChatMessageMongoRepository
) : ChatMessageRepository {

    override fun appendChatMessage(chatMessage: ChatMessage, page: Int) {
        chatMessageMongoRepository.save(ChatMessageMongoEntity.toEntity(chatMessage, page))
    }

    override fun readChatMessages(roomId: String, page: Int): ChatMessageLog {
        val messageEntities = chatMessageMongoRepository.findByRoomIdAndPage(roomId, page)
        // FriendSeqInfo 리스트를 생성하여 ChatMessageLog에 전달
        val friends = listOf(
            ChatMessage.FriendSeqInfo("sampleFriendId1", 501),
            ChatMessage.FriendSeqInfo("sampleFriendId2", 502)
        )

        return ChatMessageLog.generate(page, roomId, messageEntities.map { it.toChatMessage() }, friends)
    }
    // 메시지 삭제 구현
    override fun deleteMessage(roomId: String, messageId: String) {
        val messageEntity = chatMessageMongoRepository.findById(messageId)
        messageEntity.ifPresent {
            if (it.roomId == roomId) {
                chatMessageMongoRepository.delete(it)  // 메시지를 삭제
            } else {
                throw IllegalArgumentException("해당 채팅방에 메시지가 없습니다.")
            }
        }
    }
}

