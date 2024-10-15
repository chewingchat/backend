package org.chewing.v1.repository

import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.model.chat.room.ChatNumber

interface ChatLogRepository {
    //    fun appendChatMessage(chatMessage: ChatMessage, page: Int)
    fun readChatMessages(chatRoomId: String, page: Int): List<ChatMessage>
    fun removeMessage(messageId: String)  // 메시지 삭제 기능 추가
    fun appendChatLog(chatMessage: ChatMessage)
    fun readChatMessage(messageId: String): ChatMessage?
//    // 친구의 마지막으로 읽은 메시지 시퀀스를 조회
//    fun findFriendLastSeqNumber(chatRoomId: String, friendId: Int): Int
    fun readLatestMessages(numbers: List<ChatNumber>): List<ChatMessage>
}