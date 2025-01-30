package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.SaunterDTO;
import com.suni.judiciouspassion.entity.saunter.Saunter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SaunterService {

    Mono<Saunter> createSaunter(SaunterDTO saunterDTO);

    Mono<List<SaunterDTO>> getAllSaunters(int page, int size);

    Flux<Saunter> getSaunterById(String insertId);

    Mono<Saunter> updateSaunter(Integer id, SaunterDTO saunterDTO, String userId);

    Mono<Void> deleteSaunter(Integer id);

    Mono<Long> addLike(Long saunterId);

    Mono<Long> minusLike(Long saunterId);
}