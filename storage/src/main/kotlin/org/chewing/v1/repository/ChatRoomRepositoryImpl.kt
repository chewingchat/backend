package org.chewing.v1.repository

import org.chewing.v1.jpaentity.chat.ChatRoomEntity
import org.chewing.v1.jparepository.ChatRoomJpaRepository
import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.model.chat.room.ChatRoomInfo
import org.springframework.stereotype.Repository


@Repository
/**
 * 채팅방 관련한 메소드만 들어갈게용
 * */
internal class ChatRoomRepositoryImpl(
    private val chatRoomJpaRepository: ChatRoomJpaRepository, // JPA 레포지토리

) : ChatRoomRepository {
    /***.
     * 채팅방 ChatRoomMemberRepository에서 유저의 채팅방 목록 가져올게용
     */
//    override fun readChatRooms(sort: String): List<ChatRoomInfo> {
//        val sortedChatRooms = when (sort) {
//            "favorite" -> chatRooms.filter { it.favorite }
//            "latest" -> chatRooms.sortedByDescending { it.latestMessageTime }
//            "notRead" -> chatRooms.filter { it.totalUnReadMessage > 0 }
//            else -> throw IllegalArgumentException("Invalid sort parameter")
//        }
//        if (sortedChatRooms.isEmpty()) throw NotFoundException(ErrorCode.CHATROOM_NOT_FOUND)
//        return sortedChatRooms
//    }

//    override fun searchChatRooms(keyword: String): List<ChatRoom> {
    // 1. JPA를 통해 기본 채팅방 정보 검색
//        val chatRooms = chatRoomJpaRepository.searchByKeyword(keyword)

    /**
     * 채팅 방이 없드면 그냥 없는 리스트 반환할게용
     */
//        if (chatRooms.isEmpty()) {
//            throw NotFoundException(ErrorCode.CHATROOM_NOT_FOUND)
//        }

    /**
     * 채팅관련 한 것은 채팅 Repository에서 할게용 !
     * */
    // 2. MongoDB를 통해 각 채팅방의 추가 정보 검색 (예: 최신 메시지)
//        return chatRooms.map { chatRoom ->
//            val latestMessage = chatMessageMongoRepository.findLatestMessageByRoomId(chatRoom.chatRoomId)
//            val updatedChatRoom = chatRoom.copy(latestMessage = latestMessage?.messageId ?: chatRoom.latestMessage)
//            updatedChatRoom
//        }
//
//        return chatRooms
//    }

    /**
     * 실제 채팅방이 지워지면 안되고 -> ChatFriendEntity에서 삭제 처리할게용
     * 채팅방을 나가기 != 채팅방 삭제
     * */
//    override fun deleteChatRooms(chatRoomIds: List<String>) {
//        chatRoomIds.forEach { chatRoomId ->
//            val roomToDelete = chatRooms.find { it.chatRoomId == chatRoomId }
//                ?: throw NotFoundException(ErrorCode.CHATROOM_NOT_FOUND)
//            chatRooms.remove(roomToDelete)
//        }
//    }


    override fun readChatRoom(chatRoomId: String): ChatRoomInfo {

        // 최신 페이지와 친구가 읽은 시퀀스 번호 계산 (MongoDB를 사용)
        /**
         * 이 것들은 모두 비즈니스 로직에 있어야 해용 -> repository는 오직 읽는 기능만 수행 해야 해요
         * 계산 과 같은 것은 Serivce에서 Calculator 구현체를 만들어서 사용 혹은 Reader에서 읽는 와중에 계산
         * */
//        val latestPage = chatMessageMongoRepository.calculateLatestPage(chatRoomId)
//        val readSeqNumber = chatSequenceMongoRepository.calculateFriendReadSeqNumber(chatRoomId.toInt(), chatRoomEntity.toChatRoomInfo().chatFriendInfos)

        /**
         * SequenceRepository에서 읽을게용
         * */
//        // MongoDB에서 가져온 데이터를 설정
//        chatRoomEntity.latestPage = latestPage
//        chatRoomEntity.readSeqNumber = readSeqNumber

        // ChatRoomEntity를 ChatRoom으로 변환 후 반환
        return chatRoomJpaRepository.findByChatRoomId(chatRoomId)
            .orElse(null).toChatRoomInfo()
    }

    /**
     * 채팅 로그 가져오는 것은 ChatMessageRepository로 이동시켜주세용
     * */
//    override fun getChatLogs(chatRoomId: String, page: Int): List<ChatLog> {
//        val logs = chatLogs[chatRoomId]?.filter { it.page == page }
//            ?: throw NotFoundException(ErrorCode.CHATLOG_NOT_FOUND)
//
//        // 답글의 부모 메시지 정보 추가
//        return logs.map { message ->
//            if (message.type == "REPLY") {
//                val parentMessage = findParentMessage(message.parentMessageId ?: "")
//                message.copy(
//                    parentMessage = parentMessage?.message,
//                    parentMessagePage = parentMessage?.page,
//                    seqNumber = parentMessage?.seqNumber
//                )
//            } else {
//                message
//            }
//        }
//    }

    // 채팅방에 파일 업로드
//    override fun uploadChatRoomFiles(chatRoomId: String, uploadedMedia: List<Media>) {
//        try {
//            // 업로드된 미디어를 채팅방에 연결하는 로직
//            saveUploadedMedia(chatRoomId, uploadedMedia)
//        } catch (e: IOException) {
//            throw IllegalArgumentException("파일 업로드에 실패했습니다: ${e.message}")
//        }
//    }

    // 업로드된 미디어를 저장하는 메서드
    /**
     * ChatMessageRepository로 이동시켜주세용
     * */
//    override fun saveUploadedMedia(chatRoomId: String, mediaList: List<Media>) {
//        mediaList.forEach { media ->
//            // media 객체에서 MediaType을 추출하여 MessageSendType 객체 생성
//            val messageSendType = MessageSendType(
//                mediaType = media.type,  // Media 객체의 type 필드 사용
//                text = media.url  // 파일 URL을 메시지 텍스트로 저장
//            )
//
//            val newChatLog = ChatMessage(
//                messageId = UUID.randomUUID().toString(),  // 고유한 메시지 ID 생성
//                messageSendType = messageSendType,  // 생성한 MessageSendType 객체 사용
//                parentMessageId = null,  // 부모 메시지가 없는 경우 null
//                parentMessagePage = null,  // 부모 메시지 페이지가 없는 경우 null
//                parentSeqNumber = null,  // 부모 메시지 시퀀스 번호가 없는 경우 null
//                type = MessageType.FILE,
//                chatRoomId = chatRoomId,
//                sender = "system",
//                timestamp = LocalDateTime.now(),
//                seqNumber = chatMessageMongoRepository.countByRoomId(chatRoomId).toInt(),
//                page = chatMessageMongoRepository.calculateLatestPage(chatRoomId)
//            )
//
//            // MongoDB에 저장
//            chatMessageMongoRepository.save(ChatMessageMongoEntity.fromChatMessage(newChatLog, newChatLog.page!!))
//        }
//    }

    /**
     * 채팅 방 찾는 로직
     * */
    override fun findByChatRoomId(chatRoomId: String): ChatRoomInfo {
        return chatRoomJpaRepository.findByChatRoomId(chatRoomId).orElse(null).toChatRoomInfo()
    }

    override fun appendChatRoom(isGroup: Boolean): String {
        return chatRoomJpaRepository.save(ChatRoomEntity.generate(isGroup)).toChatRoomId()
    }

    override fun readChatRooms(chatRoomIds: List<String>): List<ChatRoomInfo> {
        return chatRoomJpaRepository.findByChatRoomIdIn(chatRoomIds).map { it.toChatRoomInfo() }
    }

    /**
     * chatMessage Repository로 이동시켜주세용
     * */
//    private fun findParentMessage(parentMessageId: String): ChatLog? {
//        // 모든 채팅 로그에서 부모 메시지 ID로 검색
//        return chatLogs.values.flatten().find { it.messageId == parentMessageId }
//    }
}