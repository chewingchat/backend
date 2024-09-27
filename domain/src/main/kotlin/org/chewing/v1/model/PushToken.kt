package org.chewing.v1.model

class PushToken private constructor(
    val pushTokenId: String,
    val appToken: String,
    val device: Device
) {
    companion object {
        fun generate(
            appToken: String,
            provider: String,
            deviceId: String
        ): PushToken {
            return PushToken(
                pushTokenId = "",
                appToken = appToken,
                device = Device.of(deviceId, provider)
            )
        }

        fun of(
            pushTokenId: String,
            appToken: String,
            provider: String,
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
        val provider: String
    ) {
        companion object {
            fun of(
                deviceId: String,
                provider: String
            ): Device {
                return Device(
                    deviceId = deviceId,
                    provider = provider
                )
            }
        }
    }

    fun updatePushToken(pushToken: PushToken): PushToken {
        return PushToken(
            pushTokenId = pushTokenId,
            appToken = pushToken.appToken,
            device = device
        )
    }
}