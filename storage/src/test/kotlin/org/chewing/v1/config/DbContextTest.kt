package org.chewing.v1.config

import org.chewing.v1.DbTestApplication
import org.chewing.v1.repository.support.TestDataGenerator
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@ContextConfiguration(classes = [DbTestApplication::class])
@ActiveProfiles("test")
@Import(MainDataSourceConfig::class, TestDataGenerator::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = ["spring.config.location=classpath:db.yml"])
abstract class DbContextTest