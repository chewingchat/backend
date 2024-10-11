package org.chewing.v1.implementation.chat.sequence

import org.chewing.v1.repository.ChatSequenceRepository
import org.springframework.stereotype.Component

@Component
class ChatSequenceUpdater(private val chatSequenceRepository: ChatSequenceRepository) {

    fun updateSequenceIncrement(roomId: String): Long {
        return chatSequenceRepository.updateSequenceIncrement(roomId)
    }
}
