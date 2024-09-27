package org.chewing.v1.util.zookeeper

import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Component

@Component
class ShutdownRunner(
    private val serviceRegistry: ServiceRegistry
) {

    @PreDestroy
    fun shutdown() {
        // Shutdown logic
    }
}