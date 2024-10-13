package org.chewing.v1.model.chat.room

class ChatRoomSequenceNumber private constructor(
    val sequenceNumber: Int,
    val chatRoomId: String
) {
    companion object {
        fun of(sequenceNumber: Int, chatRoomId: String): ChatRoomSequenceNumber {
            return ChatRoomSequenceNumber(sequenceNumber, chatRoomId)
        }
    }
}