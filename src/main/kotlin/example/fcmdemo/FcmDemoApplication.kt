package example.fcmdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class FcmDemoApplication

fun main(args: Array<String>) {
	runApplication<FcmDemoApplication>(*args)
}
