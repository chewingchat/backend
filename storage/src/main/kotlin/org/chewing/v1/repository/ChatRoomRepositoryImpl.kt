package org.chewing.v1.repository

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.chat.ChatFriend
import org.chewing.v1.model.chat.ChatLog
import org.chewing.v1.model.chat.ChatRoom
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.jparepository.ChatRoomJpaRepository
import org.chewing.v1.model.chat.*
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media
import org.chewing.v1.mongoentity.ChatMessageMongoEntity
import org.chewing.v1.mongorepository.ChatMessageMongoRepository
import org.chewing.v1.mongorepository.ChatSequenceMongoRepository
import org.springframework.stereotype.Repository
import java.io.IOException
import java.time.LocalDateTime
import java.util.*


@Repository
class ChatRoomRepositoryImpl(
    private val chatMessageMongoRepository: ChatMessageMongoRepository, // MongoDB 레포지토리 이름 변경
    private val chatRoomJpaRepository: ChatRoomJpaRepository, // JPA 레포지토리
    private val chatSequenceMongoRepository: ChatSequenceMongoRepository

) : ChatRoomRepository {
    private val chatRooms = mutableListOf<ChatRoom>()
    private val chatLogs = mutableMapOf<String, MutableList<ChatLog>>()
    override fun getChatRooms(sort: String): List<ChatRoom> {


        val sortedChatRooms = when (sort) {
            "favorite" -> chatRooms.filter { it.favorite }
            "latest" -> chatRooms.sortedByDescending { it.latestMessageTime }
            "notRead" -> chatRooms.filter { it.totalUnReadMessage > 0 }
            else -> throw IllegalArgumentException("Invalid sort parameter")
        }

        if (sortedChatRooms.isEmpty()) throw NotFoundException(ErrorCode.CHATROOM_NOT_FOUND)
        return sortedChatRooms
    }

    override fun searchChatRooms(keyword: String): List<ChatRoom> {
        // 1. JPA를 통해 기본 채팅방 정보 검색
        val chatRooms = chatRoomJpaRepository.searchByKeyword(keyword)

        if (chatRooms.isEmpty()) {
            throw NotFoundException(ErrorCode.CHATROOM_NOT_FOUND)
        }

        // 2. MongoDB를 통해 각 채팅방의 추가 정보 검색 (예: 최신 메시지)
        return chatRooms.map { chatRoom ->
            val latestMessage = chatMessageMongoRepository.findLatestMessageByRoomId(chatRoom.chatRoomId)
            val updatedChatRoom = chatRoom.copy(latestMessage = latestMessage?.messageId ?: chatRoom.latestMessage)
            updatedChatRoom
        }
    }

    override fun deleteChatRooms(chatRoomIds: List<String>) {
        chatRoomIds.forEach { chatRoomId ->
            val roomToDelete = chatRooms.find { it.chatRoomId == chatRoomId }
                ?: throw NotFoundException(ErrorCode.CHATROOM_NOT_FOUND)

            chatRooms.remove(roomToDelete)
        }
    }

    override fun getChatRoomInfo(chatRoomId: String): ChatRoom {
        val chatRoomEntity = chatRoomJpaRepository.findByChatRoomId(chatRoomId)
            .orElseThrow { NotFoundException(ErrorCode.CHATROOM_NOT_FOUND) }

        // 최신 페이지와 친구가 읽은 시퀀스 번호 계산 (MongoDB를 사용)
        val latestPage = chatMessageMongoRepository.calculateLatestPage(chatRoomId)
        val readSeqNumber = chatSequenceMongoRepository.calculateFriendReadSeqNumber(chatRoomId.toInt(), chatRoomEntity.toChatRoom().chatFriends)

        // MongoDB에서 가져온 데이터를 설정
        chatRoomEntity.latestPage = latestPage
        chatRoomEntity.readSeqNumber = readSeqNumber

        // ChatRoomEntity를 ChatRoom으로 변환 후 반환
        return chatRoomEntity.toChatRoom()
    }

    override fun getChatLogs(chatRoomId: String, page: Int): List<ChatLog> {
        val logs = chatLogs[chatRoomId]?.filter { it.page == page }
            ?: throw NotFoundException(ErrorCode.CHATLOG_NOT_FOUND)

        // 답글의 부모 메시지 정보 추가
        return logs.map { log ->
            if (log.type == "REPLY") {
                val parentMessage = findParentMessage(log.parentMessageId ?: "")
                log.copy(
                    parentMessage = parentMessage?.message,
                    parentMessagePage = parentMessage?.page,
                    seqNumber = parentMessage?.seqNumber
                )
            } else {
                log
            }
        }
    }

    // 채팅방에 파일 업로드
    override fun uploadChatRoomFiles(chatRoomId: String, fileDathList: List<FileData>) {
        try {


            // 파일 데이터들을 미디어로 변환하고 업로드합니다.
            val uploadedMedia: List<Media> = fileProcessor.processNewFiles("userId", fileDathList, FileCategory.EMOTICON)

            // 업로드된 미디어를 채팅방에 연결하는 로직
            saveUploadedMedia(chatRoomId, uploadedMedia)

        } catch (e: IOException) {
            throw IllegalArgumentException("파일 업로드에 실패했습니다: ${e.message}")
        }
    }

    // 업로드된 미디어를 저장하는 메서드
    override fun saveUploadedMedia(chatRoomId: String, mediaList: List<Media>) {
        mediaList.forEach { media ->
            // media 객체에서 MediaType을 추출하여 MessageSendType 객체 생성
            val messageSendType = MessageSendType(
                mediaType = media.type,  // Media 객체의 type 필드 사용
                text = media.url  // 파일 URL을 메시지 텍스트로 저장
            )

            val newChatLog = ChatMessage(
                messageId = UUID.randomUUID().toString(),  // 고유한 메시지 ID 생성
                messageSendType = messageSendType,  // 생성한 MessageSendType 객체 사용
                parentMessageId = null,  // 부모 메시지가 없는 경우 null
                parentMessagePage = null,  // 부모 메시지 페이지가 없는 경우 null
                parentSeqNumber = null,  // 부모 메시지 시퀀스 번호가 없는 경우 null
                type = MessageType.FILE,
                roomId = chatRoomId,
                sender = "system",
                timestamp = LocalDateTime.now(),
                seqNumber = chatMessageMongoRepository.countByRoomId(chatRoomId).toInt(),
                page = chatMessageMongoRepository.calculateLatestPage(chatRoomId)
            )

            // MongoDB에 저장
            chatMessageMongoRepository.save(ChatMessageMongoEntity.fromChatMessage(newChatLog, newChatLog.page!!))
        }
    }

    override fun findByChatRoomId(roomId: String): ChatRoom? {
        return chatRooms.find { it.chatRoomId == roomId }
    }

    override fun save(chatRoom: ChatRoom) {
        chatRooms.add(chatRoom)
    }


    private fun findParentMessage(parentMessageId: String): ChatLog? {
        // 모든 채팅 로그에서 부모 메시지 ID로 검색
        return chatLogs.values.flatten().find { it.messageId == parentMessageId }
    }





}