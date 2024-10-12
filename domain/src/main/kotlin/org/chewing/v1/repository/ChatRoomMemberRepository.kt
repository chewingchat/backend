package org.chewing.v1.repository

import org.chewing.v1.model.chat.ChatRoomMemberInfo

interface ChatRoomMemberRepository {
    fun readChatRoomMembers(roomId: String): List<ChatRoomMemberInfo>
    fun readChatRoomMember(roomId: String, userId: String): ChatRoomMemberInfo?
    fun saveChatRoomMember(roomId: String, userId: String)
    fun changeChatRoomFavorite(roomId: String, userId: String, isFavorite: Boolean)
    fun readChatRoomUsers(userId: String): List<ChatRoomMemberInfo>
    fun readChatRoomsMember(roomIds: List<String>, userId: String): List<ChatRoomMemberInfo>
    fun removeChatRoomMembers(chatRoomIds: List<String>,userId: String): List<ChatRoomMemberInfo>
    fun appendChatRoomMember(roomId: String, userId: String)
    fun readPersonalChatRoomId(userId: String, friendId: String): String?

    fun appendChatRoomMembers(roomId: String, userIds: List<String>)
    fun updateUnDelete(roomId: String, userId: String)
}