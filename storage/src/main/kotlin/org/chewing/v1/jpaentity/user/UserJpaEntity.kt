package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.ActivateType
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.media.Media
import org.hibernate.annotations.DynamicInsert
import java.util.*

@DynamicInsert
@Entity
@Table(name = "`user`", schema = "chewing")
internal class UserJpaEntity(
    @Id
    private val userId: String = UUID.randomUUID().toString(),

    private var pictureUrl: String,

    private var backgroundPictureUrl: String,

    private var userFirstName: String,

    private var userLastName: String,

    private var birth: String,

    val emailId: String?,

    val phoneNumberId: String?,

    @Enumerated(EnumType.STRING)
    private var type: ActivateType
) : BaseEntity() {
    companion object {
        fun generateByEmail(email: Email): UserJpaEntity {
            return UserJpaEntity(
                userFirstName = "",
                userLastName = "",
                birth = "",
                pictureUrl = "",
                backgroundPictureUrl = "",
                type = ActivateType.NOT_ACCESS,
                emailId = email.emailId,
                phoneNumberId = null
            )
        }

        fun generateByPhone(phone: Phone): UserJpaEntity {
            return UserJpaEntity(
                userFirstName = "",
                userLastName = "",
                birth = "",
                pictureUrl = "",
                backgroundPictureUrl = "",
                type = ActivateType.NOT_ACCESS,
                emailId = null,
                phoneNumberId = phone.phoneId
            )
        }
    }

    fun toUser(): User {
        return User.of(
            this.userId,
            this.userFirstName,
            this.userLastName,
            this.birth,
            Image.of(this.pictureUrl, 0),
            Image.of(this.backgroundPictureUrl, 0),
            this.type
        )
    }

    fun updateUserPictureUrl(media: Media) {
        this.pictureUrl = media.url
    }

    fun updateUserName(userName: UserName) {
        this.userFirstName = userName.firstName
        this.userLastName = userName.lastName
    }

    fun updateBirth(birth: String) {
        this.birth = birth
    }

    fun updateDelete() {
        this.type = ActivateType.DELETE
    }

    fun updateAccess() {
        this.type = ActivateType.ACCESS
    }

    fun id(): String {
        return this.userId
    }
}
