package com.suni.judiciouspassion.controller;

import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.entity.taste.Taste;
import com.suni.judiciouspassion.service.S3Service;
import com.suni.judiciouspassion.service.TasteService;
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
@RequestMapping("taste")
public class TasteController {

    private final TasteService tasteService;
    private final S3Service s3Service;

    @Autowired
    public TasteController(TasteService tasteService, S3Service s3Service) {
        this.tasteService = tasteService;
        this.s3Service = s3Service;
    }

    @PostMapping("/create")
    public Mono<Taste> tasteCreate(@RequestBody TasteDTO tasteDTO) {
        return tasteService.createTaste(tasteDTO);
    }

    @GetMapping("/get/{insertId}")
    public Flux<Taste> getTasteById(@PathVariable String insertId) {
        return tasteService.getTasteById(insertId);
    }

    @GetMapping("/list/{page}/{size}")
    public Mono<List<TasteDTO>> getTasteList(@PathVariable int page, @PathVariable int size) {
        return tasteService.getAllTastes(page, size); // Flux 반환
    }

    @PostMapping("/like/add")
    public Mono<Long> addLike(@RequestBody Integer tasteId) {
        return tasteService.addLike(tasteId); // Flux 반환
    }

    @PostMapping("/like/minus")
    public Mono<Long> minusLike(@RequestBody Integer tasteId) {
        return tasteService.minusLike(tasteId); // Flux 반환
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
                    return s3Service.uploadObject(Long.parseLong(id), filePart, type)
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
