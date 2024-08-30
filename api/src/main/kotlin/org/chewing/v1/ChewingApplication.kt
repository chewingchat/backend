package org.chewing.v1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChewingApplication

fun main(args: Array<String>) {
    runApplication<ChewingApplication>(*args)
}
