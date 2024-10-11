package org.chewing.v1.repository


interface ChatSequenceRepository {

    // 채팅방 시퀀스 저장
    fun saveSequence(roomId: String, userId: String, seqNumber: Long)

    // 채팅방의 시퀀스 정보를 조회하는 메서드
    fun readCurrentSequence(roomId: String): Long

    // 시퀀스 증가 로직
    fun updateSequenceIncrement(roomId: String): Long


}