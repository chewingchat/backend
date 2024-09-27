package org.chewing.v1.service

import org.chewing.v1.implementation.friend.*
import org.chewing.v1.implementation.user.UserFinder
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.implementation.user.UserStatusFinder
import org.chewing.v1.model.*
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.friend.Friend
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FriendService(
    private val friendReader: FriendReader,
    private val friendRemover: FriendRemover,
    private val friendAppender: FriendAppender,
    private val friendUpdater: FriendUpdater,
    private val userReader: UserReader,
    private val friendChecker: FriendChecker,
    private val friendEnricher: FriendEnricher,
    private val userStatusFinder: UserStatusFinder,
    private val userFinder: UserFinder
) {
    fun addFriend(
        userId: String,
        friendName: UserName,
        contact: Contact
    ) {
        // 저장할 친구 정보를 읽어옴
        val targetUser = userFinder.findUserByContact(contact)
        // 이미 친구인지 확인
        friendChecker.isAlreadyFriend(userId, targetUser.userId)
        // 나의 정보를 읽어온다.
        val user = userReader.read(userId)
        // 친구 추가
        friendAppender.appendFriend(user, friendName, targetUser)
    }

    // 친구 삭제
    fun removeFriend(userId: String, friendId: String) {
        friendRemover.removeFriend(userId, friendId)
    }

    // 친구 목록을 가져옴
    fun getFriends(userId: String, sort: SortCriteria): List<Friend> {
        val friendInfos = friendReader.reads(userId)
        val users = userReader.reads(friendInfos.map { it.friendId })
        val usersStatus = userStatusFinder.finds(friendInfos.map { it.friendId })
        val friends = friendEnricher.enriches(friendInfos, users, usersStatus)
        return FriendSortEngine.sort(friends, sort)
    }

    fun getFriendsIn(friendIds: List<String>, userId: String): List<Friend> {
        val friendInfos = friendReader.readsIn(friendIds, userId)
        val users = userReader.reads(friendInfos.map { it.friendId })
        val usersStatus = userStatusFinder.finds(friendInfos.map { it.friendId })
        val friends = friendEnricher.enriches(friendInfos, users, usersStatus)
        return friends
    }

    // 친구 즐겨찾기 변경
    @Transactional
    fun changeFriendFavorite(userId: String, friendId: String, favorite: Boolean) {
        // 사용자 정보를 읽어옴
        val user = userReader.read(userId)
        // 친구인지 확인
        friendChecker.isFriend(userId, friendId)
        // 친구 즐겨찾기 변경
        friendUpdater.updateFavorite(user, friendId, favorite)
    }

    // 친구 이름 변경
    @Transactional
    fun changeFriendName(userId: String, friendId: String, friendName: UserName) {
        // 사용자 정보를 읽어옴
        val user = userReader.read(userId)
        // 친구인지 확인
        friendChecker.isFriend(userId, friendId)
        // 친구 이름 변경
        friendUpdater.updateName(user, friendId, friendName)
    }
}