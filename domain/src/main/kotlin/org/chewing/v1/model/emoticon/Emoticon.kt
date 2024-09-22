package org.chewing.v1.model.emoticon

import org.chewing.v1.model.media.Image
import org.chewing.v1.model.media.Media

class Emoticon private constructor(
    val emoticonId: String,
    val name: String,
    val media: Media,
) {
    companion object {
        fun empty(): Emoticon {
            return Emoticon(
                emoticonId = "",
                name = "",
                media = Image.empty(),
            )
        }

        fun of(
            id: String,
            name: String,
            url: String,
        ): Emoticon {
            return Emoticon(
                emoticonId = id,
                name = name,
                media = Image.of(url, 0)
            )
        }
    }

    fun isEmpty(): Boolean {
        return emoticonId.isEmpty() && name.isEmpty() && media.isEmpty
    }
}