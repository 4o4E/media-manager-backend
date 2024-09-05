package top.e404.media.module.media.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import top.e404.media.module.common.advice.LogAccess
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.exception.NotFoundException
import top.e404.media.module.media.service.FileService

@RestController
@RequestMapping("/api/file")
@Tag(name = "静态文件接口")
class FileController {
    @set:Autowired
    lateinit var fileService: FileService

    @LogAccess
    @GetMapping("/{id}", produces = ["application/octet-stream"])
    @Operation(summary = "通过文件id获取文件")
    fun getById(@PathVariable @Parameter(description = "文件id") id: String) =
        fileService.getFileResourceBySha(id) ?: throw NotFoundException()

    @LogAccess
    @PostMapping("/{sha}/exists")
    @RequirePerm("file:exists")
    @Operation(summary = "通过文件id检查文件是否存在")
    @ApiResponse(description = "若文件不存在则404")
    fun exists(@PathVariable sha: String) {
        if (!fileService.exists(sha)) throw NotFoundException()
    }

    @LogAccess
    @PutMapping("")
    @RequirePerm("file:upload")
    @Operation(summary = "上传文件")
    @ApiResponse(description = "文件id")
    fun upload(@RequestPart file: MultipartFile): String = fileService.upload(file)
}