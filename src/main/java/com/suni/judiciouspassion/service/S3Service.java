package com.suni.judiciouspassion.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface S3Service {

    Mono<String> uploadObject(Long id, FilePart filePart, String type);
}
