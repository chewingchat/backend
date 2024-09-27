package org.chewing.v1.controller


import org.chewing.v1.dto.ChatDto
import org.chewing.v1.model.MessageType
import org.chewing.v1.service.ChatService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class ChatController(private val chatService: ChatService) {

    @MessageMapping("/chat/pub")
    fun sendMessage(message: ChatDto) {
        chatService.saveAndSendChat(message.toChat())
    }

    // 파일 메시지 전송 (type: FILE)
    @MessageMapping("/chat/file")
    fun sendFileMessage(message: ChatDto) {
        chatService.sendFileMessage(message.toChat())
    }

    // 채팅방 입장(읽음 처리용)
    @MessageMapping("/chat/pub/in")
    fun enterChatRoom(message: ChatDto) {
        chatService.enterChatRoom(message.roomId!!, message.sender)
    }

    // 메시지 삭제 (type: DELETE)
    @MessageMapping("/chat/pub/delete")
    fun deleteMessage(message: ChatDto) {
        chatService.deleteChatMessage(message.roomId!!, message.messageId!!)
    }

    // 대댓글 (type: REPLY)
    @MessageMapping("/chat/pub/reply")
    fun replyMessage(message: ChatDto) {
        chatService.sendReplyMessage(message.toChat())
    }

    @MessageMapping("/chat/pub/out")
    fun leaveChatRoom(message: ChatDto) {
        chatService.leaveChatRoom(message.roomId!!, message.sender)
    }

}





