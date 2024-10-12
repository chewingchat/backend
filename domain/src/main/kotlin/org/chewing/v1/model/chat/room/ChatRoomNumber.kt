package org.chewing.v1.model.chat.room

class ChatRoomNumber private constructor(
    val sequenceNumber: Int,
    val roomId: String,
    val page: Int
) {
    companion object {
        fun of(
            chatRoomSequenceNumber: ChatRoomSequenceNumber,
            page: Int
        ): ChatRoomNumber {
            return ChatRoomNumber(
                sequenceNumber = chatRoomSequenceNumber.sequenceNumber,
                roomId = chatRoomSequenceNumber.roomId,
                page = page
            )
        }
    }
}