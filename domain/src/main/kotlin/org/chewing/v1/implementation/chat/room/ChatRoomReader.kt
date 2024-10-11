//package org.chewing.v1.implementation.chat.room
//
//import org.chewing.v1.model.chat.ChatRoom
//import org.chewing.v1.model.chat.ChatRoomInfo
//import org.chewing.v1.repository.ChatRoomRepository
//import org.springframework.stereotype.Component
//
//@Component
//class ChatRoomReader(
//    private val chatRoomRepository: ChatRoomRepository,
//) {
//    fun readChatRooms(sort: String): List<ChatRoom> {
//        return chatRoomRepository.readChatRooms(sort)
//    }
//
//    fun readChatRoom(roomId: String): ChatRoomInfo {
//        return chatRoomRepository.readChatRoom(roomId)
//    }
//}