package example.fcmdemo.fcm

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import example.fcmdemo.logger
import example.fcmdemo.user.UserRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class FirebaseCloudMessageService(
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
) {
    val log = logger()
    companion object {
        const val API_URL = "https://fcm.googleapis.com/v1/projects/test-maoman/messages:send"
    }

    private fun getAccessToken(): String {
        val firebaseConfigPath = "firebase/firebase_service_key.json"
        val credentials = GoogleCredentials
            .fromStream(ClassPathResource(firebaseConfigPath).inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform"))
        credentials.refreshIfExpired()
        return credentials.accessToken.tokenValue
    }

    fun sendDirectTo(targetToken: String, title: String, body: String) {
        val message = makeMessage(targetToken, title, body)

        val client = OkHttpClient()
        val requestBody = message
            .toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url(API_URL)
            .post(requestBody)
            .addHeader("Authorization", "Bearer ${getAccessToken()}")
            .build()
        val response = client.newCall(request).execute()

        println(response.body!!.string())
    }

    fun sendTopicTo(topicToken: String, title: String, body: String) {
        sendDirectTo(topicToken, title, body)
    }

    @Async
    fun sendAllUsers(title: String, body: String) {
        userRepository.findAll()
            .forEach {
                log.info("Sending to ${it.fcmToken}")
                sendDirectTo(it.fcmToken, title, body)
            }
    }

    private fun makeMessage(targetToken: String, title: String, body: String): String {
        val notification = Notification(title = title, body = body)
        val message = Message(token = targetToken, notification = notification)
        return objectMapper.writeValueAsString(FcmMessage(message = message))
    }
}