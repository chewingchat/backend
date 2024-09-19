package org.chewing.v1.model.comment

import org.chewing.v1.model.User

class UserComment(
    val comment: Comment,
    val writer: User
) {
    companion object {
        fun of(comment: Comment, writer: User): UserComment {
            return UserComment(comment, writer)
        }
    }
}