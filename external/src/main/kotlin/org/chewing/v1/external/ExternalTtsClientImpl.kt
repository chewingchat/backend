package org.chewing.v1.external

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*

@Component
class ExternalTtsClientImpl : ExternalTtsClient {

    @Value("\${NCP_TTS_API_URL}")
    private lateinit var apiUrl: String

    @Value("\${NCP_ACCESS_KEY}")
    private lateinit var clientId: String

    @Value("\${NCP_SECRET_KEY}")
    private lateinit var clientSecret: String

    override fun generateTts(text: String, speaker: String): File {
        try {
            // 환경 변수 값 확인
            println("Client ID: $clientId") // 디버깅용 출력
            println("Client Secret: $clientSecret") // 디버깅용 출력
            println("API URL: $apiUrl") // 디버깅용 출력

            val encodedText = URLEncoder.encode(text, "UTF-8")
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            connection.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId)
            connection.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret)
            connection.doOutput = true



            val postParams = "speaker=$speaker&volume=0&speed=0&pitch=0&format=mp3&text=$encodedText"
            println("Post Params: $postParams") // 요청 파라미터 디버깅
            DataOutputStream(connection.outputStream).use { outputStream ->
                outputStream.writeBytes(postParams)
                outputStream.flush()
            }

            val responseCode = connection.responseCode
            return if (responseCode == 200) {
                val tempName = "${Date().time}.mp3"
                val file = File(tempName)
                file.createNewFile()

                connection.inputStream.use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        val buffer = ByteArray(1024)
                        var bytesRead: Int
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }
                    }
                }
                file
            } else {
                val errorMessage = connection.errorStream.bufferedReader().use { it.readText() }
                throw IOException("TTS API Error: $errorMessage")
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to generate TTS", e)
        }
    }
}
