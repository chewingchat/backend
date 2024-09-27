package org.chewing.v1.util.zookeeper

import org.springframework.stereotype.Component
import org.apache.curator.framework.CuratorFramework

@Component
class ServiceRegistry(private val client: CuratorFramework) {

    companion object {
        private const val BASE_PATH = "/services"
    }

    fun registerService(serviceName: String, serviceAddress: String, servicePort: Int) {
        val path = "$BASE_PATH/$serviceName/$serviceAddress:$servicePort"
        client.create().creatingParentsIfNeeded().forPath(path)
    }

    fun unregisterService(serviceName: String, serviceAddress: String, servicePort: Int) {
        val path = "$BASE_PATH/$serviceName/$serviceAddress:$servicePort"
        client.delete().forPath(path)
    }
}