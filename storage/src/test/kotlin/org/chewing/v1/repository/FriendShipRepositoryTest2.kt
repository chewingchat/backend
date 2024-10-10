package org.chewing.v1.repository

import org.chewing.v1.jpaentity.friend.FriendShipId
import org.chewing.v1.jparepository.FriendShipJpaRepository
import org.chewing.v1.jparepository.UserJpaRepository
import org.chewing.v1.repository.support.UserProvider
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*

class FriendShipRepositoryTest2 {
    private val friendShipJpaRepository: FriendShipJpaRepository = mock()

    private var friendShipRepositoryImpl: FriendShipRepositoryImpl = FriendShipRepositoryImpl(friendShipJpaRepository)

    @Test
    fun `친구 삭제 -  실패(친구 관계가 존재하지 않음)`() {
        val userId = "userId"
        val friendId = "friendId"
        whenever(friendShipJpaRepository.findById(FriendShipId(userId, friendId))).thenReturn(Optional.empty())
        val result = friendShipRepositoryImpl.remove(userId, friendId)
        assert(result == null)
    }

    @Test
    fun `친구 차단 -  실패(친구 관계가 존재하지 않음)`() {
        val userId = "userId"
        val friendId = "friendId"
        whenever(friendShipJpaRepository.findById(FriendShipId(userId, friendId))).thenReturn(Optional.empty())
        val result = friendShipRepositoryImpl.block(userId, friendId)
        assert(result == null)
    }

    @Test
    fun `친구 차단 해제 -  실패(친구 관계가 존재하지 않음)`() {
        val userId = "userId"
        val friendId = "friendId"
        whenever(friendShipJpaRepository.findById(FriendShipId(userId, friendId))).thenReturn(Optional.empty())
        val result = friendShipRepositoryImpl.blocked(userId, friendId)
        assert(result == null)
    }

    @Test
    fun `친구 관계 읽기 -  실패(친구 관계가 존재하지 않음)`() {
        val userId = "userId"
        val friendId = "friendId"
        whenever(friendShipJpaRepository.findById(FriendShipId(userId, friendId))).thenReturn(Optional.empty())
        val result = friendShipRepositoryImpl.read(userId, friendId)
        assert(result == null)
    }

    @Test
    fun `친구 즐겨 찾기 - 실패(친구 관계가 존재하지 않음)`() {
        val userId = "userId"
        val friendId = "friendId"
        whenever(friendShipJpaRepository.findById(FriendShipId(userId, friendId))).thenReturn(Optional.empty())
        val result = friendShipRepositoryImpl.updateFavorite(userId, friendId, true)
        assert(result == null)
    }

    @Test
    fun `친구 관계 이름 변경 - 실패(친구 관계가 존재하지 않음)`() {
        val userId = "userId"
        val friendId = "friendId"
        val newName = UserProvider.buildFriendName()
        whenever(friendShipJpaRepository.findById(FriendShipId(userId, friendId))).thenReturn(Optional.empty())
        val result = friendShipRepositoryImpl.updateName(userId, friendId, newName)
        assert(result == null)
    }

}