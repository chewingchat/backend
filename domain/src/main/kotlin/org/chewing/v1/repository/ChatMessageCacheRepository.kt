package org.chewing.v1.repository

import org.chewing.v1.model.ChatMessageLog


interface ChatMessageCacheRepository {
    fun appendChatMessageLog(chatMessageLog: ChatMessageLog)
    fun readChatMessageLog(roomId: String, page: Int): ChatMessageLog?
}