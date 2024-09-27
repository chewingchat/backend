package org.chewing.v1.config

import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.RetryOneTime
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ZookeeperConfig {

    // 분산 서비스 등록 및 관리?

    @Bean
    fun curatorFramework(): CuratorFramework {
        return CuratorFrameworkFactory.newClient("localhost:2181", RetryOneTime(5000))
    }
}