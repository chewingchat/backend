package org.chewing.v1.util.zookeeper

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Component
class StartupRunner(
    private val serviceRegistry: ServiceRegistry
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // Startup logic
    }
}