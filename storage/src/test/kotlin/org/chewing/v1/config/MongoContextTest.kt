package org.chewing.v1.config

import org.chewing.v1.DbTestApplication
import org.chewing.v1.repository.support.MongoDataGenerator
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@DataMongoTest
@ContextConfiguration(classes = [DbTestApplication::class])
@ActiveProfiles("test")
@Import(MongoDataGenerator::class)
@TestPropertySource(properties = ["spring.config.location=classpath:db.yml"])
abstract class MongoContextTest