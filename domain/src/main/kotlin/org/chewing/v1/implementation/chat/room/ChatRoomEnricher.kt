package org.chewing.v1.implementation.chat.room

import org.chewing.v1.model.chat.room.ChatRoom
import org.chewing.v1.model.chat.member.ChatRoomMember
import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.ChatRoomInfo
import org.chewing.v1.model.chat.room.ChatNumber
import org.springframework.stereotype.Component

@Component
class ChatRoomEnricher {

    fun enrichChatRooms(
        chatRoomMemberInfos: List<ChatRoomMemberInfo>,
        userChatRooms: List<ChatRoomMemberInfo>,
        chatRoomInfos: List<ChatRoomInfo>,
        roomSequenceNumbers: List<ChatNumber>,
        userId: String,
    ): List<ChatRoom> {
        // 모든 기준은 chatRoomId를 기준으로 매핑시켜야 함
        // chatRoomId로 chatRoomMemberInfo를 그룹화
        val groupedChatRoomMembers = chatRoomMemberInfos.groupBy { it.chatRoomId }
        val userChatRoomsMap = userChatRooms.associateBy { it.chatRoomId }
        val roomSequenceNumberMap = roomSequenceNumbers.associateBy { it.chatRoomId }

        return chatRoomInfos.mapNotNull { chatRoomInfo ->
            // myChatRoom이 없는 경우 필터링
            val myChatRoom = userChatRoomsMap[chatRoomInfo.chatRoomId] ?: return@mapNotNull null

            val chatRoomMembersInfo = groupedChatRoomMembers[chatRoomInfo.chatRoomId] ?: emptyList()
            val chatRoomMembers = chatRoomMembersInfo.map {
                ChatRoomMember.of(it.memberId, it.readSeqNumber, it.memberId == userId,)
            }
            val roomSequenceNumber = roomSequenceNumberMap[chatRoomInfo.chatRoomId]
            val totalUnRead = calculateUnreadMessages(myChatRoom, roomSequenceNumber)

            ChatRoom.of(
                chatRoomInfo = chatRoomInfo,
                userChatRoom = myChatRoom,
                chatRoomMembers = chatRoomMembers,
                totalUnReadMessage = totalUnRead,
                latestPage = roomSequenceNumber?.page ?: 0,
                latestSeqNumber = roomSequenceNumber?.sequenceNumber ?: 0
            )
        }
    }




    fun calculateUnreadMessages(myChatRoom: ChatRoomMemberInfo, roomSequenceNumber: ChatNumber?): Int {
        val seqNumber = roomSequenceNumber?.sequenceNumber ?: 0
        return seqNumber - myChatRoom.readSeqNumber
    }
}
