package kr.co.shophub.shophub.global.s3

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.util.*

@Component
class AwsS3Uploader(
    private val amazonS3Client: AmazonS3Client
) {

    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    fun upload(file: MultipartFile): String {
        val fileName = file.originalFilename ?: throw IllegalArgumentException("올바른 파일 이름 형식이 아닙니다.")
        validateExtension(fileName)

        val createFileName = createFileName(fileName)
        val objMeta = ObjectMetadata()
        val bytes = IOUtils.toByteArray(file.inputStream)
        objMeta.contentLength = bytes.size.toLong()
        val byteArrayIs = ByteArrayInputStream(bytes)

        amazonS3Client.putObject(
            PutObjectRequest(bucket, createFileName, byteArrayIs, objMeta)
            .withCannedAcl(CannedAccessControlList.PublicRead))

        return amazonS3Client.getUrl(bucket, fileName).toString()
    }

    private fun createFileName(fileName: String): String {
        return (UUID.randomUUID()).toString() + fileName
    }

    private fun validateExtension(fileName: String) {
        if (FileExtension.validateExtension(fileName)) {
            throw IllegalArgumentException("올바른 파일 확장자 형식이 아닙니다. (jpg, jpeg, png)")
        }
    }
}