package org.chewing.v1.service

import org.chewing.v1.implementation.friend.*
import org.chewing.v1.implementation.user.UserFinder
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.*
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.user.UserName
import org.springframework.stereotype.Service

@Service
class FriendService(
    private val friendReader: FriendReader,
    private val friendRemover: FriendRemover,
    private val friendAppender: FriendAppender,
    private val friendUpdater: FriendUpdater,
    private val userReader: UserReader,
    private val friendValidator: FriendValidator,
    private val friendEnricher: FriendEnricher,
    private val userFinder: UserFinder,
) {
    fun addFriend(
        userId: String,
        friendName: UserName,
        targetContact: Credential
    ) {
        // 저장할 친구 정보를 읽어옴
        val targetUser = userFinder.findByContact(targetContact)
        // 친구 추가 가능한지 확인
        friendValidator.validateAddAllowed(userId, targetUser.userId)
        // 나의 정보를 읽어온다.
        val user = userReader.read(userId)
        // 친구 추가
        friendAppender.appendFriend(user, friendName, targetUser)
        // 상대방도 친구 맺기
        friendAppender.appendFriend(targetUser, targetUser.name, user)
    }

    // 친구 삭제
    fun removeFriend(userId: String, friendId: String) {
        friendRemover.removeFriend(userId, friendId)
    }
    // 친구 차단
    fun blockFriend(userId: String, friendId: String) {
        friendRemover.blockFriend(userId, friendId)
    }
    // 친구 목록을 가져옴
    fun getSortedFriends(userId: String, sort: SortCriteria): List<Friend> {
        val friendInfos = friendReader.readsAccess(userId)
        val users = userReader.reads(friendInfos.map { it.friendId })
        val usersStatus = userReader.readSelectedStatuses(friendInfos.map { it.friendId })
        val friends = friendEnricher.enriches(friendInfos, users, usersStatus)
        return FriendSortEngine.sort(friends, sort)
    }

    fun getFriends(friendIds: List<String>, userId: String): List<Friend> {
        val friendInfos = friendReader.readsAccessIdIn(friendIds, userId)
        val users = userReader.reads(friendInfos.map { it.friendId })
        val usersStatus = userReader.readSelectedStatuses(friendInfos.map { it.friendId })
        val friends = friendEnricher.enriches(friendInfos, users, usersStatus)
        return friends
    }

    // 친구 즐겨찾기 변경
    fun changeFriendFavorite(userId: String, friendId: String, favorite: Boolean) {
        // 친구인지 확인
        friendValidator.validateFriendShipAllowed(userId, friendId)
        // 친구 즐겨찾기 변경
        friendUpdater.updateFavorite(userId, friendId, favorite)
    }

    // 친구 이름 변경
    fun changeFriendName(userId: String, friendId: String, friendName: UserName) {
        // 친구인지 확인
        friendValidator.validateFriendShipAllowed(userId, friendId)
        // 친구 이름 변경
        friendUpdater.updateName(userId, friendId, friendName)
    }
}