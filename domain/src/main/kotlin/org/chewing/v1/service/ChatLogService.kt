package org.chewing.v1.service

import org.chewing.v1.implementation.chat.message.ChatReader
import org.chewing.v1.implementation.chat.sequence.ChatFinder
import org.chewing.v1.model.chat.message.ChatMessage
import org.springframework.stereotype.Service

@Service
class ChatLogService(
    private val chatReader: ChatReader,
    private val chatFinder: ChatFinder,
) {
    fun getChatLog(chatRoomId: String, page: Int): List<ChatMessage> {
        return chatReader.readChatLog(chatRoomId, page)
    }

    fun getChatLogLatest(chatRoomId: String): List<ChatMessage> {
        val page = chatFinder.findLastPage(chatRoomId)
        return chatReader.readChatLog(chatRoomId, page)
    }
}