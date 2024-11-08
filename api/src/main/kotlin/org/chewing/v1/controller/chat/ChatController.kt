package org.chewing.v1.controller.chat

import org.chewing.v1.dto.request.chat.message.*
import org.chewing.v1.facade.ChatFacade
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.util.FileUtil
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@Controller
class ChatController(
    private val chatFacade: ChatFacade
) {
    @MessageMapping("/chat/read")
    fun readMessage(
        message: ChatReadDto,
        principal: Principal
    ) {
        val userId = principal.name
        chatFacade.processRead(message.chatRoomId, userId)
    }

    @MessageMapping("/chat/delete")
    fun deleteMessage(
        message: ChatDeleteDto,
        principal: Principal
    ) {
        val userId = principal.name
        chatFacade.processDelete(message.chatRoomId, userId, message.messageId)
    }

    @MessageMapping("/chat/reply")
    fun replyMessage(
        message: ChatReplyDto,
        principal: Principal
    ) {
        val userId = principal.name
        chatFacade.processReply(message.chatRoomId, userId, message.parentMessageId, message.message)
    }

    @MessageMapping("/chat/bomb")
    fun bombMessage(
        message: ChatBombDto,
        principal: Principal
    ) {
        val userId = principal.name
        chatFacade.processBombing(message.chatRoomId, userId, message.message, message.toExpireAt())
    }

    @MessageMapping("/chat/common")
    fun chatMessage(
        message: ChatCommonDto,
        principal: Principal
    ) {
        val userId = principal.name
        chatFacade.processCommon(message.chatRoomId, userId, message.message)
    }

    @PostMapping("/api/chat/file/upload")
    fun uploadFiles(
        @RequestPart("files") files: List<MultipartFile>,
        @RequestAttribute("userId") userId: String,
        @RequestParam("chatRoomId") chatRoomId: String
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val convertFiles = FileUtil.convertMultipartFileToFileDataList(files)
        chatFacade.processFiles(convertFiles, userId, chatRoomId)
        // 생성 완료 응답 201 반환
        return ResponseHelper.successCreateOnly()
    }
}
