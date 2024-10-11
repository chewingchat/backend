package org.chewing.v1.model.chat

import org.chewing.v1.model.media.MediaType

data class MessageSendType(
    val mediaType: MediaType?,  // MediaType 필드
    val text: String? = null  // 메시지 텍스트 필드
)