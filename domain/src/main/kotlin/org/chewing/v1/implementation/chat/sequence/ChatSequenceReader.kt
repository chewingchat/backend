package org.chewing.v1.implementation.chat.sequence

import org.chewing.v1.model.chat.room.ChatRoomSequenceNumber
import org.chewing.v1.repository.ChatSequenceRepository
import org.springframework.stereotype.Component

@Component
class ChatSequenceReader(
    private val chatSequenceRepository: ChatSequenceRepository,
) {
    fun readCurrent(chatRoomId: String): ChatRoomSequenceNumber {
        return chatSequenceRepository.readCurrent(chatRoomId)
    }
    fun readSeqNumbers(chatRoomIds: List<String>): List<ChatRoomSequenceNumber> {
        return chatSequenceRepository.readSeqNumbers(chatRoomIds)
    }
}