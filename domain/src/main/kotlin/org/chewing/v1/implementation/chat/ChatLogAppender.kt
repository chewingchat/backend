package org.chewing.v1.implementation.chat


import org.chewing.v1.model.chat.ChatMessage
import org.chewing.v1.repository.ChatMessageRepository
import org.chewing.v1.repository.ChatRoomRepository
import org.springframework.stereotype.Component

@Component
class ChatLogAppender(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatRoomRepository: ChatRoomRepository
) {

    fun appendChatMessage(chatMessage: ChatMessage, page: Int) {
        chatMessageRepository.appendChatMessage(chatMessage, page)
    }
    // 메시지 삭제
    fun deleteMessage(roomId: String, messageId: String) {
        // 레포지토리 레이어에 메시지 삭제 요청
        chatMessageRepository.deleteMessage(roomId, messageId)
    }
    // 채팅방에 있는 친구들의 읽은 메시지 시퀀스를 가져오는 메서드
    fun getChatFriends(roomId: String): List<ChatMessage.FriendSeqInfo> {
        // 채팅방 정보를 가져와서 참여자 정보와 읽은 시퀀스를 가져옴
        val chatRoom = chatRoomRepository.getChatRoomInfo(roomId)

        // 각 친구의 마지막 읽은 메시지 시퀀스를 가져와 FriendSeqInfo 리스트로 반환
        return chatRoom.chatFriends.map { friend ->
            val lastReadSeqNumber = chatMessageRepository.findFriendLastSeqNumber(roomId, friend.friendId)
            ChatMessage.FriendSeqInfo(friendId = friend.friendId, friendSeqNumber = lastReadSeqNumber)
        }
    }
}

