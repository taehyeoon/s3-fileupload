package io.cloudtype.springfileupload.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Log4j2
public class FileUploadService {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public FileUploadService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(String keyName, MultipartFile file) throws IOException {

        // ObjectMetadata 객체 생성
        ObjectMetadata metadata = new ObjectMetadata();
        // 파일의 콘텐츠 길이 설정
        metadata.setContentLength(file.getSize());

        // PutObjectRequest에 metadata 전달
        PutObjectResult putObjectResult = s3Client.putObject(bucketName, keyName, file.getInputStream(), metadata);
        log.info(putObjectResult.getMetadata().toString());
    }

    public S3Object getFile(String keyName) {
        try {
            return s3Client.getObject(bucketName, keyName);
        } catch (AmazonS3Exception e) {
            log.error(e);
            return null;
        }
    }
}
