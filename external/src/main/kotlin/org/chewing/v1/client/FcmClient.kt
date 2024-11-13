package org.chewing.v1.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.RequestBody.Companion.toRequestBody
import org.chewing.v1.dto.FcmMessageDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class FcmClient(
    private val objectMapper: ObjectMapper,
    @Value("\${fcm.api.url}") private val url: String,
    private val client: OkHttpClient = OkHttpClient(),
) {
    fun sendMessage(message: FcmMessageDto) {
        val jsonMessage = objectMapper.writeValueAsString(message)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonMessage.toRequestBody(mediaType)
        val request: Request = Builder()
            .url(url)
            .post(requestBody)
            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
            .build()

        client.newCall(request).execute()
    }

    private fun getAccessToken(): String {
        val firebaseConfigPath = "firebase/firebase_service_key.json"
        val googleCredentials = GoogleCredentials
            .fromStream(ClassPathResource(firebaseConfigPath).inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform"))

        googleCredentials.refreshIfExpired()
        return googleCredentials.accessToken.tokenValue
    }
}
