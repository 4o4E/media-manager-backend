package top.e404.media.controller.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import top.e404.media.annontation.CheckPerm
import top.e404.media.service.FileService

@RestController
@RequestMapping("/api/file")
@Tag(name = "file", description = "静态文件接口")
class FileController {
    @Autowired
    lateinit var fileService: FileService

    @GetMapping("/{id}")
    @CheckPerm("file:get")
    @Operation(summary = "通过文件id获取文件")
    fun getById(@PathVariable @Parameter(description = "文件id") id: String): ResponseEntity<FileSystemResource> {
        val resource = fileService.getFileResourceBySha(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok().body(resource)
    }

    @PostMapping("/{sha}/exists")
    @CheckPerm("file:exists")
    @Operation(summary = "通过文件id检查文件是否存在")
    @ApiResponse(description = "若文件不存在则404")
    fun exists(@PathVariable sha: String): ResponseEntity<Void> =
        if (fileService.checkUpload(sha)) ResponseEntity.ok(null)
        else ResponseEntity.notFound().build()

    @PutMapping("/")
    @CheckPerm("file:upload")
    @Operation(summary = "上传文件")
    @ApiResponse(description = "文件id")
    fun upload(@RequestPart file: ByteArray): ResponseEntity<String> = ResponseEntity.ok(fileService.upload(file))
}