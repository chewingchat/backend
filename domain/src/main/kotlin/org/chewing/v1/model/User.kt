package org.chewing.v1.model

import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.media.Media


class User private constructor(
    val userId: UserId,
    val name: UserName,
    val birth: String,
    val image: Image,
    val status: UserStatus,
    val backgroundImage: Image
) {
    companion object {
        fun of(
            userId: String,
            firstName: String,
            lastName: String,
            birth: String,
            image: Image,
            backgroundImage: Image,
            statusId: String,
            emoticon: Emoticon,
            statusMessage: String
        ): User {
            return User(
                UserId.of(userId),
                status = UserStatus.of(statusId, statusMessage, emoticon),
                birth = birth,
                image = image,
                backgroundImage = backgroundImage,
                name = UserName.of(firstName, lastName)
            )
        }

        fun empty(): User {
            return User(
                userId = UserId.empty(),
                status = UserStatus.of("", "", Emoticon.empty()),
                birth = "",
                image = Image.empty(),
                backgroundImage = Image.empty(),
                name = UserName.empty()

            )
        }

        fun generate(birth: String, firstName: String, lastName: String): User {
            return User(
                userId = UserId.empty(),
                status = UserStatus.of("", "", Emoticon.empty()),
                birth = birth,
                image = Image.empty(),
                backgroundImage = Image.empty(),
                name = UserName.of(firstName, lastName)
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

    class UserStatus private constructor(val statusId: String, val statusMessage: String, val emoticon: Emoticon) {
        companion object {
            fun of(statusId: String, statusMessage: String, emoticon: Emoticon): UserStatus {
                return UserStatus(statusId, statusMessage, emoticon)
            }
        }
    }

    class UserBasicInfo private constructor(val name: UserName, val birth: String, val image: Image) {
        companion object {
            fun of(name: UserName, birth: String, image: Image): UserBasicInfo {
                return UserBasicInfo(name, birth, image)
            }
        }
    }

    fun updateImage(media: Media): User {
        return User(
            userId,
            name,
            birth,
            media as Image,
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

    fun updateUserId(userId: UserId): User {
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
