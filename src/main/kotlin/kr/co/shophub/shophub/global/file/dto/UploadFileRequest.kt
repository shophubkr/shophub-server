package kr.co.shophub.shophub.global.file.dto

import org.springframework.web.multipart.MultipartFile

data class UploadFileRequest(
    val files: MultipartFile,
    val directoryName: String,
) {
}