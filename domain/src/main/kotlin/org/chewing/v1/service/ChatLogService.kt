package org.chewing.v1.service

import org.chewing.v1.implementation.chat.log.ChatLogReader
import org.chewing.v1.implementation.chat.sequence.ChatHelper
import org.chewing.v1.model.chat.log.ChatLog1
import org.springframework.stereotype.Service

@Service
class ChatLogService(
    private val chatLogReader: ChatLogReader,
    private val chatHelper: ChatHelper,
) {

    fun getChatLog(roomId: String, page: Int): List<ChatLog1> {
        return chatLogReader.readChatLog(roomId, page)
    }

    fun getChatLogLatest(roomId: String): List<ChatLog1> {
        val page = chatHelper.readLastPage(roomId)
        return chatLogReader.readChatLog(roomId, page)
    }
}