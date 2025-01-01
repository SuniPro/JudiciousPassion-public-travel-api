package com.suni.judiciouspassion.controller;

import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.entity.taste.Taste;
import com.suni.judiciouspassion.service.S3ServiceImpl;
import com.suni.judiciouspassion.service.TasteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("taste")
public class TasteController {

    private final TasteService tasteService;
    private final S3ServiceImpl s3Service;

    public TasteController(TasteService tasteService, S3ServiceImpl s3ServiceImpl) {
        this.tasteService = tasteService;
        this.s3Service = s3ServiceImpl;
    }

    @PostMapping("/create")
    public Mono<Taste> tasteCreate(@RequestBody TasteDTO tasteDTO, String email) {
        return tasteService.createTaste(tasteDTO, email);
    }

    @GetMapping("/get/{insertId}")
    public Flux<Taste> tasteCreate(@PathVariable String insertId) {
        return tasteService.getTasteById(insertId);
    }

    @GetMapping("/all")
    public Flux<Taste> getTasteList() {
        return tasteService.getAllTastes(); // Flux 반환
    }

    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> uploadFile(@RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(filePart -> {
                    String filename = filePart.filename();
                    String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

                    // 확장자 확인
                    List<String> allowedExtensions = List.of("png", "jpg", "jpeg", "gif");
                    if (!allowedExtensions.contains(extension)) {
                        return Mono.error(new IllegalArgumentException("Unsupported file type: " + extension));
                    }

                    // MIME 타입 확인
                    if (!isValidImage(filePart)) {
                        return Mono.error(new IllegalArgumentException("Invalid content type: " + filePart.headers().getContentType()));
                    }

                    // S3 업로드
                    return s3Service.uploadObject(filePart)
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
                filePart.headers().getContentType().toString().startsWith("image/");
    }


}
