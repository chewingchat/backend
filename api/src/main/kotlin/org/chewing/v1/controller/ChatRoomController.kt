//package org.chewing.v1.controller
//
//import org.chewing.v1.dto.request.chat.*
//import org.chewing.v1.dto.response.chat.ChatRoomResponse
//import org.chewing.v1.model.chat.ChatLog
//import org.chewing.v1.model.media.FileData
//import org.chewing.v1.model.media.MediaType
//import org.chewing.v1.response.HttpResponse
//import org.chewing.v1.response.SuccessOnlyResponse
//import org.chewing.v1.service.ChatRoomService
//import org.chewing.v1.util.ResponseHelper
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.*
//import java.io.File
//import java.io.FileInputStream
//import java.io.IOException
//
//@RestController
//@RequestMapping("/api/chatRooms")
//class ChatRoomController(
//    private val chatRoomService: ChatRoomService,
//) {
//
//    // 채팅방 목록 가져오기
//    @PostMapping("/list")
//    fun getChatRooms(
//        @RequestBody request: ChatRoomRequest
//    ): ResponseEntity<HttpResponse<List<ChatRoomResponse>>> {
//        val chatRooms = chatRoomService.getChatRooms(request.sort)
//        return ResponseHelper.success(chatRooms.map { ChatRoomResponse.from(it) })
//    }
//
//    // 채팅방 검색
//    @PostMapping("/search")
//    fun searchChatRooms(
//        @RequestBody request: ChatRoomSearchRequest
//    ): ResponseEntity<HttpResponse<List<ChatRoomResponse>>> {
//        val chatRooms = chatRoomService.searchChatRooms(request.keyword)
//        return ResponseHelper.success(chatRooms.map { ChatRoomResponse.from(it) })
//    }
//
//    // 채팅방 삭제
//    @PostMapping("/delete")
//    fun deleteChatRooms(
//        @RequestBody request: ChatRoomDeleteRequest
//    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
//        chatRoomService.deleteChatRooms(request.chatRoomIds)
//        return ResponseHelper.successOnly()
//    }
//
//
//    // 채팅방 접속 후 정보 가져오기
//    @GetMapping("/{chatRoomId}")
//    fun getChatRoomInfo(
//        @PathVariable chatRoomId: String
//    ): ResponseEntity<HttpResponse<ChatRoomResponse>> {
//        val chatRoom = chatRoomService.getChatRoomInfo(chatRoomId)
//        return ResponseHelper.success(ChatRoomResponse.from(chatRoom))
//    }
//
//
//    // 채팅방 로그 가져오기
//    @PostMapping("/log")
//    fun getChatLogs(
//        @RequestBody request: ChatLogRequest
//    ): ResponseEntity<HttpResponse<List<ChatLog>>> {
//        val chatLogs = chatRoomService.getChatLogs(request.chatRoomId, request.page)
//        return ResponseHelper.success(chatLogs)
//    }
//
//    // 파일 업로드
//    @PostMapping("/file/upload")
//    fun uploadFiles(
//        @RequestBody request: FileUploadRequest
//    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
//        val fileDataList = request.files.map { filePath ->
//            // 파일 경로를 FileData로 직접 변환
//            convertToFileData(filePath)
//        }
//
//        // 변환된 FileData 리스트를 서비스로 넘김
//        chatRoomService.uploadChatRoomFiles(request.chatRoomId, fileDataList)
//
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
//}