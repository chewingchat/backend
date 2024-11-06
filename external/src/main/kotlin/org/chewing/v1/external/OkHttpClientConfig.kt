package org.chewing.v1.external

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test")
class OkHttpClientConfig {

    @Bean
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}
