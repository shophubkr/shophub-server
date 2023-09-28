package kr.co.shophub.shophub.global.file

enum class FileExtension(val extension: String) {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png");

    companion object {
        fun validateExtension(fileName: String): Boolean {
            return values().any { fileName.endsWith(".$it.extension", ignoreCase = true) }
        }
    }
}