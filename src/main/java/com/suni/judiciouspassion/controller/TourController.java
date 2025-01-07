package com.suni.judiciouspassion.controller;

import com.suni.judiciouspassion.dto.TourDTO;
import com.suni.judiciouspassion.entity.tour.Tour;
import com.suni.judiciouspassion.service.S3ServiceImpl;
import com.suni.judiciouspassion.service.TourService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("tour")
public class TourController {

    private final TourService tourService;
    private final S3ServiceImpl s3Service;

    @Autowired
    public TourController(TourService tourService, S3ServiceImpl s3Service) {
        this.tourService = tourService;
        this.s3Service = s3Service;
    }

    @PostMapping("/create")
    public Mono<Tour> tourCreate(@RequestBody TourDTO tourDTO) {
        return tourService.createTour(tourDTO);
    }

    @GetMapping("/get/{insertId}")
    public Flux<Tour> getTourById(@PathVariable String insertId) {
        return tourService.getTourById(insertId);
    }

    @GetMapping("/list/{page}/{size}")
    public Mono<List<TourDTO>> getTourList(@PathVariable String page, @PathVariable String size) {
        return tourService.getAllTours(Integer.parseInt(page), Integer.parseInt(size)); // Flux 반환
    }

    @PostMapping("/like/add")
    public Mono<Long> addLike(@RequestBody Integer tourId) {
        return tourService.addLike(tourId); // Flux 반환
    }

    @PostMapping("/like/minus")
    public Mono<Long> minusLike(@RequestBody Integer tourId) {
        return tourService.minusLike(tourId); // Flux 반환
    }

    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> uploadFile(@RequestPart("id") String id, @RequestPart("file") Mono<FilePart> filePartMono
    ,@RequestPart("type") String type) {
        return filePartMono
                .flatMap(filePart -> {
                    String filename = filePart.filename();
                    String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

                    // 확장자 확인
                    List<String> allowedExtensions = List.of("png", "jpg", "jpeg", "gif", "webp", "heic", "bmp");
                    if (!allowedExtensions.contains(extension)) {
                        return Mono.error(new IllegalArgumentException("Unsupported file type: " + extension));
                    }

                    // MIME 타입 확인
                    if (!isValidImage(filePart)) {
                        return Mono.error(new IllegalArgumentException("Invalid content type: " + filePart.headers().getContentType()));
                    }

                    // S3 업로드
                    return s3Service.uploadObject(Integer.parseInt(id), filePart, type)
                            .onErrorMap(e -> {
                                log.error("S3 upload failed for file: {}", filePart.filename(), e);
                                return new RuntimeException("S3 upload failed: " + e.getMessage());
                            });
                })
                .map(response -> ResponseEntity.ok("File uploaded successfully: " + response))
                .onErrorResume(e -> {
                    log.error("Upload error: {}", e.getMessage(), e);
                    return Mono.just(ResponseEntity.badRequest().body("Upload failed: " + e.getMessage()));
                });
    }

    private boolean isValidImage(FilePart filePart) {
        return filePart.headers().getContentType() != null &&
                Objects.requireNonNull(filePart.headers().getContentType()).toString().startsWith("image/");
    }

}
