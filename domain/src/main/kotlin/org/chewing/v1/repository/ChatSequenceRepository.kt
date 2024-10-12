package org.chewing.v1.repository

import org.chewing.v1.model.chat.room.ChatRoomSequenceNumber


interface ChatSequenceRepository {
    // 채팅방의 시퀀스 정보를 조회하는 메서드
    fun readCurrent(roomId: String): ChatRoomSequenceNumber

    // 시퀀스 증가 로직 -> 없다면 생성
    fun updateSequenceIncrement(roomId: String): ChatRoomSequenceNumber
    fun readSeqNumbers(roomIds: List<String>): List<ChatRoomSequenceNumber>
}