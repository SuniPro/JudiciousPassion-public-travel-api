package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.entity.taste.Taste;
import com.suni.judiciouspassion.entity.taste.TasteImages;
import com.suni.judiciouspassion.repository.TasteRepository;
import com.suni.judiciouspassion.tools.BoundaryFilter;
import io.r2dbc.spi.R2dbcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
public class TasteServiceImpl implements TasteService{

    private final TasteRepository tasteRepository;

    private final BoundaryFilter boundaryFilter;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final ZoneId seoul = ZoneId.of("Asia/Seoul");

    @Autowired
    public TasteServiceImpl(TasteRepository tasteRepository, BoundaryFilter boundaryFilter, R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.tasteRepository = tasteRepository;
        this.boundaryFilter = boundaryFilter;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Mono<Taste> createTaste(TasteDTO tasteDto) {

        Taste taste = Taste.builder()
                .title(tasteDto.getTitle())
                .contents(tasteDto.getContents())
                .placeName(tasteDto.getPlaceName())
                .latitude(tasteDto.getLatitude())
                .longitude(tasteDto.getLongitude())
                .insertDate(ZonedDateTime.now().withZoneSameInstant(seoul).toLocalDateTime())
                .rate(0L)
                .personalColor(tasteDto.getPersonalColor())
                .insertId(tasteDto.getInsertId())
                .build();

        return tasteRepository.save(taste);
    }

    @Override
    public Mono<List<TasteDTO>> getAllTastes(int page, int size) {
        try {
            int offset = page * size;

            return r2dbcEntityTemplate.select(Taste.class)
                    .matching(Query.query(Criteria.empty())
                            .sort(Sort.by(Sort.Order.desc("id")))
                            .limit(size)
                            .offset(offset))
                    .all()
                    .flatMap(taste -> r2dbcEntityTemplate.select(TasteImages.class)
                            .matching(Query.query(Criteria.where("taste_id").is(taste.getId())))
                            .all()
                            .map(TasteImages::getImageUrl)
                            .collectList()
                            .map(imageUrls -> TasteDTO.builder()
                                    .id(taste.getId())
                                    .placeName(taste.getPlaceName())
                                    .title(taste.getTitle())
                                    .contents(taste.getContents())
                                    .latitude(taste.getLatitude())
                                    .longitude(taste.getLongitude())
                                    .insertDate(taste.getInsertDate())
                                    .insertId(taste.getInsertId())
                                    .updateDate(taste.getUpdateDate())
                                    .updateId(taste.getUpdateId())
                                    .rate(taste.getRate())
                                    .personalColor(taste.getPersonalColor())
                                    .imageUrls(imageUrls)
                                    .build()))
                    .collectList();
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
                            .id(tasteDto.getId())
                            .placeName(tasteDto.getPlaceName())
                            .title(tasteDto.getTitle())
                            .contents(tasteDto.getContents())
                            .latitude(tasteDto.getLatitude())
                            .longitude(tasteDto.getLongitude())
                            .updateDate(ZonedDateTime.now().withZoneSameInstant(seoul).toLocalDateTime())
                            .updateId(userId)
                            .build();
                    return tasteRepository.save(updatedTaste);
                });
    }

    @Override
    public Mono<Void> deleteTaste(Integer id) {
        return tasteRepository.deleteById(id);
    }

    @Override
    public Mono<Long> addLike(Integer tasteId) {
        return tasteRepository.findById(tasteId) // Taste 엔티티 조회
                .flatMap(taste -> {
                    // 현재 like 값 증가
                    Long updatedLike = taste.getRate() + 1;

                    // 업데이트된 Taste 엔티티 생성
                    Taste updatedTaste = taste.toBuilder()
                            .rate(updatedLike)
                            .build();

                    // 저장 후 업데이트된 like 값 반환
                    return tasteRepository.save(updatedTaste)
                            .thenReturn(updatedLike);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Taste not found with id: " + tasteId)));
    }

    @Override
    public Mono<Long> minusLike(Integer tasteId) {
        return tasteRepository.findById(tasteId) // Taste 엔티티 조회
                .flatMap(taste -> {
                    // 현재 like 값 증가
                    Long updatedLike = taste.getRate() - 1 <= 0 ? 0 : taste.getRate() - 1;

                    // 업데이트된 Taste 엔티티 생성
                    Taste updatedTaste = taste.toBuilder()
                            .rate(updatedLike)
                            .build();

                    // 저장 후 업데이트된 like 값 반환
                    return tasteRepository.save(updatedTaste)
                            .thenReturn(updatedLike);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Taste not found with id: " + tasteId)));
    }
}
