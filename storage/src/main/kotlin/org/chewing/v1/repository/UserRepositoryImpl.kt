package org.chewing.v1.repository

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jpaentity.PushNotificationJpaEntity
import org.chewing.v1.jpaentity.friend.FriendSearchJpaEntity
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.jparepository.*
import org.chewing.v1.jparepository.FriendSearchJpaRepository
import org.chewing.v1.jparepository.UserJpaRepository
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserContent
import org.chewing.v1.model.user.UserName
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

    override fun readByContact(contact: Contact): User? {
        return when (contact) {
            is Email -> userJpaRepository.findByEmailId(contact.emailId).map { it.toUser() }.orElse(null)
            is Phone -> userJpaRepository.findByPhoneNumberId(contact.phoneId).map { it.toUser() }.orElse(null)
            else -> null
        }
    }

    override fun removePushToken(device: PushToken.Device) {
        pushNotificationJpaRepository.deleteByDeviceIdAndDeviceProvider(device.deviceId, device.provider)
    }

    override fun appendPushToken(device: PushToken.Device, appToken: String, user: User) {
        pushNotificationJpaRepository.save(PushNotificationJpaEntity.generate(appToken, device, user))
    }

    override fun appendUser(contact: Contact): User {
        return when (contact) {
            is Email -> userJpaRepository.findByEmailId(contact.emailId).map { it.toUser() }.orElseGet {
                userJpaRepository.save(UserJpaEntity.generateByEmail(contact)).toUser()
            }
            is Phone -> userJpaRepository.findByPhoneNumberId(contact.phoneId).map { it.toUser() }.orElseGet {
                userJpaRepository.save(UserJpaEntity.generateByPhone(contact)).toUser()
            }
            else -> throw NotFoundException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
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

    override fun updateContent(userId: String, content: UserContent) {
        userJpaRepository.findById(userId).ifPresent {
            it.updateUserName(content.name)
            it.updateBirth(content.birth)
            it.updateAccess()
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

    override fun checkContactIsUsedByElse(contact: Contact, userId: String): Boolean {
        return when (contact) {
            is Email -> userJpaRepository.existsByEmailIdAndUserIdNot(contact.emailId, userId)
            is Phone -> userJpaRepository.existsByPhoneNumberIdAndUserIdNot(contact.phoneId, userId)
            else -> false
        }
    }
}