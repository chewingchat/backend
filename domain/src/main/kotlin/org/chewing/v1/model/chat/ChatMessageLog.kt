package org.chewing.v1.model.chat


data class ChatMessageLog(
    val page: Int,
    val roomId: String,
    val messageList: List<ChatMessage>,
    var friends: List<ChatMessage.FriendSeqInfo>? // friends 필드 추가
) {
    companion object {
        fun generate(page: Int, roomId: String, messageList: List<ChatMessage>, friends: List<ChatMessage.FriendSeqInfo>): ChatMessageLog {
            return ChatMessageLog(page, roomId, messageList, friends)
        }
    }
}