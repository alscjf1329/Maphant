package com.tovelop.maphant.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.tovelop.maphant.dto.UploadLogDTO
import com.tovelop.maphant.utils.UploadUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.util.*


@Service
class AwsS3Service(
    private val amazonS3Client: AmazonS3Client,
    private val uploadUtils: UploadUtils) {

    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket:String

    //파일을 aws 보내고 url 받기
    fun uploadFiles(files:List<MultipartFile>): List<String> {
        var imageUrls = ArrayList<String>()
        for (file in files) {
            val originalFileName: String = getRandom8CharsFromUUID() + "-" + file.originalFilename

            if(uploadUtils.isNotImageFile(originalFileName))
                throw IllegalArgumentException("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.");

            val objectMetadata = ObjectMetadata()
            objectMetadata.setContentLength(file.getSize())
            objectMetadata.setContentType(file.getContentType())

            try {
                val inputStream:InputStream = file.inputStream
                amazonS3Client.putObject(bucket,originalFileName, inputStream,objectMetadata);
                val uploadFileUrl = amazonS3Client.getUrl(bucket, originalFileName).toString();
                imageUrls.add(uploadFileUrl)
            }catch (e: IOException) {
                e.printStackTrace();
            }
        }
        return imageUrls
    }

    fun uploadFile(image: MultipartFile): String {
        lateinit var imageUrl: String
        val originalFileName: String = getRandom8CharsFromUUID() + "-" + image.originalFilename
        if (uploadUtils.isNotImageFile(originalFileName))
            throw IllegalArgumentException("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.");
        val objectMetadata = ObjectMetadata()
        objectMetadata.setContentLength(image.getSize())
        objectMetadata.setContentType(image.getContentType())
        try {
            val inputStream: InputStream = image.inputStream
            amazonS3Client.putObject(bucket, originalFileName, inputStream, objectMetadata);
            imageUrl = amazonS3Client.getUrl(bucket, originalFileName).toString();
        } catch (e: IOException) {
            e.printStackTrace();
        }
        return imageUrl
    }

    fun getRandom8CharsFromUUID(): String {
        return UUID.randomUUID().toString().substring(0, 8)
    }


}