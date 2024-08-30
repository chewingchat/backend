package org.chewing.v1.model

import java.time.LocalDateTime
import java.util.*

class User private constructor(
    val userId: UserId,
    val name: String,
    val birth: String,
    val statusMessage: String,
    val image: Image
) {
    companion object {
        fun withId(
            userId: String, name: String, birth: String, statusMessage: String, image: Image
        ): User {
            return User(
                userId = UserId.of(userId),
                name = name,
                birth = birth,
                image = image,
                statusMessage = statusMessage
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

    fun updateImage(fileName: String): User {
        return User(
            userId,
            name,
            birth,
            statusMessage,
            Image.upload(Image.ImageCategory.USER_PROFILE, userId.value(), fileName)
        )
    }

    fun updateStatusMessage(statusMessage: String): User {
        return User(
            userId,
            name,
            birth,
            statusMessage,
            image
        )
    }
}
