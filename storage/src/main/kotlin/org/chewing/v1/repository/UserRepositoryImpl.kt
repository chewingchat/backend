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
    override fun readyId(userId: String): User? {
        val userEntity = userJpaRepository.findById(userId)
        return userEntity.map { it.toUser() }.orElse(null)
    }

    override fun readContactId(userId: String): Pair<String?, String?> {
        val userEntity = userJpaRepository.findById(userId)
        return userEntity.map { it.getEmailId() to it.getPhoneNumberId() }.orElse(null to null)
    }
    override fun readsByIds(userIds: List<String>): List<User> {
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

    override fun remove(userId: String) {
        userJpaRepository.findById(userId).ifPresent {
            it.updateDelete()
            userJpaRepository.save(it)
        }
    }

    override fun updateImage(user: User, media: Media) {
        userJpaRepository.findById(user.userId).ifPresent {
            when (media.category) {
                FileCategory.PROFILE -> it.updateUserPictureUrl(media)
                FileCategory.BACKGROUND -> it.updateBackgroundPictureUrl(media)
                else -> {}
            }
            userJpaRepository.save(it)
        }
    }

    override fun updateBackgroundImage(user: User, media: Media) {
        userJpaRepository.findById(user.userId).ifPresent {
            it.updateBackgroundPictureUrl(media)
            userJpaRepository.save(it)
        }
    }

    override fun updateName(userId: String, userName: UserName) {
        userJpaRepository.findById(userId).ifPresent {
            it.updateUserName(userName)
            userJpaRepository.save(it)
        }
    }

    override fun updateContact(userId: String, contact: Contact) {
        userJpaRepository.findById(userId).ifPresent {
            it.updateContact(contact)
            userJpaRepository.save(it)
        }
    }

    override fun updateAccess(userId: String, userContent: UserContent) {
        userJpaRepository.findById(userId).ifPresent {
            it.updateUserName(userContent.name)
            it.updateBirth(userContent.birth)
            it.updateAccess()
            userJpaRepository.save(it)
        }
    }

    override fun checkContactIsUsedByElse(contact: Contact, userId: String): Boolean {
        return when (contact) {
            is Email -> userJpaRepository.existsByEmailIdAndUserIdNot(contact.emailId, userId)
            is Phone -> userJpaRepository.existsByPhoneNumberIdAndUserIdNot(contact.phoneId, userId)
        }
    }

    override fun updateBirth(userId: String, birth: String) {
        userJpaRepository.findById(userId).ifPresent {
            it.updateBirth(birth)
            userJpaRepository.save(it)
        }
    }
    override fun updateTTS(userId: String, tts: Media): Media? {
        val userEntity = userJpaRepository.findById(userId).orElse(null)
        userEntity?.updateTTS(tts)
        userJpaRepository.save(userEntity)
        return userEntity.toTTS()
    }
}