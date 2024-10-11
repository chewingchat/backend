package org.chewing.v1.repository

import org.chewing.v1.model.chat.ChatLog
import org.chewing.v1.model.chat.ChatMessage

interface ChatMessageRepository {
    fun appendChatMessage(chatMessage: ChatMessage, page: Int)
    fun readChatMessages(roomId: String, page: Int): List<ChatMessage>
    fun deleteMessage(roomId: String, messageId: String)  // 메시지 삭제 기능 추가

    // 친구의 마지막으로 읽은 메시지 시퀀스를 조회
    fun findFriendLastSeqNumber(roomId: String, friendId: Int): Int
}