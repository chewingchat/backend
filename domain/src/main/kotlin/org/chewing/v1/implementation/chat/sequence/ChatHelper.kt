package org.chewing.v1.implementation.chat.sequence

import org.chewing.v1.model.chat.room.ChatRoomNumber
import org.springframework.stereotype.Component

@Component
class ChatHelper(
    private val chatSequenceReader: ChatSequenceReader,
    private val chatSequenceUpdater: ChatSequenceUpdater,
) {
    companion object {
        const val PAGE_SIZE = 50
    }

    fun readLastPage(roomId: String): Int {
        return (chatSequenceReader.readCurrent(roomId).sequenceNumber / PAGE_SIZE)
    }


    fun readNumbers(roomIds: List<String>): List<ChatRoomNumber> {
        val sequenceNumbers = chatSequenceReader.readSeqNumbers(roomIds)
        return sequenceNumbers.map {
            ChatRoomNumber.of(it, (it.sequenceNumber / PAGE_SIZE))
        }
    }

    fun findNextNumber(roomId: String): ChatRoomNumber {
        val nextSequenceNumber = chatSequenceUpdater.updateSequenceIncrement(roomId)
        return ChatRoomNumber.of(nextSequenceNumber, (nextSequenceNumber.sequenceNumber / PAGE_SIZE))
    }
}