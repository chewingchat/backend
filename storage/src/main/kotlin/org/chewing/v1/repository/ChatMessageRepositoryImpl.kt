package org.chewing.v1.repository

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jparepository.ChatRoomJpaRepository
import org.chewing.v1.model.chat.ChatLog
import org.chewing.v1.model.chat.ChatMessage
import org.chewing.v1.mongoentity.ChatMessageMongoEntity
import org.chewing.v1.mongoentity.ChatSequenceMongoEntity
import org.chewing.v1.mongorepository.ChatMessageMongoRepository
import org.springframework.stereotype.Repository


@Repository
class ChatMessageRepositoryImpl(
    private val chatMessageMongoRepository: ChatMessageMongoRepository,
//    private val chatRoomJpaRepository: ChatRoomJpaRepository
) : ChatMessageRepository {


    // 메시지를 MongoDB에 저장하는 로직 (기존 로직 사용)
    override fun appendChatMessage(chatMessage: ChatMessage, page: Int) {
        val chatMessageEntity = ChatMessageMongoEntity.fromChatMessage(chatMessage, page)
        chatMessageMongoRepository.save(chatMessageEntity)
    }

    // 채팅방의 특정 페이지의 메시지를 조회
    override fun readChatMessages(roomId: String, page: Int): List<ChatMessage> {
        // MongoDB에서 roomId와 page로 메시지 조회
        val messageEntities = chatMessageMongoRepository.findByRoomIdAndPage(roomId, page)

        /**
         * page가 주어진상태에서 바로 메시지 읽기 할게용
         */
//        // 해당 채팅방의 친구 목록을 가져옴 (ChatRoom 정보를 통해 가져올 수 있다고 가정)
//        val chatRoomEntity = chatRoomJpaRepository.findByChatRoomId(roomId)
//            .orElseThrow { NotFoundException(ErrorCode.CHATROOM_NOT_FOUND) }

        /**
         * 지금 생각해보니 메시지 보낼때 보내는 친구(senderId) Id만 있어도 될 것 같아요
         */
        // 친구 시퀀스 정보를 담은 FriendSeqInfo 리스트 동적 생성
//        val friends = chatRoomEntity.chatFriends.map { friend ->
//            // 각 친구가 마지막으로 읽은 메시지의 시퀀스 번호를 계산
//            val lastReadSeq = chatMessageMongoRepository.findLastMessageByRoomIdAndFriendId(roomId, friend.friendId)
//                ?.seqNumber ?: 0  // 읽은 메시지가 없으면 0으로 처리
//
//            ChatMessage.FriendSeqInfo(friend.friendId, lastReadSeq)
//        }

        // ChatMessageMongoEntity를 ChatMessage로 변환하여 ChatLog를 생성
        return messageEntities.map { it.toChatMessage() }
    }

    // 메시지를 삭제하는 기능
    /**
     * room확인 하지 않고
     * 바로 메시지 삭제 할게용
     * 이떄 실제 삭제는 하지않고, type을 delete로 업데이트만 할게용
     */
    override fun deleteMessage(roomId: String, messageId: String) {
        // 메시지 ID로 MongoDB에서 메시지 조회
        val messageEntity = chatMessageMongoRepository.findById(messageId)
        messageEntity.ifPresent {
            // 해당 메시지가 roomId에 속해 있는지 확인 후 삭제
//            if (it.roomId == roomId) {
//                chatMessageMongoRepository.delete(it)  // 메시지를 삭제
//            } else {
//                throw IllegalArgumentException("해당 채팅방에 메시지가 없습니다.")
//            }
            it.delete()
        } ?: throw IllegalArgumentException("해당 메시지를 찾을 수 없습니다.")
    }

    // MongoDB에서 친구의 마지막 읽은 메시지 시퀀스를 조회
    /** ChatFriendEntity에서
     * 마지막 읽은것 조회 할게용
     */
    override fun findFriendLastSeqNumber(roomId: String, friendId: Int): Int {
        val lastMessage = chatMessageMongoRepository.findLastMessageByRoomIdAndFriendId(roomId, friendId)
        return lastMessage?.seqNumber ?: 0
    }
}


