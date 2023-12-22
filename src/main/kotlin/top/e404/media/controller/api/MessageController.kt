package top.e404.media.controller.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.e404.media.service.FileService
import top.e404.media.service.MessageService

@RestController
@RequestMapping("/api/message")
class MessageController {
    @set:Autowired
    lateinit var messageService: MessageService

    @set:Autowired
    lateinit var fileService: FileService

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String) = ResponseEntity.ok(messageService.getById(id))

    // @PutMapping
    // fun add(chain: List<Message>): ResponseEntity<MessageData> {
    //     require(chain.filterIsInstance<BinaryMessage>().all { fileService.exists(it.id) })
    //     return ResponseEntity.ok(messageService.save(chain))
    // }
}