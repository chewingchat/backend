package org.chewing.v1.repository

import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.ChatNumber

interface ChatRoomMemberRepository {
    fun readChatRoomMembers(chatRoomId: String): List<ChatRoomMemberInfo>
    fun readChatRoomMember(chatRoomId: String, userId: String): ChatRoomMemberInfo?
    fun saveChatRoomMember(chatRoomId: String, userId: String)
    fun changeChatRoomFavorite(chatRoomId: String, userId: String, isFavorite: Boolean)
    fun readChatRoomUsers(userId: String): List<ChatRoomMemberInfo>
    fun readChatRoomsMember(chatRoomIds: List<String>, userId: String): List<ChatRoomMemberInfo>
    fun removeChatRoomMembers(chatRoomIds: List<String>,userId: String): List<ChatRoomMemberInfo>
    fun appendChatRoomMember(chatRoomId: String, userId: String)
    fun readPersonalChatRoomId(userId: String, friendId: String): String?

    fun appendChatRoomMembers(chatRoomId: String, userIds: List<String>)
    fun updateUnDelete(chatRoomId: String, userId: String)
    fun updateRead(userId: String, number: ChatNumber)
}