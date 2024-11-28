package org.chewing.v1.controller.tts

import org.chewing.v1.model.media.Media
import org.springframework.http.MediaType
import org.chewing.v1.service.tts.TtsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files

@RestController
class TtsController(
    private val ttsService: TtsService
) {
    @GetMapping("/api/tts")
    suspend fun generateTts(
        @RequestParam text: String,
        @RequestParam(defaultValue = "nara") speaker: String
    ): ResponseEntity<String> {
        val media = ttsService.generateTtsFile(text, speaker)
        return ResponseEntity.ok("File uploaded successfully: ${media.url}")
    }
    // 녹음 파일 업로드(피그마)
    @PostMapping("/api/tts/upload")
    suspend fun uploadRecording(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("userId") userId: String
    ): ResponseEntity<Media> {
        val media = ttsService.uploadRecording(file, userId)
        return ResponseEntity.ok(media)
    }
}
