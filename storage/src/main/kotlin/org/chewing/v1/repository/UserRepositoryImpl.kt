package org.chewing.v1.repository

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
import org.chewing.v1.model.user.UserAccount

import org.springframework.stereotype.Repository

@Repository
internal class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun read(userId: String): User? {
        val userEntity = userJpaRepository.findById(userId)
        return userEntity.map { it.toUser() }.orElse(null)
    }

    override fun readAccount(userId: String): UserAccount? {
        val userEntity = userJpaRepository.findById(userId)
        return userEntity.map {
            it.toUserAccount()
        }.orElse(null)
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

    override fun remove(userId: String): User? {
        return userJpaRepository.findById(userId)
            .map { entity ->
                entity.updateDelete()
                userJpaRepository.save(entity)
                entity.toUser()
            }.orElse(null)
    }

    override fun updateMedia(userId: String, media: Media): Media? {
        return userJpaRepository.findById(userId).map { user ->
            // 수정 전 기존 미디어 정보를 반환
            val previousMedia = when (media.category) {
                FileCategory.PROFILE -> user.toUser().image // 기존 프로필 이미지
                FileCategory.BACKGROUND -> user.toUser().backgroundImage // 기존 배경 이미지
                FileCategory.TTS -> user.toTTS() // 기존 TTS 정보
                else -> null
            }

            // 새로운 미디어 정보 업데이트
            when (media.category) {
                FileCategory.PROFILE -> user.updateUserPictureUrl(media)
                FileCategory.BACKGROUND -> user.updateBackgroundPictureUrl(media)
                FileCategory.TTS -> user.updateTTS(media)
                else -> {}
            }

            // 사용자 정보 저장
            userJpaRepository.save(user)

            // 수정 전 정보를 반환
            previousMedia
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