package org.chewing.v1.model.auth

class PushToken private constructor(
    val pushTokenId: String,
    val fcmToken: String, // 특정 앱이 API에 접근할 수 있는 권한을 갖고 있는지를 확인하는 데 사용
    val device: Device
) { // 푸시 알림: 애플리케이션 서버에서 앱을 사용하지 않은 상태에서도 사용자에게 메시지를 전송가능한 기술
    companion object {
        fun of(
            pushTokenId: String,
            fcmToken: String,
            provider: Provider,
            deviceId: String
        ): PushToken {
            return PushToken(
                pushTokenId = pushTokenId,
                fcmToken = fcmToken,
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
        ANDROID,
        IOS
    }
}
