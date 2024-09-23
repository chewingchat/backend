package org.chewing.v1.repository

import org.chewing.v1.jpaentity.PushNotificationJpaEntity
import org.chewing.v1.jpaentity.friend.FriendSearchJpaEntity
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.jparepository.*
import org.chewing.v1.jparepository.FriendSearchJpaRepository
import org.chewing.v1.jparepository.UserJpaRepository
import org.chewing.v1.jparepository.UserStatusJpaRepository
import org.chewing.v1.model.PushToken
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.User
import org.chewing.v1.model.UserContent
import org.chewing.v1.model.UserName
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.media.Media

import org.springframework.stereotype.Repository

@Repository
internal class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
    private val friendSearchJpaRepository: FriendSearchJpaRepository,
    private val pushNotificationJpaRepository: PushNotificationJpaRepository,
    private val userEmoticonJpaRepository: UserEmoticonJpaRepository
) : UserRepository {
    override fun readUserById(userId: String): User? {
        val userEntity = userJpaRepository.findById(userId)
        return userEntity.map { it.toUser() }.orElse(null)
    }

    override fun readUsersByIds(userIds: List<String>): List<User> {
        val userEntities = userJpaRepository.findAllById(userIds.map { it })
        return userEntities.map { it.toUser() }
    }

    override fun removePushToken(device: PushToken.Device) {
        pushNotificationJpaRepository.deleteByDeviceIdAndDeviceProvider(device.deviceId, device.provider)
    }

    override fun appendPushToken(device: PushToken.Device, appToken: String, user: User) {
        pushNotificationJpaRepository.save(PushNotificationJpaEntity.generate(appToken, device, user))
    }

    override fun appendUser(userContent: UserContent): User {
        return userJpaRepository.save(UserJpaEntity.generate(userContent)).toUser()
    }

    override fun remove(userId: String): String? {
        userJpaRepository.findById(userId).ifPresent {
            it.updateDelete()
            userJpaRepository.save(it)
        }
        return userId
    }

    override fun updateProfileImage(user: User, media: Media) {
        userJpaRepository.findById(user.userId).ifPresent {
            it.updateUserPictureUrl(media)
            userJpaRepository.save(it)
        }
    }

    override fun updateName(userId: String, userName: UserName) {
        userJpaRepository.findById(userId).ifPresent {
            it.updateUserName(userName)
            userJpaRepository.save(it)
        }
    }

    override fun appendSearchHistory(user: User, search: FriendSearch) {
        friendSearchJpaRepository.save(FriendSearchJpaEntity.fromFriendSearch(user, search))
    }

    override fun readSearchHistory(userId: String): List<FriendSearch> {
        return friendSearchJpaRepository.findAllByUserId(userId).map {
            it.toFriendSearch()
        }
    }

    override fun readUserEmoticonPacks(userId: String): List<String> {
        return userEmoticonJpaRepository.findAllByIdUserId(userId).map { it.id.emoticonPackId }
    }
}