//package org.chewing.v1.dto.response.chat
//
//import org.chewing.v1.model.chat.ChatMessage
//import org.chewing.v1.model.chat.MessageType
//import org.chewing.v1.model.chat.MessageSendType
//import java.time.LocalDateTime
//
//data class ChatDto(
//    val type: MessageType,
//    val roomId: String?,
//    val sender: String,
//    val messageSendType: MessageSendType?,  // messageSendType 필드 수정
//    val timestamp: LocalDateTime?,
//    val messageId: String?,
//    val parentMessageId: String?,  // 부모 메시지 ID 추가
//    val parentMessagePage: Int?,   // 부모 메시지 페이지
//    val parentSeqNumber: Int?, // 부모 메시지 시퀀스 번호 추가
//    val friends: List<ChatMessage.FriendSeqInfo>? = null // 친구 목록 및 읽음 상태 추가
//) {
//    companion object {
//        // ChatMessage를 ChatDto로 변환하는 메서드
//        fun from(chatMessage: ChatMessage) = ChatDto(
//            chatMessage.type,
//            chatMessage.roomId,
//            chatMessage.sender,
//            chatMessage.messageSendType,  // 수정된 필드
//            chatMessage.timestamp,
//            chatMessage.messageId,
//            chatMessage.parentMessageId,
//            chatMessage.parentMessagePage,
//            chatMessage.parentSeqNumber,
//            chatMessage.friends
//        )
//    }
//
//    // ChatDto를 ChatMessage로 변환하는 메서드
//    fun toChat() = ChatMessage.withoutId(
//        type,
//        roomId!!,
//        sender,
//        messageSendType!!,  // 수정된 필드
//        parentMessageId,
//        parentMessagePage,
//        parentSeqNumber,
//        friends,
//        1, // 페이지 번호를 기본 값으로 설정 (필요에 따라 수정 가능)
//    )
//}