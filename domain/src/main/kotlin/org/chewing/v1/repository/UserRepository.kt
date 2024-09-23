package org.chewing.v1.repository

import org.chewing.v1.model.PushToken
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.User
import org.chewing.v1.model.UserContent
import org.chewing.v1.model.UserName
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {
    fun readUserById(userId: String): User?
    fun readUsersByIds(userIds: List<String>): List<User>
    fun remove(userId: String): String?
    fun updateProfileImage(user: User, media: Media)
    fun updateName(userId: String, userName: UserName)
    fun appendSearchHistory(user: User, search: FriendSearch)
    fun readSearchHistory(userId: String): List<FriendSearch>
    fun removePushToken(device: PushToken.Device)
    fun appendPushToken(device: PushToken.Device, appToken: String, user: User)
    fun appendUser(userContent: UserContent): User
    fun readUserEmoticonPacks(userId: String): List<String>
}