package org.chewing.v1.model.chat.room

class ChatRoomSequenceNumber private constructor(
    val sequenceNumber: Int,
    val roomId: String
) {
    companion object {
        fun of(sequenceNumber: Int, roomId: String): ChatRoomSequenceNumber {
            return ChatRoomSequenceNumber(sequenceNumber, roomId)
        }
    }
}