package com.suni.judiciouspassion.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.util.*;

@Slf4j
@Service
@PropertySource("classpath:application-private.properties")
public class S3ServiceImpl implements S3Service {

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    @Autowired
    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public Mono<String> uploadObject(FilePart filePart) {
        String key = UUID.randomUUID().toString() + "-" + filePart.filename();
        String bucket = bucketName;

        // 1. Create multipart upload request
        CreateMultipartUploadRequest createRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        return DataBufferUtils.join(filePart.content()).flatMap(dataBuffer -> {
            try {
                byte[] fileContent = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(fileContent);
                DataBufferUtils.release(dataBuffer);

                String filename = UUID.randomUUID() + "-" + filePart.filename();
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(filePart.headers().getContentType().toString());
                metadata.setContentLength(fileContent.length);

                InputStream inputStream = new ByteArrayInputStream(fileContent);

                PutObjectRequest request = new PutObjectRequest(bucketName, filename, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
                // S3 업로드
                amazonS3.putObject(request);

                return Mono.just(amazonS3.getUrl(bucketName, filename).toString());
            } catch (Exception e){
                return Mono.error(e);
            }
        });
    }
}
