package org.chewing.v1.repository

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.chat.message.*
import org.chewing.v1.model.chat.room.ChatNumber
import org.chewing.v1.model.media.Media
import org.chewing.v1.mongoentity.*
import org.chewing.v1.mongoentity.ChatCommonMongoEntity
import org.chewing.v1.mongoentity.ChatFileMongoEntity
import org.chewing.v1.mongoentity.ChatInviteMongoEntity
import org.chewing.v1.mongoentity.ChatLeaveMongoEntity
import org.chewing.v1.mongorepository.ChatLogMongoRepository
import org.springframework.stereotype.Repository


@Repository
internal class ChatLogRepositoryImpl(
    private val chatLogMongoRepository: ChatLogMongoRepository,
//    private val chatRoomJpaRepository: ChatRoomJpaRepository
) : ChatLogRepository {
//
//    // 메시지를 MongoDB에 저장하는 로직 (기존 로직 사용)
//     fun appendChatMessage(chatMessage: ChatMessage, page: Int) {
//        val chatMessageEntity = ChatMessageMongoEntity.fromChatMessage(chatMessage, page)
//        chatLogMongoRepository.save(chatMessageEntity)
//    }

    // 채팅방의 특정 페이지의 메시지를 조회
    override fun readChatMessages(chatRoomId: String, page: Int): List<ChatMessage> {
        // MongoDB에서 chatRoomId와 page로 메시지 조회
        val messageEntities = chatLogMongoRepository.findByRoomIdAndPage(chatRoomId, page)

        /**
         * page가 주어진상태에서 바로 메시지 읽기 할게용
         */
//        // 해당 채팅방의 친구 목록을 가져옴 (ChatRoom 정보를 통해 가져올 수 있다고 가정)
//        val chatRoomEntity = chatRoomJpaRepository.findByChatRoomId(chatRoomId)
//            .orElseThrow { NotFoundException(ErrorCode.CHATROOM_NOT_FOUND) }

        /**
         * 지금 생각해보니 메시지 보낼때 보내는 친구(senderId) Id만 있어도 될 것 같아요
         */
        // 친구 시퀀스 정보를 담은 FriendSeqInfo 리스트 동적 생성
//        val friends = chatRoomEntity.chatFriends.map { friend ->
//            // 각 친구가 마지막으로 읽은 메시지의 시퀀스 번호를 계산
//            val lastReadSeq = chatMessageMongoRepository.findLastMessageByRoomIdAndFriendId(chatRoomId, friend.friendId)
//                ?.seqNumber ?: 0  // 읽은 메시지가 없으면 0으로 처리
//
//            ChatMessage.FriendSeqInfo(friend.friendId, lastReadSeq)
//        }

        // ChatMessageMongoEntity를 ChatMessage로 변환하여 ChatLog를 생성
        return messageEntities.map { it.toChatMessage() }
    }

    override fun readChatMessage(messageId: String): ChatMessage? {
        return chatLogMongoRepository.findById(messageId).map { it.toChatMessage() }.orElse(null)
    }

    override fun readLatestMessages(numbers: List<ChatNumber>): List<ChatMessage> {
        val conditions = numbers.map {
            mapOf("chatRoomId" to it.chatRoomId, "seqNumber" to it.sequenceNumber)
        }

        val messages = chatLogMongoRepository.findByRoomIdAndSeqNumbers(conditions)
        return messages.map { it.toChatMessage() }
    }

    // 메시지를 삭제하는 기능
    /**
     * room확인 하지 않고
     * 바로 메시지 삭제 할게용
     * 이떄 실제 삭제는 하지않고, type을 delete로 업데이트만 할게용
     */
    override fun removeMessage(messageId: String) {
        // 메시지 ID로 MongoDB에서 메시지 조회
        val messageEntity = chatLogMongoRepository.findById(messageId)
        messageEntity.ifPresent {
            // 해당 메시지가 chatRoomId에 속해 있는지 확인 후 삭제
//            if (it.chatRoomId == chatRoomId) {
//                chatMessageMongoRepository.delete(it)  // 메시지를 삭제
//            } else {
//                throw IllegalArgumentException("해당 채팅방에 메시지가 없습니다.")
//            }
            it.delete()
        } ?: throw IllegalArgumentException("해당 메시지를 찾을 수 없습니다.")
    }

    override fun appendChatLog(chatMessage: ChatMessage) {
        when (chatMessage) {
            is ChatCommonMessage -> {
                val entity = ChatCommonMongoEntity.from(chatMessage)
                chatLogMongoRepository.save(entity)
            }

            is ChatFileMessage -> {
                val entity = ChatFileMongoEntity.from(chatMessage)
                chatLogMongoRepository.save(entity)
            }

            is ChatLeaveMessage -> {
                val entity = ChatLeaveMongoEntity.from(chatMessage)
                chatLogMongoRepository.save(entity)
            }

            is ChatInviteMessage -> {
                val entity = ChatInviteMongoEntity.from(chatMessage)
                chatLogMongoRepository.save(entity)
            }
            is ChatReplyMessage -> {
                val entity = ChatReplyMongoEntity.from(chatMessage)
                chatLogMongoRepository.save(entity)
            }

            else -> throw ConflictException(ErrorCode.WRONG_VALIDATE_CODE)
        }

        // MongoDB에서 친구의 마지막 읽은 메시지 시퀀스를 조회
        /** ChatRoomMemberRepository에서
         * 마지막 읽은것 조회 할게용
         */
//     fun findFriendLastSeqNumber(chatRoomId: String, friendId: Int): Int {
//        val lastMessage = chatMessageMongoRepository.findLastMessageByRoomIdAndFriendId(chatRoomId, friendId)
//        return lastMessage?.seqNumber ?: 0
//    }
    }
}


