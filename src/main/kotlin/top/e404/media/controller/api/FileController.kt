package top.e404.media.controller.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import top.e404.media.annontation.CheckPerm
import top.e404.media.service.FileService

@RestController
@RequestMapping("/api/file")
class FileController {
    @Autowired
    lateinit var fileService: FileService

    @GetMapping("/{sha}")
    @CheckPerm("file:get")
    fun getById(@PathVariable sha: String): ResponseEntity<FileSystemResource> {
        val resource = fileService.getFileResourceBySha(sha) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok().body(resource)
    }

    @PostMapping("/{sha}/exists")
    @CheckPerm("file:exists")
    fun exists(@PathVariable sha: String): ResponseEntity<Void> {
        if (fileService.checkUpload(sha)) {
            return ResponseEntity.ok(null)
        }
        return ResponseEntity.notFound().build()
    }

    @PutMapping("/")
    @CheckPerm("file:upload")
    fun upload(@RequestPart file: ByteArray): ResponseEntity<String> {
        println(file)
        return ResponseEntity.ok(fileService.upload(file))
    }
}