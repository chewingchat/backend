package org.chewing.v1

import org.chewing.v1.config.TestSecurityConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
internal class ChewingApplicationTests: ContextTest() {

    @Test
    fun contextLoads() {
    }

}
