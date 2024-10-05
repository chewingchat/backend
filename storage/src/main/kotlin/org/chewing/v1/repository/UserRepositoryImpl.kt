package org.chewing.v1.repository

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.jparepository.UserJpaRepository
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserContent
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.Media

import org.springframework.stereotype.Repository

@Repository
internal class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun read(userId: String): User? {
        val userEntity = userJpaRepository.findById(userId)
        return userEntity.map { it.toUser() }.orElse(null)
    }

    override fun readContactId(userId: String): Pair<String?, String?> {
        val userEntity = userJpaRepository.findById(userId)
        return userEntity.map { it.getEmailId() to it.getPhoneNumberId() }.orElse(null to null)
    }

    override fun reads(userIds: List<String>): List<User> {
        val userEntities = userJpaRepository.findAllById(userIds.map { it })
        return userEntities.map { it.toUser() }
    }

    override fun readByContact(contact: Contact): User? {
        return when (contact) {
            is Email -> userJpaRepository.findByEmailId(contact.emailId).map { it.toUser() }.orElse(null)
            is Phone -> userJpaRepository.findByPhoneNumberId(contact.phoneId).map { it.toUser() }.orElse(null)
        }
    }

    override fun append(contact: Contact): User {
        return when (contact) {
            is Email -> userJpaRepository.findByEmailId(contact.emailId).map { it.toUser() }.orElseGet {
                userJpaRepository.save(UserJpaEntity.generateByEmail(contact)).toUser()
            }

            is Phone -> userJpaRepository.findByPhoneNumberId(contact.phoneId).map { it.toUser() }.orElseGet {
                userJpaRepository.save(UserJpaEntity.generateByPhone(contact)).toUser()
            }
        }
    }

    override fun remove(userId: String): String? {
        return userJpaRepository.findById(userId)
            .map { entity ->
                entity.updateDelete()
                userJpaRepository.save(entity)
                userId
            }.orElse(null)
    }

    override fun updateMedia(user: User, media: Media) : String? {
        return userJpaRepository.findById(user.userId).map {
            when (media.category) {
                FileCategory.PROFILE -> it.updateUserPictureUrl(media)
                FileCategory.BACKGROUND -> it.updateBackgroundPictureUrl(media)
                FileCategory.TTS -> it.updateTTS(media)
                else -> {}
            }
            userJpaRepository.save(it)
            user.userId
        }.orElse(null)
    }

    override fun updateName(userId: String, userName: UserName): String? {
        return userJpaRepository.findById(userId).map {
            it.updateUserName(userName)
            userJpaRepository.save(it)
            userId
        }.orElse(null)
    }

    override fun updateContact(userId: String, contact: Contact): String? {
        return userJpaRepository.findById(userId).map {
            it.updateContact(contact)
            userJpaRepository.save(it)
            userId
        }.orElse(null)
    }

    override fun updateAccess(userId: String, userContent: UserContent): String? {
        return userJpaRepository.findById(userId).map {
            it.updateUserName(userContent.name)
            it.updateBirth(userContent.birth)
            it.updateAccess()
            userJpaRepository.save(it)
            userId
        }.orElse(null)
    }

    override fun checkContactIsUsedByElse(contact: Contact, userId: String): Boolean {
        return when (contact) {
            is Email -> userJpaRepository.existsByEmailIdAndUserIdNot(contact.emailId, userId)
            is Phone -> userJpaRepository.existsByPhoneNumberIdAndUserIdNot(contact.phoneId, userId)
        }
    }

    override fun updateBirth(userId: String, birth: String): String? {
        return userJpaRepository.findById(userId).map {
            it.updateBirth(birth)
            userJpaRepository.save(it)
            userId
        }.orElse(null)
    }
}