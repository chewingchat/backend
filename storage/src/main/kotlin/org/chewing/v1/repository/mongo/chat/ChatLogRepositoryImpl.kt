package org.chewing.v1.repository.mongo.chat

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.chat.log.ChatLog
import org.chewing.v1.model.chat.message.*
import org.chewing.v1.model.chat.room.ChatNumber
import org.chewing.v1.mongoentity.*
import org.chewing.v1.mongorepository.ChatLogMongoRepository
import org.chewing.v1.repository.chat.ChatLogRepository
import org.springframework.stereotype.Repository

@Repository
internal class ChatLogRepositoryImpl(
    private val chatLogMongoRepository: ChatLogMongoRepository,
) : ChatLogRepository {

    // 채팅방의 특정 페이지의 메시지를 조회
    override fun readChatMessages(chatRoomId: String, page: Int): List<ChatLog> {
        // MongoDB에서 chatRoomId와 page로 메시지 조회하고 seqNumber 기준으로 정렬
        val messageEntities = chatLogMongoRepository.findByRoomIdAndPageSortedBySeqNumber(chatRoomId, page)
        return messageEntities.map { it.toChatLog() }
    }

    override fun readChatMessage(messageId: String): ChatLog? =
        chatLogMongoRepository.findById(messageId).map { it.toChatLog() }.orElse(null)

    override fun readLatestMessages(numbers: List<ChatNumber>): List<ChatLog> {
        // MongoDB에서 chatRoomId와 seqNumber로 메시지 조회
        val conditions = numbers.map { mapOf("chatRoomId" to it.chatRoomId, "seqNumber" to it.sequenceNumber) }
        val messageEntities = chatLogMongoRepository.findByRoomIdAndSeqNumbers(conditions)
        return messageEntities.map { it.toChatLog() }
    }

    // 메시지를 삭제하는 기능
    /**
     * room확인 하지 않고
     * 바로 메시지 삭제 할게용
     * 이떄 실제 삭제는 하지않고, type을 delete로 업데이트만 할게용
     */
    override fun removeLog(messageId: String) {
        // 메시지 ID로 MongoDB에서 메시지 조회
        chatLogMongoRepository.updateMessageTypeToDelete(messageId)
    }

    override fun appendChatLog(chatMessage: ChatMessage) {
        chatLogMongoRepository.save(
            ChatMessageMongoEntity.fromChatMessage(chatMessage) ?: throw ConflictException(
                ErrorCode.INVALID_TYPE,
            ),
        )
    }
}
