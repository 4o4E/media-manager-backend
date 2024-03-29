package top.e404.media

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["top.e404.media"])
class App

fun main(vararg args: String) {
    runApplication<App>(*args)
}
