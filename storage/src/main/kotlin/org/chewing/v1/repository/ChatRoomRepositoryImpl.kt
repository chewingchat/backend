package org.chewing.v1.repository


import org.chewing.v1.model.chat.*
import org.chewing.v1.repository.ChatRoomRepository
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class ChatRoomRepositoryImpl : ChatRoomRepository {

    override fun findChatRooms(sort: String?): List<ChatRoomResponse> {
        // 채팅방 정렬에 따라 목록 조회 로직 추가
        return listOf() // Mocked data for simplicity
    }

    override fun searchChatRooms(keyword: String): List<ChatRoomResponse> {
        // 키워드를 이용한 채팅방 검색 로직 추가
        return listOf() // Mocked data for simplicity
    }

    override fun deleteChatRooms(chatRoomIds: List<String>) {
        // 채팅방 ID 리스트를 받아서 삭제 처리 로직 추가
    }

    override fun findChatRoomInfo(chatRoomId: String): ChatRoomResponse {
        // 특정 채팅방 정보를 조회하는 로직 추가
        return ChatRoomResponse(
            chatRoomId = "sampleId",
            isFavorite = true,
            isGroupChatRoom = true,
            latestMessage = "Hello",
            latestMessageTime = "2024-09-20 14:00:00",
            totalUnreadMessages = 5,
            friends = listOf(
                FriendChatResponse(
                    friendId = "sampleId1",
                    friendFirstName = "John",
                    friendLastName = "Doe",
                    friendImageUrl = "http://aws.example.com/sample1.png",
                ),
                FriendChatResponse(
                    friendId = "sampleId2",
                    friendFirstName = "Jane",
                    friendLastName = "Doe",
                    friendImageUrl = "http://aws.example.com/sample2.png",

                )
            )
        )
    }

    override fun findChatLogs(chatRoomId: String, page: Int): ChatLogResponse {
        // 채팅방의 특정 페이지의 채팅 로그를 조회하는 로직 추가
        return ChatLogResponse(
            page = page,
            messages = listOf(
                MessageResponse(
                    type = "CHAT",
                    messageId = "msg1",
                    senderId = "user1",
                    message = "Hello",
                    messageSendTime = "2023-09-20 14:00:00",
                    messageSeqNumber = 501,
                    parentMessageId = null,
                    parentMessage = null,
                    parentMessagePage = null,
                    parentMessageSeqNumber = null
                )
            )
        )
    }

    override fun uploadFiles(chatRoomId: String, files: List<File>): FileUploadResponse {
        // 파일 업로드 처리 로직 추가
        return FileUploadResponse(message = "파일 업로드 성공")
    }
}