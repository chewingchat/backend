package org.chewing.v1.controller.chat

import org.chewing.v1.dto.request.chat.message.*
import org.chewing.v1.facade.ChatFacade
import org.chewing.v1.model.chat.message.ChatBombMessage
import org.chewing.v1.model.chat.message.ChatNormalMessage
import org.chewing.v1.model.chat.message.ChatDeleteMessage
import org.chewing.v1.model.chat.message.ChatReplyMessage
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.util.FileUtil
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
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
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }
}

//
//    @MessageMapping("/chat/pub")
//    fun sendMessage(message: ChatDto) {
//        chatService.saveAndSendChat(message.toChat())
//    }
//
//
//
//    // 1.채팅방 입장(읽음 처리용)
//    @MessageMapping("/chat/pub/in")
//    fun enterChatRoom(message: ChatDto) {
//        chatService.enterChatRoom(message.chatRoomId!!, message.sender)
//    }
//
//    // 2. 채팅방 메시지 보내기 (파일 포함)
//    @PostMapping("/send/file")
//    fun sendFileMessage(
//        @RequestBody request: FileUploadRequest
//    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
//        // 파일 경로를 FileData로 직접 변환
//        val fileDataList = request.files.map { filePath ->
//            convertToFileData(filePath)
//        }
//
//        // 파일 메시지 전송 로직을 서비스로 넘김
//        chatService.sendFileMessage(request.chatRoomId, fileDataList)
//        return ResponseHelper.successOnly()
//    }
//
//    // 파일 경로에서 FileData로 변환하는 함수
//    private fun convertToFileData(filePath: String): FileData {
//        try {
//            val file = File(filePath)
//            val inputStream = FileInputStream(file)
//            val contentType = "application/octet-stream" // 필요시 적절한 MIME 타입으로 변경 가능
//
//            return FileData.of(inputStream, MediaType.fromType(contentType)!!, file.name, file.length())
//        } catch (e: IOException) {
//            throw IllegalArgumentException("파일 경로에서 FileData로 변환하는데 실패했습니다: ${e.message}")
//        }
//    }
//
//
//    // 3.메시지 삭제 (type: DELETE)
//    @MessageMapping("/chat/pub/delete")
//    fun deleteMessage(message: ChatDto) {
//        chatService.deleteChatMessage(message.chatRoomId!!, message.messageId!!)
//    }
//
//    // 4.대댓글 (type: REPLY)
//    @MessageMapping("/chat/pub/reply")
//    fun replyMessage(message: ChatDto) {
//        chatService.sendReplyMessage(message.toChat())
//    }
//
//    // 5.채팅방 나가기(읽음 처리용)
//    @MessageMapping("/chat/pub/out")
//    fun leaveChatRoom(message: ChatDto) {
//        chatService.leaveChatRoom(message.chatRoomId!!, message.sender)
//
//    }
//
//
//    // 채팅방에서 입장 메시지 받기 (읽음 처리용)
//    @MessageMapping("/chat/sub/in")
//    fun receiveEnterMessage(message: ChatDto) {
//        // 채팅방 입장 메시지를 처리하고 몽고DB에 읽음 처리 기록
//        chatService.processEnterMessage(message.toChat())
//    }
//
//    // 단체 채팅방에서 입장 메시지 받기
//    @MessageMapping("/chat/sub/enter")
//    fun receiveEnterGroupMessage(message: ChatDto) {
//        // 단체 채팅방에 사용자 입장 메시지를 처리
//        chatService.processGroupEnterMessage(message.toChat())
//    }
//
//    // 채팅방 밖에서 채팅 메시지 받기
//    @MessageMapping("/chat/sub/chat/outside")
//    fun receiveChatOutsideMessage(message: ChatDto) {
//        // 채팅방 외부에서 채팅 메시지를 처리
//        chatService.processChatOutsideMessage(message.toChat())
//    }
//
//    // 채팅방 밖에서 파일 메시지 받기
//    @MessageMapping("/chat/sub/file/outside")
//    fun receiveFileOutsideMessage(message: ChatDto) {
//        // 채팅방 외부에서 파일 메시지를 처리
//        chatService.processFileOutsideMessage(message.toChat())
//    }
//    // 엑세스 토큰 만료 시 리프레시 토큰을 사용해 새로운 토큰 발급
//    @GetMapping("/refresh")
//    fun refreshToken(@RequestHeader("Authorization") refreshToken: String): ResponseEntity<HttpResponse<TokenResponse>> {
//        val newToken: JwtToken = authService.refreshJwtToken(refreshToken)
//        return ResponseHelper.success(TokenResponse.of(newToken))
//    }
//}
//
//
//
//
//
