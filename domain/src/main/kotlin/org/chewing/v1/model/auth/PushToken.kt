package org.chewing.v1.model.auth

class PushToken private constructor(
    val pushTokenId: String,
    val appToken: String, // 특정 앱이 API에 접근할 수 있는 권한을 갖고 있는지를 확인하는 데 사용
    val device: Device
) { // 푸시 알림: 애플리케이션 서버에서 앱을 사용하지 않은 상태에서도 사용자에게 메시지를 전송가능한 기술
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
        // FCM: google firebase에서 사용하는 클라우드 메시징 서비스
        // APNS: iOS 앱이 사용자의 기기로 알림을 보낼 수 있도록 합니다.

        // 무료로 메세지를 안정적으로 전송
        // 이 토큰으로 android, ios 각각 2개 디바이스 식별
        //  유저의 토큰 필드를 조회하여 프론트에 보내주는 방식




    }
}