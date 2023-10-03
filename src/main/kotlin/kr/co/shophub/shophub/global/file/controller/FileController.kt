package kr.co.shophub.shophub.global.file.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.file.dto.UploadFileRequest
import kr.co.shophub.shophub.global.file.dto.UploadFileResponse
import kr.co.shophub.shophub.global.file.service.FileService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/file")
class FileController(
    private val fileService: FileService,
) {

    @PostMapping
    fun uploadFile(
        @ModelAttribute uploadFileRequest: UploadFileRequest,
    ): CommonResponse<UploadFileResponse> {

        return fileService.upload(
            files = uploadFileRequest.files,
            directoryName = uploadFileRequest.directoryName
        ).let { CommonResponse(it) }
    }

    @DeleteMapping
    fun deleteFile(
        @RequestParam fileUrl: String,
    ): CommonResponse<EmptyDto> {

        return fileService.delete(
            fileUrl = fileUrl
        ).let { CommonResponse.EMPTY }
    }

}