package org.chewing.v1.dto.request

data class CommentRequest(
    val feedId: String,
    val comment: String
) {
}