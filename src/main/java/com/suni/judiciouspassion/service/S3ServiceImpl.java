package com.suni.judiciouspassion.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.suni.judiciouspassion.entity.saunter.SaunterMedias;
import com.suni.judiciouspassion.entity.taste.TasteImages;
import com.suni.judiciouspassion.entity.tour.TourImages;
import com.suni.judiciouspassion.repository.SaunterMediasRepository;
import com.suni.judiciouspassion.repository.TasteImagesRepository;
import com.suni.judiciouspassion.repository.TourImagesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@PropertySource("classpath:application-private.properties")
public class S3ServiceImpl implements S3Service {

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    private final AmazonS3 amazonS3;
    private final TasteImagesRepository tasteImagesRepository;
    private final TourImagesRepository tourImagesRepository;
    private final SaunterMediasRepository saunterMediasRepository;

    @Autowired
    public S3ServiceImpl(AmazonS3 amazonS3, TasteImagesRepository tasteImagesRepository, TourImagesRepository tourImagesRepository, SaunterMediasRepository saunterMediasRepository) {
        this.amazonS3 = amazonS3;
        this.tasteImagesRepository = tasteImagesRepository;
        this.tourImagesRepository = tourImagesRepository;
        this.saunterMediasRepository = saunterMediasRepository;
    }

    @Override
    public Mono<String> uploadObject(Long id, FilePart filePart, String type) {
        String key = UUID.randomUUID() + "-" + filePart.filename();
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

                return TableUploader(type, filename, id);
            } catch (Exception e) {
                return Mono.error(e);
            }
        });
    }

    private Mono<String> TableUploader(String type, String filename, Long id) {
        String mediaUrl = amazonS3.getUrl(bucketName, filename).toString();

        return switch (type) {
            case "taste" -> {
                TasteImages tasteImages = TasteImages.builder()
                        .tasteId(id)
                        .imageUrl(mediaUrl)
                        .build();

                // 체인 반환
                yield tasteImagesRepository.save(tasteImages)
                        .thenReturn(mediaUrl);
            }
            case "saunter" -> {
                SaunterMedias saunterMedias = SaunterMedias.builder()
                        .saunterId(id)
                        .mediaUrl(mediaUrl)
                        .build();

                // 체인 반환
                yield saunterMediasRepository.save(saunterMedias)
                        .thenReturn(mediaUrl);
            }

            case "tour" -> {
                TourImages tourImages = TourImages.builder()
                        .tourId(id)
                        .imageUrl(mediaUrl)
                        .build();

                // 체인 반환
                yield tourImagesRepository.save(tourImages)
                        .thenReturn(mediaUrl);
            }
            default -> Mono.error(new IllegalArgumentException("Invalid type: " + type));
        };
    }
}