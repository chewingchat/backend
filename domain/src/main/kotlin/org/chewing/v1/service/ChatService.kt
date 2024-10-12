package org.chewing.v1.service

import org.chewing.v1.implementation.chat.*
import org.chewing.v1.implementation.chat.log.ChatLogAppender
import org.chewing.v1.implementation.chat.log.ChatLogReader
import org.chewing.v1.implementation.chat.message.ChatSender
import org.chewing.v1.implementation.chat.sequence.ChatHelper
import org.chewing.v1.implementation.chat.sequence.ChatSequenceReader
import org.chewing.v1.implementation.media.FileHandler
import org.chewing.v1.model.chat.*
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.chewing.v1.repository.ChatRoomRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class ChatService(
//    private val chatLogAppender: ChatLogAppender,
    private val chatLogReader: ChatLogReader,
//    private val chatSequenceUpdater: ChatSequenceUpdater,
    private val chatSender: ChatSender,
    private val chatSequenceReader: ChatSequenceReader,
    private val fileHandler: FileHandler,
    private val chatHelper: ChatHelper,
    private val chatLogAppender: ChatLogAppender,
//    private val chatRoomService: ChatRoomService,
//    private val chatRoomRepository: ChatRoomRepository,
//    private val fileHandler: FileHandler
) {
    fun uploadFiles(fileDataList: List<FileData>, userId: String, chatRoomId: String) {
        val medias = fileHandler.handleNewFiles(userId, fileDataList, FileCategory.CHATROOM)
        val chatRoomNumber = chatHelper.findNextNumber(chatRoomId)
        val chatLog = chatLogAppender.appendFileLog(medias, chatRoomId, userId, chatRoomNumber)
    }


//    fun saveAndSendChat(chatMessage: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(chatMessage.roomId!!)
//        val page = calculatePage(seq)
//        chatLogAppender.appendChatMessage(chatMessage, page)
//        chatSender.sendChat(chatMessage)
//    }
//
//    fun enterChatRoom(roomId: String, userId: String) {
//        // 0. 채팅방이 존재하는지 확인하고 없으면 생성
//        val chatRoom = chatRoomService.getOrCreateChatRoom(roomId, userId)
//
//        // 1. 시퀀스 번호 업데이트
//        val seq = chatSequenceUpdater.updateSequenceIncrement(chatRoom.chatRoomId)
//
//        // 2. 친구 목록과 각 친구의 읽은 시퀀스 번호를 가져오기
//        val friendsWithSeq = getFriendsWithSeqNumber(roomId)
//
//        // 3. 입장 메시지 생성
//        val enterMessage = ChatMessage.withoutId(
//            type = MessageType.IN,
//            roomId = roomId,
//            sender = userId,
//            messageSendType = MessageSendType(
//                mediaType = null,
//                text = "User $userId has entered the chat"
//            ),
//
//            parentMessageId = null,
//            parentMessagePage = null,
//            parentMessageSeqNumber = null,
//            friends = friendsWithSeq,
//            page = calculatePage(seq)
//        )
//
//        // 4. 메시지를 기록하고 전송
////        chatLogAppender.appendChatMessage(enterMessage, calculatePage(seq))
////        chatSender.sendChat(enterMessage)
////
////        // 5. 읽음 처리 및 채팅 시퀀스 MongoDB에 저장
////        chatSequenceReader.saveChatSequence(roomId, userId, seq)
////    }
//
//    // 채팅방 친구 목록과 읽은 시퀀스 번호를 조회하는 메서드 (DB 연동으로 수정)
//    fun getFriendsWithSeqNumber(roomId: String): List<ChatMessage.FriendSeqInfo> {
//        // 실제 데이터베이스에서 친구 목록과 그들의 읽음 상태를 가져오는 로직으로 수정해야 합니다.
//        val friendsList = chatLogAppender.getChatFriends(roomId)  // 친구 목록을 DB에서 가져옴
//        return friendsList.map { friend ->
//            val lastSeqNumber = chatSequenceReader.readFriendLastSeqNumber(roomId, friend.friendId)
//            ChatMessage.FriendSeqInfo(friend.friendId, lastSeqNumber)
//        }
//    }

//    fun deleteChatMessage(roomId: String, messageId: String) {
//        chatLogAppender.deleteMessage(roomId, messageId)
//        val deleteMessage = ChatMessage.withoutId(
//            type = MessageType.DELETE,
//            roomId = roomId,
//            sender = "system",
//            messageSendType = MessageSendType(  // MessageSendType 인스턴스 생성
//                mediaType = null,  // DELETE 메시지 또한 TEXT로 처리
//                text = "Message has been deleted"
//            ),
//            parentMessageId = null,
//            parentMessagePage = null,
//            parentMessageSeqNumber = null,
//            friends = null,
//            page = 1
//        )
//        chatSender.sendChat(deleteMessage)
//    }

//    fun sendFileMessage(chatRoomId: String, fileDataList: List<FileData>) {
//        // 파일을 처리하여 Media로 변환
//        val mediaList = fileHandler.handleNewFiles(chatRoomId, fileDataList, FileCategory.FEED)
//
//        // 파일 메시지로 전송
//        mediaList.forEach { media ->
//            val message = ChatMessage.withoutId(
//                type = MessageType.FILE,
//                roomId = chatRoomId,
//                sender = "system",  // 보낸 사람
//                messageSendType = MessageSendType(
//                    mediaType = media.type,
//                    text = media.url  // 파일 URL
//                ),
//                parentMessageId = null,
//                parentMessagePage = null,
//                parentMessageSeqNumber = null,
//                friends = null,
//                page = 1
//            )
//            chatLogAppender.appendChatMessage(message, 1)
//            chatSender.sendChat(message)
//        }
//    }

//    fun sendReplyMessage(chatMessage: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(chatMessage.roomId!!)
//        val page = calculatePage(seq)
//
//        // REPLY 메시지 처리
//        chatLogAppender.appendChatMessage(chatMessage, page)
//        chatSender.sendChat(chatMessage)
//    }
//
//
//
//
//    fun leaveChatRoom(roomId: String, userId: String) {
//        // LEAVE 메시지를 생성
//        val leaveMessage = ChatMessage.withoutId(
//            type = MessageType.LEAVE,
//            roomId = roomId,
//            sender = userId,
//            messageSendType = MessageSendType(  // MessageSendType 인스턴스 생성
//                mediaType = null,  // LEAVE 메시지도 TEXT로 처리
//                text = "User $userId has left the chat"
//            ),
//
//            parentMessageId = null,
//            parentMessagePage = null,
//            parentMessageSeqNumber = null,
//            friends = null,
//            page = 1
//        )
//        chatSender.sendChat(leaveMessage)
//        chatRoomRepository.deleteChatRooms(listOf(roomId))
//    }

//
//    // 채팅방 입장 처리 (읽음 처리용)
//    fun processEnterMessage(message: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(message.roomId!!)
//        val page = calculatePage(seq)
//
//        val enterMessage = ChatMessage(
//            messageId = message.messageId ?: generateMessageId(),
//            type = MessageType.IN,
//            roomId = message.roomId,
//            sender = message.sender,
//            messageSendType = message.messageSendType ?: MessageSendType(mediaType = null, text = "User entered"),
//            parentMessageId = null,
//            parentMessagePage = message.parentMessagePage,
//            parentSeqNumber = message.parentSeqNumber,
//            timestamp = LocalDateTime.now(),
//            page = page
//        )
//        chatLogAppender.appendChatMessage(enterMessage, page)
//        chatSender.sendChat(enterMessage)
//    }
//
//    // 단체 채팅방에서 입장 처리
//    fun processGroupEnterMessage(message: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(message.roomId!!)
//        val page = calculatePage(seq)
//
//        val enterGroupMessage = ChatMessage(
//            messageId = message.messageId ?: generateMessageId(),
//            type = MessageType.ENTER,
//            roomId = message.roomId,
//            sender = message.sender,
//            messageSendType = message.messageSendType ?: MessageSendType(mediaType = null, text = "User entered group"),
//            parentMessageId = null,
//            parentMessagePage = message.parentMessagePage,
//            parentSeqNumber = message.parentSeqNumber,
//            timestamp = LocalDateTime.now(),
//            page = page
//        )
//        chatLogAppender.appendChatMessage(enterGroupMessage, page)
//        chatSender.sendChat(enterGroupMessage)
//    }
//
//    // 채팅방 밖에서 채팅 메시지 처리
//    fun processChatOutsideMessage(message: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(message.roomId!!)
//        val page = calculatePage(seq)
//
//        val chatOutsideMessage = ChatMessage(
//            messageId = message.messageId ?: generateMessageId(),
//            type = MessageType.CHAT,
//            roomId = message.roomId,
//            sender = message.sender,
//            messageSendType = message.messageSendType ?: MessageSendType(mediaType = null, text = message.messageId),
//            parentMessageId = null,
//            parentMessagePage = message.parentMessagePage,
//            parentSeqNumber = message.parentSeqNumber,
//            timestamp = LocalDateTime.now(),
//            page = page
//        )
//        chatLogAppender.appendChatMessage(chatOutsideMessage, page)
//        chatSender.sendChat(chatOutsideMessage)
//    }
//
//    // 채팅방 밖에서 파일 메시지 처리
//    fun processFileOutsideMessage(message: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(message.roomId!!)
//        val page = calculatePage(seq)
//
//        val fileOutsideMessage = ChatMessage(
//            messageId = message.messageId ?: generateMessageId(),
//            type = MessageType.FILE,
//            roomId = message.roomId,
//            sender = message.sender,
//            messageSendType = message.messageSendType ?: MessageSendType(mediaType = message.messageSendType?.mediaType, text = message.messageSendType?.text),
//            parentMessageId = null,
//            parentMessagePage = message.parentMessagePage,
//            parentSeqNumber = message.parentSeqNumber,
//            timestamp = LocalDateTime.now(),
//            page = page
//        )
//        chatLogAppender.appendChatMessage(fileOutsideMessage, page)
//        chatSender.sendChat(fileOutsideMessage)
//    }
//
//
//
//
//    fun generateMessageId(): String {
//        return java.util.UUID.randomUUID().toString()
//    }
//
//
//
//
//
//    private fun calculatePage(seq: Long): Int {
//        return (seq / PAGE_SIZE).toInt()
//    }
//
//
//
//    companion object {
//        const val PAGE_SIZE = 50
//    }
}