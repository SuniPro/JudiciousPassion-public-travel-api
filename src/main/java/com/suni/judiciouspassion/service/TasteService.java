package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.entity.taste.Taste;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TasteService {

    Mono<Taste> createTaste(TasteDTO tasteDto);

    Mono<List<TasteDTO>> getAllTastes(int page, int size);

    Flux<Taste> getTasteById(String insertId);

    Mono<Taste> updateTaste(Integer id, TasteDTO tasteDto, String userId);

    Mono<Void> deleteTaste(Integer id);

    Mono<Long> addLike(Integer tasteId);

    Mono<Long> minusLike(Integer tasteId);
}
