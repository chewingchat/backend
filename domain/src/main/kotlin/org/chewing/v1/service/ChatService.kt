package org.chewing.v1.service

import org.chewing.v1.implementation.chat.ChatLogAppender
import org.chewing.v1.implementation.chat.ChatLogReader
import org.chewing.v1.implementation.chat.ChatSender
import org.chewing.v1.implementation.chat.ChatSequenceUpdater
import org.chewing.v1.model.ChatMessage
import org.chewing.v1.model.ChatMessageLog
import org.chewing.v1.model.MessageType
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatLogAppender: ChatLogAppender,
    private val chatLogReader: ChatLogReader,
    private val chatSequenceUpdater: ChatSequenceUpdater,
    private val chatSender: ChatSender
) {

    fun saveAndSendChat(chatMessage: ChatMessage) {
        val seq = chatSequenceUpdater.updateSequenceIncrement(chatMessage.roomId!!)
        val page = calculatePage(seq)
        chatLogAppender.appendChatMessage(chatMessage, page)
        chatSender.sendChat(chatMessage)
    }

    fun enterChatRoom(roomId: String, userId: String) {
        val seq = chatSequenceUpdater.updateSequenceIncrement(roomId)
        val page = calculatePage(seq)
        // 새로운 IN 메시지에 친구 목록과 그들의 시퀀스 정보 추가
        val friends = getFriendsWithSeqNumber(roomId) // 친구 목록과 시퀀스 정보 가져오는 함수
        chatLogAppender.appendChatMessage(
            ChatMessage.withoutId(MessageType.IN, roomId, userId, null, null, null, null, null, null),
            page
        )
        val enterMessage = ChatMessage.withoutId(MessageType.IN, roomId, userId, "User $userId has entered the chat", null, null, null, null, null)
        enterMessage.friends = friends // friends 추가
        chatSender.sendChat(enterMessage)
    }

    fun sendFileMessage(chatMessage: ChatMessage) {
        val seq = chatSequenceUpdater.updateSequenceIncrement(chatMessage.roomId!!)
        val page = calculatePage(seq)
        chatLogAppender.appendChatMessage(chatMessage, page)
        chatSender.sendChat(chatMessage)
    }

    fun deleteChatMessage(roomId: String, messageId: String) {
        chatLogAppender.deleteMessage(roomId, messageId)
        val deleteMessage = ChatMessage.withoutId(MessageType.DELETE, roomId, "system", null, null, messageId, null, null, null)
        chatSender.sendChat(deleteMessage)
    }

    fun sendReplyMessage(chatMessage: ChatMessage) {
        val seq = chatSequenceUpdater.updateSequenceIncrement(chatMessage.roomId!!)
        val page = calculatePage(seq)
        chatLogAppender.appendChatMessage(chatMessage, page)
        // REPLY 메시지에 parentMessageId, parentMessagePage, parentMessageSeqNumber 포함
        chatSender.sendChat(chatMessage)
    }

    fun leaveChatRoom(roomId: String, userId: String) {
        val leaveMessage = ChatMessage.withoutId(MessageType.LEAVE, roomId, userId, null, null, null, null, null, null)
        chatSender.sendChat(leaveMessage)
    }


    fun getChatLog(roomId: String, page: Int): ChatMessageLog {
        return chatLogReader.readChatLog(roomId, page)
    }

    fun getChatLogLatest(roomId: String): ChatMessageLog {
        val seq = chatSequenceUpdater.updateSequenceIncrement(roomId)
        return chatLogReader.readChatLog(roomId, calculatePage(seq))
    }

    private fun calculatePage(seq: Long): Int {
        return (seq / PAGE_SIZE).toInt()
    }
    // ChatMessage.FriendSeqInfo 사용으로 변경
    fun getFriendsWithSeqNumber(roomId: String): List<ChatMessage.FriendSeqInfo> {
        // roomId를 기준으로 친구 목록과 해당 친구가 읽은 마지막 메시지 시퀀스 정보를 가져오는 로직
        return listOf(
            ChatMessage.FriendSeqInfo(friendId = "sampleId", friendSeqNumber = 501),
            ChatMessage.FriendSeqInfo(friendId = "sampleId2", friendSeqNumber = 501)
        )
    }

    companion object {
        const val PAGE_SIZE = 50
    }
}



