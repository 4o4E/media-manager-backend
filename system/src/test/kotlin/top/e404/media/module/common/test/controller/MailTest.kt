package top.e404.media.module.common.test.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import top.e404.media.module.common.service.system.MailService

@SpringBootTest
class MailTest {
    @set:Autowired
    lateinit var mailService: MailService

    @Test
    fun testSend() {
        mailService.sendMail("869951226@qq.com", "test title", "test content")
    }
}