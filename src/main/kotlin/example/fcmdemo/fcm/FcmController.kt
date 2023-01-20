package example.fcmdemo.fcm

import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class FcmController(private val firebaseCloudMessageService: FirebaseCloudMessageService) {

    @PostMapping("/send")
    fun send(@RequestBody messageDto: MessageDto): MessageDto {
        firebaseCloudMessageService.sendDirectTo(
            messageDto.targetToken,
            messageDto.title,
            messageDto.body
        )
        return messageDto
    }

    @PostMapping("/sendAll")
    fun sendAll(@RequestBody messageDto: MessageDto): MessageDto {
        firebaseCloudMessageService.sendAllUsers(
            messageDto.title,
            messageDto.body
        )
        return messageDto
    }
}