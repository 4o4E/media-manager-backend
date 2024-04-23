package top.e404.media.module.common.service.system

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


interface MailService {
    /**
     * 发送邮件
     */
    fun sendMail(to: String, title: String, content: String)
}

@Service
class MailServiceImpl : MailService {
    @set:Autowired
    lateinit var javaMailSender: JavaMailSender

    @Value("\${spring.mail.username}")
    lateinit var from: String

    override fun sendMail(to: String, title: String, content: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.from = from
        message.text = content
        message.subject = title
        javaMailSender.send(message)
    }
}