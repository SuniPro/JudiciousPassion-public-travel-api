package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.entity.taste.Taste;
import com.suni.judiciouspassion.repository.TasteRepository;
import com.suni.judiciouspassion.tools.BoundaryFilter;
import io.r2dbc.spi.R2dbcException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TasteServiceImpl implements TasteService{

    private final TasteRepository tasteRepository;

    private final ModelMapper modelMapper;

    private final BoundaryFilter boundaryFilter;

    @Autowired
    public TasteServiceImpl(TasteRepository tasteRepository, ModelMapper modelMapper, BoundaryFilter boundaryFilter) {
        this.tasteRepository = tasteRepository;
        this.modelMapper = modelMapper;
        this.boundaryFilter = boundaryFilter;
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
        try {
            return tasteRepository.findAll()
                    .flatMap(taste ->
                            Mono.justOrEmpty(boundaryFilter.getPlacesWithinRadius(taste, taste.getLatitude(), taste.getLongitude(), 20))
                                    .map(places -> taste.toBuilder().id(taste.getId()).placeName(taste.getPlaceName())
                                                .title(taste.getTitle())
                                                .description(taste.getDescription())
                                                .contents(taste.getContents())
                                                .latitude(taste.getLatitude())
                                                .longitude(taste.getLongitude())
                                                .insertDate(taste.getInsertDate())
                                                .insertId(taste.getInsertId()).build()
                                    )
                    );
        } catch (R2dbcException e) {
            log.error("Database error occurred: {}", e.getMessage(), e);
            throw new RuntimeException("Database error occurred", e);
        }
    }


    @Override
    public Flux<Taste> getTasteById(String insertId) {
        return tasteRepository.findAllByInsertId(insertId);
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
