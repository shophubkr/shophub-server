package kr.co.shophub.shophub.global.file.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import kr.co.shophub.shophub.global.file.FileExtension
import kr.co.shophub.shophub.global.file.dto.UploadFileResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.util.*

@Service
class FileService(
    private val amazonS3Client: AmazonS3Client
) {

    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String
    @Value("\${cloud.aws.region.static}")
    lateinit var region: String

    fun upload(files: MultipartFile, directoryName: String): UploadFileResponse {
        val fileName = files.originalFilename ?: throw IllegalArgumentException("올바른 파일 이름 형식이 아닙니다.")
        validateExtension(fileName)

        val createFileName = createFileName(fileName, directoryName)
        val objMeta = ObjectMetadata()
        val bytes = IOUtils.toByteArray(files.inputStream)
        objMeta.contentLength = bytes.size.toLong()
        val byteArrayIs = ByteArrayInputStream(bytes)

        amazonS3Client.putObject(
            PutObjectRequest(bucket, createFileName, byteArrayIs, objMeta)
            .withCannedAcl(CannedAccessControlList.PublicRead))

        return UploadFileResponse(
            fileUrl = amazonS3Client.getUrl(bucket, createFileName).toString()
        )
    }

    fun delete(fileUrl: String) {
        val objectKey = fileUrl.split("$bucket.s3.$region.amazonaws.com/").last()
        amazonS3Client.deleteObject(DeleteObjectRequest(bucket, objectKey))
    }

    private fun createFileName(fileName: String, directoryName: String): String {
        return "$directoryName/${UUID.randomUUID()}$fileName"
    }

    private fun validateExtension(fileName: String) {
        if (FileExtension.validateExtension(fileName)) {
            throw IllegalArgumentException("올바른 파일 확장자 형식이 아닙니다. (jpg, jpeg, png)")
        }
    }
}