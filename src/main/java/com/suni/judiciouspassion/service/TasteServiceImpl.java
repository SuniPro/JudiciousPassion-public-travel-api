package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.entity.taste.Taste;
import com.suni.judiciouspassion.repository.TasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class TasteServiceImpl implements TasteService{

    private final TasteRepository tasteRepository;

    @Autowired
    public TasteServiceImpl(TasteRepository tasteRepository) {
        this.tasteRepository = tasteRepository;
    }

    @Override
    public Mono<Taste> createTaste(TasteDTO tasteDto, String userId) {
        Taste taste = Taste.builder()
                .placeName(tasteDto.getPlaceName())
                .title(tasteDto.getTitle())
                .description(tasteDto.getDescription())
                .contents(tasteDto.getContents())
                .latitude(tasteDto.getLatitude())
                .longitude(tasteDto.getLongitude())
                .insertDate(LocalDateTime.now())
                .insertId(userId)
                .build();

        return tasteRepository.save(taste);
    }

    @Override
    public Flux<Taste> getAllTastes() {
        return tasteRepository.findAll();
    }

    @Override
    public Mono<Taste> getTasteById(Integer id) {
        return tasteRepository.findById(id);
    }

    @Override
    public Mono<Taste> updateTaste(Integer id, TasteDTO tasteDto, String userId) {
        return tasteRepository.findById(id)
                .flatMap(existingTaste -> {
                    Taste updatedTaste = existingTaste.toBuilder()
                            .placeName(tasteDto.getPlaceName())
                            .title(tasteDto.getTitle())
                            .description(tasteDto.getDescription())
                            .contents(tasteDto.getContents())
                            .latitude(tasteDto.getLatitude())
                            .longitude(tasteDto.getLongitude())
                            .updateDate(LocalDateTime.now())
                            .updateId(userId)
                            .build();
                    return tasteRepository.save(updatedTaste);
                });
    }

    @Override
    public Mono<Void> deleteTaste(Integer id) {
        return tasteRepository.deleteById(id);
    }
}
