package com.tovelop.maphant.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.tovelop.maphant.dto.FcmMessageDTO
import com.tovelop.maphant.mapper.FcmMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FcmService(
    @Autowired private val notificationService: NotificationService,
    @Autowired private val fcmMapper: FcmMapper,
) {

    private fun sendByTokens(fcmMessageDTO: FcmMessageDTO) {
        val title = fcmMessageDTO.title
        val body = fcmMessageDTO.body
        val tokens = fcmMessageDTO.getTokens()

        val messageBuilder = MulticastMessage.builder()
            .addAllTokens(tokens)
            .setNotification(
                com.google.firebase.messaging.Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build()
            )

        if (fcmMessageDTO.etc != null) {
            for (key in fcmMessageDTO.etc.keys) {
                messageBuilder.putData(key, fcmMessageDTO.etc[key])
            }
        }

        val message = messageBuilder.build()
        try {
            FirebaseMessaging.getInstance().sendEachForMulticast(message)
        } catch (e: Exception) {
            System.err.println("FCM 전송 실패 : ${e.message}")
            e.printStackTrace()
        }
    }

    fun saveFcmToken(userId: Int, token: String) {
        fcmMapper.saveFcmToken(userId, token)
    }

    fun send(messageDTO: FcmMessageDTO) {
        notificationService.createNotification(messageDTO) // web에서도 알림 로그 저장가능하게

        val tokens = fcmMapper.selectTokenById(messageDTO.userId)
        if (tokens.isEmpty()) return

        messageDTO.setTokens(tokens)
        sendByTokens(messageDTO)
    }
}
