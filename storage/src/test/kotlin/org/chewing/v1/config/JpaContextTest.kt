package org.chewing.v1.config

import org.chewing.v1.DbTestApplication
import org.chewing.v1.repository.support.JpaDataGenerator
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(classes = [DbTestApplication::class])
@ActiveProfiles("test")
@Import(CoreDataSourceConfig::class, JpaDataGenerator::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class JpaContextTest
