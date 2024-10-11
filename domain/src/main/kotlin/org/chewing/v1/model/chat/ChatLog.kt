package org.chewing.v1.model.chat

import org.chewing.v1.model.media.MediaType

data class ChatLog(
    val type: String, // api 0.2 v로 인한 수정, 메세지 타입 의미(파일, 텍스트 등등)
    val messageType: List<MessageType>? = null, // api 0.2 v로 인한 수정(삭제, 나감 등 메시지 상태여부)
    val roomId: String? = null,
    val messageId: String,
    val senderId: String,
    val message: String,
    val sendTime: String, // api 0.2 v로 인한 수정
    val seqNumber: Int?, // api 0.2 v로 인한 수정
    val parentMessageId: String? = null, // 부모 메시지 ID 추가
    val parentMessage: String? = null, // 부모 메시지 내용 추가
    val parentMessagePage: Int? = null, // 부모 메시지 페이지 번호 추가
    val parentSeqNumber: Int? = null, // api 0.2 v로 인한 수정
    val page: Int? = null,
    val chatMessages: List<ChatMessage>? = null, // ChatMessage 리스트 추가
    var friends: List<ChatMessage.FriendSeqInfo>? = null // friends 필드 추가
) {
    companion object {
        fun generate(
            page: Int,
            roomId: String,
            messageList: List<ChatMessage>,
            friends: List<ChatMessage.FriendSeqInfo>
        ): ChatLog {
            return ChatLog(
                type = "", // 임의의 기본 타입 설정
                messageId = "", // 기본값으로 설정
                senderId = "",
                message = "",
                sendTime = "",
                seqNumber = null,
                parentMessageId = null,
                parentMessage = null,
                parentMessagePage = null,
                parentSeqNumber = null,
                page = page,
                chatMessages = messageList, // 기존 ChatMessage 리스트를 통합
                friends = friends
            )
        }
    }
}