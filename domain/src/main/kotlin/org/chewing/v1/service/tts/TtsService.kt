package org.chewing.v1.service.tts

import org.chewing.v1.external.ExternalFileClient
import org.chewing.v1.external.ExternalTtsClient
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media
import org.chewing.v1.model.media.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class TtsService(
    private val externalTtsClient: ExternalTtsClient,
    private val externalFileClient: ExternalFileClient
) {
    suspend fun generateTtsFile(text: String, speaker: String): Media {
        return externalTtsClient.generateTts(text, speaker)
    }
    suspend fun uploadRecording(file: MultipartFile, userId: String): Media {
        // 파일 데이터 생성
        val fileData = FileData.of(
            inputStream = file.inputStream,
            contentType = MediaType.fromType(file.contentType!!) ?: throw IllegalArgumentException("Unsupported file type"),
            fileName = file.originalFilename!!,
            size = file.size
        )

        // 업로드할 Media 생성
        val media = Media.upload(
            baseUrl = "https://kr.object.ncloudstorage.com",
            buckName = "chewing-bucket",
            category = FileCategory.TTS,
            userId = userId,
            fileName = file.originalFilename!!,
            type = fileData.contentType
        )

        // NCP Storage에 업로드
        externalFileClient.uploadFile(fileData, media)

        return media
    }
}
