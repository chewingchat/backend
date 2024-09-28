package org.chewing.v1.model.auth

class PushToken private constructor(
    val pushTokenId: String,
    val appToken: String,
    val device: Device
) {
    companion object {
        fun of(
            pushTokenId: String,
            appToken: String,
            provider: Provider,
            deviceId: String
        ): PushToken {
            return PushToken(
                pushTokenId = pushTokenId,
                appToken = appToken,
                device = Device.of(deviceId, provider)
            )
        }
    }

    class Device private constructor(
        val deviceId: String,
        val provider: Provider
    ) {
        companion object {
            fun of(
                deviceId: String,
                provider: Provider
            ): Device {
                return Device(
                    deviceId = deviceId,
                    provider = provider
                )
            }
        }
    }

    enum class Provider {
        FCM,
        APNS
    }
}