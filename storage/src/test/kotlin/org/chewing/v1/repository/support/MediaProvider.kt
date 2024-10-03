package org.chewing.v1.repository.support

import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.Media
import org.chewing.v1.model.media.MediaType
import org.springframework.stereotype.Component

@Component
object MediaProvider {
    fun buildProfileContent(): Media {
        return Media.of(FileCategory.PROFILE, "www.example.com", 0, MediaType.IMAGE_PNG)
    }
    fun buildBackgroundContent(): Media {
        return Media.of(FileCategory.BACKGROUND, "www.example.com", 0, MediaType.IMAGE_PNG)
    }

    fun buildTTSContent(): Media {
        return Media.of(FileCategory.TTS, "www.example.com", 0, MediaType.AUDIO_MP3)
    }
}