package org.chewing.v1.implementation.chat.sequence

import org.chewing.v1.repository.ChatMessageRepository
import org.chewing.v1.repository.ChatSequenceRepository
import org.springframework.stereotype.Component

@Component
class ChatSequenceReader(
    private val chatSequenceRepository: ChatSequenceRepository,
    private val chatMessageRepository: ChatMessageRepository
) {

    // 친구가 마지막으로 읽은 메시지 시퀀스를 DB에서 읽음
    fun readFriendLastSeqNumber(roomId: String, friendId: Int): Int {
        return chatMessageRepository.findFriendLastSeqNumber(roomId, friendId)
    }

    // 채팅방에 새로 입장할 때 시퀀스 저장
    fun saveChatSequence(roomId: String, userId: String, seqNumber: Long) {
        chatSequenceRepository.saveSequence(roomId, userId, seqNumber)
    }
}