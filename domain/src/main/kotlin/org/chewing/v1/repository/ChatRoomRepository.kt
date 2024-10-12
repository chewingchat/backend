package org.chewing.v1.repository

import org.chewing.v1.model.chat.room.ChatRoomInfo
import org.chewing.v1.model.chat.room.ChatRoomSortCriteria

interface ChatRoomRepository {

    // 채팅방 목록 가져오기
//    fun readChatRooms(userId:String , sort: ChatRoomSortCriteria): List<ChatRoomInfo>

    // 채팅방 검색
//    fun searchChatRooms(keyword: String): List<ChatRoom>

    // 채팅방 삭제
//    fun deleteChatRooms(chatRoomIds: List<String>)

    // 채팅방 접속 후 정보 가져오기
    fun readChatRoom(chatRoomId: String): ChatRoomInfo
    // 채팅 로그 가져오기
//    fun getChatLogs(chatRoomId: String, page: Int): List<ChatLog>

    // 채팅방 파일 업로드
    // 채팅방 파일 업로드
//    fun saveUploadedMedia(chatRoomId: String, uploadedMedia: List<Media>)

    fun findByChatRoomId(roomId: String): ChatRoomInfo

    fun appendChatRoom(isGroup: Boolean): String

    fun readChatRooms(roomIds: List<String>): List<ChatRoomInfo>
}