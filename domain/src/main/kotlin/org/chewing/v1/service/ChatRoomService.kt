//package org.chewing.v1.service
//
//import org.chewing.v1.implementation.media.FileHandler
//import org.chewing.v1.model.chat.ChatLog
//import org.chewing.v1.model.chat.ChatRoom
//import org.chewing.v1.model.media.FileCategory
//import org.chewing.v1.model.media.FileData
//import org.chewing.v1.model.media.Media
//import org.chewing.v1.repository.ChatRoomRepository
//import org.springframework.stereotype.Service
//import java.time.LocalDateTime
//
//@Service
//class ChatRoomService(
//    private val chatRoomRepository: ChatRoomRepository,
//    private val fileHandler: FileHandler
//
//) {
//    fun getChatRooms(sort: String): List<ChatRoom> {
//        return chatRoomRepository.readChatRooms(sort)
//    }
//
////    fun searchChatRooms(keyword: String): List<ChatRoom> {
////        return chatRoomRepository.searchChatRooms(keyword)
////    }
//
//    fun deleteChatRooms(chatRoomIds: List<String>) {
//        chatRoomRepository.deleteChatRooms(chatRoomIds)
//    }
//
//    fun getChatRoom(chatRoomId: String): ChatRoom {
//        return chatRoomRepository.readChatRoom(chatRoomId)
//    }
//
//    fun getChatLogs(chatRoomId: String, page: Int): List<ChatLog> {
//        return chatRoomRepository.getChatLogs(chatRoomId, page)
//    }
//
//    fun uploadChatRoomFiles(chatRoomId: String, fileDathList: List<FileData>) {
//        if (fileDathList.isEmpty()) {
//            throw IllegalArgumentException("파일이 존재하지 않습니다.")
//        }
//        val uploadedMedia: List<Media> = fileHandler.handleNewFiles("userId", fileDathList, FileCategory.EMOTICON)
//
//        chatRoomRepository.saveUploadedMedia(chatRoomId, uploadedMedia)
//    }
//
//    // 채팅방이 없을 경우 생성
//    fun getOrCreateChatRoom(roomId: String, userId: String): ChatRoom {
//        val chatRoom = chatRoomRepository.findByChatRoomId(roomId)
//        return chatRoom ?: createChatRoom(roomId, userId)
//    }
//
//    // 채팅방 생성 로직
//    private fun createChatRoom(roomId: String, userId: String): ChatRoom {
//        val newChatRoom = ChatRoom(
//            chatRoomId = roomId,
//            favorite = false,
//            groupChatRoom = false, // 기본적으로 단체 채팅이 아닌 것으로 설정
//            latestMessage = "New chat room created",
//            latestMessageTime = LocalDateTime.now().toString(),
//            totalUnReadMessage = 0,
//            chatFriendInfos = listOf(), // 채팅방 생성 시 친구 목록 비어 있음
//            0,
//            1
//        )
//        chatRoomRepository.save(newChatRoom)
//        return newChatRoom
//    }
//
//
//}
//
//
