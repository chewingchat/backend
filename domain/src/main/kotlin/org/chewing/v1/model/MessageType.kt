package org.chewing.v1.model

enum class MessageType {
    REPLY, DELETE, LEAVE, CHAT, FILE, IN, OUT
}
//답글 채팅(REPLY)
//삭제된 채팅(DELETE)
//단체 채팅방일 경우 친구가 나갔음을 알리는 채팅(LEAVE)
// 기본적인 텍스트 형태의 채팅(CHAT)
//파일 형태의 채팅(FILE)