package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.Media
import org.chewing.v1.model.media.MediaType
import org.hibernate.annotations.DynamicInsert
import java.util.*

@DynamicInsert
@Entity
@Table(name = "`user`", schema = "chewing")
internal class UserJpaEntity(
    @Id
    private val userId: String = UUID.randomUUID().toString(),

    private var pictureUrl: String,

    @Enumerated(EnumType.STRING)
    private var pictureType: MediaType,

    private var backgroundPictureUrl: String,

    @Enumerated(EnumType.STRING)
    private var backgroundPictureType: MediaType,

    private var userFirstName: String,

    private var userLastName: String,

    private var birth: String,

    var emailId: String?,

    var phoneNumberId: String?,

    @Enumerated(EnumType.STRING)
    private var type: AccessStatus
) : BaseEntity() {
    companion object {
        fun generateByEmail(email: Email): UserJpaEntity {
            return UserJpaEntity(
                userFirstName = "",
                userLastName = "",
                birth = "",
                pictureUrl = "",
                backgroundPictureUrl = "",
                type = AccessStatus.NOT_ACCESS,
                emailId = email.emailId,
                phoneNumberId = null,
                pictureType = MediaType.IMAGE_BASIC,
                backgroundPictureType = MediaType.IMAGE_BASIC
            )
        }

        fun generateByPhone(phone: Phone): UserJpaEntity {
            return UserJpaEntity(
                userFirstName = "",
                userLastName = "",
                birth = "",
                pictureUrl = "",
                backgroundPictureUrl = "",
                type = AccessStatus.NOT_ACCESS,
                emailId = null,
                phoneNumberId = phone.phoneId,
                pictureType = MediaType.IMAGE_BASIC,
                backgroundPictureType = MediaType.IMAGE_BASIC
            )
        }
    }

    fun toUser(): User {
        return User.of(
            this.userId,
            this.userFirstName,
            this.userLastName,
            this.birth,
            Image.of(FileCategory.PROFILE,this.pictureUrl, 0, this.pictureType),
            Image.of(FileCategory.BACKGROUND,this.backgroundPictureUrl, 0, this.backgroundPictureType),
            this.type
        )
    }

    fun updateUserPictureUrl(media: Media) {
        this.pictureUrl = media.url
    }

    fun updateBackgroundPictureUrl(media: Media) {
        this.backgroundPictureUrl = media.url
    }

    fun updateUserName(userName: UserName) {
        this.userFirstName = userName.firstName
        this.userLastName = userName.lastName
    }

    fun updateBirth(birth: String) {
        this.birth = birth
    }

    fun updateDelete() {
        this.type = AccessStatus.DELETE
    }

    fun updateAccess() {
        this.type = AccessStatus.ACCESS
    }

    fun updateContact(contact: Contact) {
        when (contact) {
            is Email -> this.emailId = contact.emailId
            is Phone -> this.phoneNumberId = contact.phoneId
        }
    }

    fun id(): String {
        return this.userId
    }
}
