package top.e404.media

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["top.e404.media"])
class MediaManagerApplication

fun main(vararg args: String) {
    runApplication<MediaManagerApplication>(*args)
}
