import org.chewing.v1.dto.request.chat.message.ChatReadDto
import org.chewing.v1.model.chat.message.ChatCommonMessage
import org.chewing.v1.model.chat.message.ChatDeleteMessage
import org.chewing.v1.model.chat.message.ChatReplyMessage
import org.chewing.v1.service.ChatService
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

//package org.chewing.v1.controller
//
//
//import org.chewing.v1.dto.request.chat.FileUploadRequest
//import org.chewing.v1.dto.response.auth.LogInfoResponse
//import org.chewing.v1.dto.response.auth.TokenResponse
//import org.chewing.v1.dto.response.chat.ChatDto
//import org.chewing.v1.model.auth.JwtToken
//import org.chewing.v1.model.media.FileData
//import org.chewing.v1.model.media.MediaType
//import org.chewing.v1.response.HttpResponse
//import org.chewing.v1.response.SuccessOnlyResponse
//import org.chewing.v1.service.AuthService
//import org.chewing.v1.service.ChatService
//import org.chewing.v1.util.FileUtil
//import org.chewing.v1.util.ResponseHelper
//import org.springframework.http.ResponseEntity
//import org.springframework.messaging.handler.annotation.MessageMapping
//import org.springframework.stereotype.Controller
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestHeader
//import org.springframework.web.multipart.MultipartFile
//import java.io.File
//import java.io.FileInputStream
//import java.io.IOException
//
@Controller
class ChatStompController(
    private val chatService: ChatService,
) {
    @MessageMapping("/chat/pub/read")
    fun readMessage(
        message: ChatReadDto,
        @Header("simpSessionAttributes") sessionAttributes: Map<String, Any>
    ) {
        val userId = sessionAttributes["userId"] as String
        chatService.processRead(message.chatRoomId, userId)
    }

    @MessageMapping("/chat/pub/delete")
    fun deleteMessage(
        message: ChatDeleteMessage,
        @Header("simpSessionAttributes") sessionAttributes: Map<String, Any>
    ) {
        val userId = sessionAttributes["userId"] as String
        chatService.processDelete(message.chatRoomId, userId, message.messageId)
    }

    @MessageMapping("/chat/pub/reply")
    fun replyMessage(
        message: ChatReplyMessage,
        @Header("simpSessionAttributes") sessionAttributes: Map<String, Any>
    ) {
        val userId = sessionAttributes["userId"] as String
        chatService.processReply(message.chatRoomId, userId, message.parentMessageId, message.text)
    }

    @MessageMapping("/chat/pub/chat")
    fun chatMessage(
        message: ChatCommonMessage,
        @Header("simpSessionAttributes") sessionAttributes: Map<String, Any>
    ) {
        val userId = sessionAttributes["userId"] as String
        chatService.processChat(message.chatRoomId, userId, message.text)
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
//
//
//}
//
//
//
//
//
