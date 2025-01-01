package com.nerdery.ecommerce.service.aws;

import jakarta.persistence.criteria.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String BUCKET_NAME;

    public void listBuckets(){
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = s3Client.listBuckets();

        System.out.println("Buckets: ");
        listBucketsResponse.buckets().forEach(bucket -> System.out.println(bucket.name()));
    }

    public String uploadImage(String keyName, MultipartFile file) throws IOException {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(keyName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return "https://" + BUCKET_NAME + ".s3.amazonaws.com/" + keyName;
        } catch (SdkClientException | IOException e) {
            System.err.println("Error uploading file to S3: " + e.getMessage());
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}
