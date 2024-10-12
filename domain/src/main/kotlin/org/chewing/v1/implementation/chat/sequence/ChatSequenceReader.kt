package org.chewing.v1.implementation.chat.sequence

import org.chewing.v1.model.chat.room.ChatRoomSequenceNumber
import org.chewing.v1.repository.ChatSequenceRepository
import org.springframework.stereotype.Component

@Component
class ChatSequenceReader(
    private val chatSequenceRepository: ChatSequenceRepository,
) {
    fun readCurrent(roomId: String): ChatRoomSequenceNumber {
        return chatSequenceRepository.readCurrent(roomId)
    }
    fun readSeqNumbers(roomIds: List<String>): List<ChatRoomSequenceNumber> {
        return chatSequenceRepository.readSeqNumbers(roomIds)
    }
}