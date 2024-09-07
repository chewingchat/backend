package org.chewing.v1.model

import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.media.Image

class User private constructor(
    val userId: UserId,
    val name: UserName,
    val birth: String,
    val image: Image,
    val status: UserStatus,
    val backgroundImage: Image
) {
    companion object {
        fun withId(
            userId: String,
            firstName: String,
            lastName: String,
            birth: String,
            image: Image,
            backgroundImage: Image,
            emoticon: Emoticon,
            statusMessage: String
        ): User {
            return User(
                userId = UserId.of(userId),
                status = UserStatus.of(statusMessage, emoticon),
                birth = birth,
                image = image,
                backgroundImage = backgroundImage,
                name = UserName.of(firstName, lastName)
            )
        }

        fun empty(): User {
            return User(
                userId = UserId.empty(),
                status = UserStatus.of("", Emoticon.empty()),
                birth = "",
                image = Image.empty(),
                backgroundImage = Image.empty(),
                name = UserName.empty()

            )
        }
    }

    class UserId private constructor(private val userId: String) {
        fun value(): String {
            return userId
        }

        companion object {
            fun empty(): UserId {
                return UserId("")
            }

            fun of(id: String): UserId {
                return UserId(id)
            }
        }
    }

    class UserName private constructor(private val firstName: String, private val lastName: String) {
        fun firstName(): String {
            return firstName
        }

        fun lastName(): String {
            return lastName
        }

        companion object {
            fun of(firstName: String, lastName: String): UserName {
                return UserName(firstName, lastName)
            }

            fun empty(): UserName = UserName("", "")
        }
    }

    class UserStatus private constructor(val statusMessage: String, val emoticon: Emoticon) {
        companion object {
            fun of(statusMessage: String, emoticon: Emoticon): UserStatus {
                return UserStatus(statusMessage, emoticon)
            }
        }
    }

    fun updateImage(fileName: String): User {
        return User(
            userId,
            name,
            birth,
            Image.upload(Image.ImageCategory.USER_PROFILE, userId.value(), fileName),
            status,
            backgroundImage
        )
    }

    fun updateStatus(status: UserStatus): User {
        return User(
            userId,
            name,
            birth,
            image,
            status,
            backgroundImage
        )
    }
}
