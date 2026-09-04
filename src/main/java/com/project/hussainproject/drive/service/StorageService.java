package com.project.hussainproject.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final S3Presigner presigner;

    @Value("${supabase.s3.bucket-name}")
    private String bucketName;

    // 1. Upload ke liye URL (Pehle se tha)
    public String generateUploadUrl(String storageKey, String mimeType) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .contentType(mimeType)
                .build();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(r -> r
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(objectRequest)
        );

        return presignedRequest.url().toString();
    }

    // 2. YEH NAYA METHOD HAI - File ko view/download karne ke liye URL
    public String generateDownloadUrl(String storageKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(r -> r
                .signatureDuration(Duration.ofHours(1)) // URL 1 ghante tak valid rahega
                .getObjectRequest(getObjectRequest)
        );

        return presignedRequest.url().toString();
    }
}