package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.entity.taste.Taste;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TasteService {

    Mono<Taste> createTaste(TasteDTO tasteDto, String userId);

    Flux<Taste> getAllTastes();

    Mono<Taste> getTasteById(Integer id);

    Mono<Taste> updateTaste(Integer id, TasteDTO tasteDto, String userId);

    Mono<Void> deleteTaste(Integer id);
}
