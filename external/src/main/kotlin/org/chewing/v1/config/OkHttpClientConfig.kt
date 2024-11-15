package org.chewing.v1.config

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test")
class OkHttpClientConfig {

    @Bean
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder().build()
}
