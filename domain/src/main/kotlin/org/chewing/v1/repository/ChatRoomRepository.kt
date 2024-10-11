package org.chewing.v1.repository

import org.chewing.v1.model.chat.ChatLog
import org.chewing.v1.model.chat.ChatRoom
import org.chewing.v1.model.chat.ChatRoomInfo
import org.chewing.v1.model.media.Media

interface ChatRoomRepository {

    // 채팅방 목록 가져오기
    fun readChatRooms(sort: String): List<ChatRoom>

    // 채팅방 검색
//    fun searchChatRooms(keyword: String): List<ChatRoom>

    // 채팅방 삭제
    fun deleteChatRooms(chatRoomIds: List<String>)

    // 채팅방 접속 후 정보 가져오기
    fun readChatRoom(chatRoomId: String): ChatRoomInfo

    // 채팅 로그 가져오기
    fun getChatLogs(chatRoomId: String, page: Int): List<ChatLog>

    // 채팅방 파일 업로드
    // 채팅방 파일 업로드
    fun saveUploadedMedia(chatRoomId: String, uploadedMedia: List<Media>)

    fun findByChatRoomId(roomId: String): ChatRoomInfo

    fun save(chatRoom: ChatRoom)


}