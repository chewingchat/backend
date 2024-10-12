package org.chewing.v1.repository

import org.chewing.v1.model.chat.log.ChatLog1
import org.chewing.v1.model.chat.room.ChatRoomNumber
import org.chewing.v1.model.media.Media

interface ChatLogRepository {
    //    fun appendChatMessage(chatMessage: ChatMessage, page: Int)
    fun readChatMessages(roomId: String, page: Int): List<ChatLog1>
    fun deleteMessage(roomId: String, messageId: String)  // 메시지 삭제 기능 추가

    fun appendChatFileLog(medias: List<Media>, roomId: String, senderId: String, number: ChatRoomNumber): ChatLog1

//    // 친구의 마지막으로 읽은 메시지 시퀀스를 조회
//    fun findFriendLastSeqNumber(roomId: String, friendId: Int): Int
}