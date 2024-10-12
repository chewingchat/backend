package org.chewing.v1.model.chat.message

import org.chewing.v1.model.media.Media
import java.time.LocalDateTime

class ChatFileMessage private constructor(
    val medias: List<Media>,
    val senderId: String,
    val sendTime: LocalDateTime,
    val seqNumber: Int,
    val page: Int
): ChatMessage1() {
}