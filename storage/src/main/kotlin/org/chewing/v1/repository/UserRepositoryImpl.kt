package org.chewing.v1.repository

import org.chewing.v1.jpaentity.friend.FriendSearchJpaEntity
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.jpaentity.user.UserStatusJpaEntity
import org.chewing.v1.jparepository.AuthJpaRepository
import org.chewing.v1.jparepository.FriendSearchJpaRepository
import org.chewing.v1.jparepository.UserJpaRepository
import org.chewing.v1.jparepository.UserStatusJpaRepository
import org.chewing.v1.model.PushToken
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
    private val authJpaRepository: AuthJpaRepository,
    private val friendSearchJpaRepository: FriendSearchJpaRepository,
    private val userStatusJpaRepository: UserStatusJpaRepository
) : UserRepository {
    override fun readUserById(userId: User.UserId): User? {
        val userEntity = userJpaRepository.findById(userId.value())
        return userEntity.map { it.toUser() }.orElse(null)
    }

    override fun readUserByContact(contact: Contact): User? {
        return when (contact) {
            is Email -> {
                readUserByEmail(contact.emailAddress)
            }

            is Phone ->
                readUserByPhoneNumber(contact.number, contact.country)

            else -> null
        }
    }

    override fun readUserWithStatus(userId: User.UserId): User? {
        val userFulledEntity = userStatusJpaRepository.findSelectedUserStatusWithUser(userId.value())
        return userFulledEntity.map { it.toUserWithStatusAndEmoticon() }.orElse(null)
    }

    override fun readUsersWithStatuses(userIds: List<User.UserId>): List<User> {
        val userFulledEntities = userStatusJpaRepository.findSelectedUsersStatusWithUser(userIds.map { it.value() })
        return userFulledEntities.map { it.toUserWithStatusAndEmoticon() }
    }

    override fun readPushToken(pushToken: PushToken): PushToken? {
        TODO("Not yet implemented")
    }

    override fun appendUserPushToken(user: User, pushToken: PushToken) {
        TODO("Not yet implemented")
    }

    override fun updateUserPushToken(user: User, pushToken: PushToken) {
        TODO("Not yet implemented")
    }

    override fun saveUser(user: User): User.UserId {
        userJpaRepository.save(UserJpaEntity.fromUser(user)).userId.let {
            return User.UserId.of(it)
        }
    }

    override fun remove(userId: User.UserId): User.UserId? {
        userJpaRepository.deleteById(userId.value())
        return userId
    }

    override fun updateUser(user: User) {
        userJpaRepository.save(UserJpaEntity.fromUser(user))
    }

    override fun readUserByEmail(email: String): User? {
        return authJpaRepository.findUserByEmail(email).map {
            it.user.toUser()
        }.orElse(null)
    }

    override fun readUserByPhoneNumber(phoneNumber: String, countryCode: String): User? {
        return authJpaRepository.findByPhoneNumber(phoneNumber, countryCode).map {
            it.user.toUser()
        }.orElse(null)
    }

    override fun appendSearchHistory(user: User, search: FriendSearch) {
        friendSearchJpaRepository.save(FriendSearchJpaEntity.fromFriendSearch(user, search))
    }

    override fun readSearchHistory(userId: User.UserId): List<FriendSearch> {
        return friendSearchJpaRepository.findAllByUserUserId(userId.value()).map {
            it.toFriendSearch()
        }
    }
}