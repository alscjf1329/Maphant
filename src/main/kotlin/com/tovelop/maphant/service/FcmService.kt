package com.tovelop.maphant.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
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

        for (token in tokens!!) {
            val messageBuilder = Message.builder()
                .setToken(token)
                .setNotification(
                    com.google.firebase.messaging.Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )

            if(fcmMessageDTO.etc != null) {
                for (key in fcmMessageDTO.etc.keys) {
                    messageBuilder.putData(key, fcmMessageDTO.etc[key])
                }
            }

            val message = messageBuilder.build()
            FirebaseMessaging.getInstance().send(message)
        }

        notificationService.createNotification(fcmMessageDTO)
    }

    fun saveFcmToken(userId: Int, token: String) {
        fcmMapper.saveFcmToken(userId, token)
    }

    fun send(messageDTO: FcmMessageDTO) {
        val tokens = fcmMapper.selectTokenById(messageDTO.userId)
        if(tokens.isEmpty()) return
        messageDTO.setTokens(tokens)
        sendByTokens(messageDTO)
    }
}
