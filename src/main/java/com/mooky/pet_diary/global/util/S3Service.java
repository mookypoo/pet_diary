package com.mooky.pet_diary.global.util;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mooky.pet_diary.global.exception.InvalidArgumentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;


@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    private final String petProfile = "pet-profile";

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final List<String> allowedFileTypes = List.of(
        "image/jpeg", "image/jpg", "image/png", "image/webp");

    // determines the bucket key
    private final List<String> allowedTypes = List.of("pet-profile");
    // for meta data purposes
    // need user-id
    // file original name
    // 
    public String createPresignedUrl(S3UploadReq req, String type, Long userId) {

        if (!this.allowedFileTypes.contains(req.getFileType())) {
            throw InvalidArgumentException.type(
                        "filetype", 
                        "needs to be one of the following values: image/jpeg, image/jpg, image/png, or image/webp",
                        req.getFileType());
        }

        if (!this.allowedTypes.contains(type)) {
            throw InvalidArgumentException.type(
                    "type",
                    "type query parameter is invalid",
                    type);
        }

        try {
            String objectKey = String.format("%s/%s.%s",
                    type,
                    UUID.randomUUID().toString(), 
                    this.getFileExtension(req.getFileName()));

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(this.bucketName)
                    .key(objectKey)
                    .contentType(req.getFileType())
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = this.s3Presigner.presignPutObject(presignRequest);

            return presignedRequest.url().toString();

        } catch (Exception ex) {
        
            log.debug("create presigned url exception: {}", ex.getMessage());
        }
        return null;
    }
    
    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}


