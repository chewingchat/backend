package org.chewing.v1.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MainDataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "chewing.datasource.main")
    fun mainHikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    fun mainDataSource(@Qualifier("mainHikariConfig") config: HikariConfig): HikariDataSource {
        println("Test JDBC URL: " + config.jdbcUrl)
        return HikariDataSource(config)
    }
}