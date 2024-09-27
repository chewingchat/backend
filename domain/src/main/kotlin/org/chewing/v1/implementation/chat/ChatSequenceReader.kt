package org.chewing.v1.implementation.chat

import org.chewing.v1.repository.ChatSequenceRepository
import org.springframework.stereotype.Component

@Component
class ChatSequenceReader(private val chatSequenceRepository: ChatSequenceRepository) {

    fun readCurrentSequence(roomId: String): Long {
        return chatSequenceRepository.readCurrentSequence(roomId)
    }
}