package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.SaunterDTO;
import com.suni.judiciouspassion.entity.saunter.Saunter;
import com.suni.judiciouspassion.entity.saunter.SaunterMedias;
import com.suni.judiciouspassion.entity.saunter.SaunterWaypoint;
import com.suni.judiciouspassion.entity.saunter.WaypointType;
import com.suni.judiciouspassion.repository.SaunterRepository;
import com.suni.judiciouspassion.repository.WaypointRepository;
import com.suni.judiciouspassion.tools.BoundaryFilter;
import com.suni.judiciouspassion.tools.Parse;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SaunterServiceImpl implements SaunterService {

    private final SaunterRepository saunterRepository;

    private final WaypointRepository waypointRepository;

    private final BoundaryFilter boundaryFilter;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    Parse<Object> parse = new Parse<>();

    private final ZoneId seoul = ZoneId.of("Asia/Seoul");

    @Autowired
    public SaunterServiceImpl(SaunterRepository saunterRepository, SaunterRepository saunterRepository1, WaypointRepository waypointRepository, BoundaryFilter boundaryFilter, R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.saunterRepository = saunterRepository;
        this.waypointRepository = waypointRepository;
        this.boundaryFilter = boundaryFilter;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Mono<Saunter> createSaunter(SaunterDTO saunterDTO) {
        Saunter saunter = Saunter.builder()
                .title(saunterDTO.getTitle())
                .contents(saunterDTO.getContents())
                .placeName(saunterDTO.getPlaceName())
                .insertDate(ZonedDateTime.now().withZoneSameInstant(seoul).toLocalDateTime())
                .rate(0L)
                .personalColor(saunterDTO.getPersonalColor())
                .insertId(saunterDTO.getInsertId())
                .youtubeLink(saunterDTO.getYoutubeLink().isEmpty() ? null : saunterDTO.getYoutubeLink())
                .build();

        return saunterRepository.save(saunter)
                .flatMap(savedSaunter -> {
                    List<SaunterWaypoint> waypoints = Optional.ofNullable(saunterDTO.getWaypoints())
                            .orElse(Collections.emptyList());

                    return Flux.fromIterable(waypoints)
                            .flatMap(waypoint -> {
                                SaunterWaypoint saunterWaypoint = SaunterWaypoint.builder()
                                        .saunterId(savedSaunter.getId())
                                        .latitude(waypoint.getLatitude())
                                        .longitude(waypoint.getLongitude())
                                        .orderIndex(waypoint.getOrderIndex())
                                        .type(WaypointType.valueOf(waypoint.getType().toString().toUpperCase())) // Enum 변환
                                        .build();
                                return waypointRepository.save(saunterWaypoint);
                            })
                            .then(Mono.just(savedSaunter)); // ✅ 모든 Waypoint 저장 후 Saunter 반환
                });
    }


    @Override
    public Mono<List<SaunterDTO>> getAllSaunters(int page, int size) {
        try {
            int offset = page * size;

            return r2dbcEntityTemplate.select(Saunter.class)
                    .matching(Query.query(Criteria.empty())
                            .sort(Sort.by(Sort.Order.desc("id")))
                            .limit(size)
                            .offset(offset))
                    .all()
                    .flatMap(saunter ->
                            // SaunterMedias 조회
                            r2dbcEntityTemplate.select(SaunterMedias.class)
                                    .matching(Query.query(Criteria.where("saunter_id").is(saunter.getId())))
                                    .all()
                                    .map(SaunterMedias::getMediaUrl)
                                    .collectList()
                                    .flatMap(mediaUrls ->
                                            // SaunterWaypoint 조회
                                            r2dbcEntityTemplate.select(SaunterWaypoint.class)
                                                    .matching(Query.query(Criteria.where("saunter_id").is(saunter.getId())))
                                                    .all()
                                                    .collectList()
                                                    .map(waypoints ->
                                                            SaunterDTO.builder()
                                                                    .id(saunter.getId())
                                                                    .placeName(saunter.getPlaceName())
                                                                    .title(saunter.getTitle())
                                                                    .contents(saunter.getContents())
                                                                    .insertDate(saunter.getInsertDate())
                                                                    .insertId(saunter.getInsertId())
                                                                    .updateDate(saunter.getUpdateDate())
                                                                    .updateId(saunter.getUpdateId())
                                                                    .rate(saunter.getRate())
                                                                    .personalColor(saunter.getPersonalColor())
                                                                    .mediaUrls(mediaUrls)
                                                                    .waypoints((ArrayList<SaunterWaypoint>) waypoints)
                                                                    .build()
                                                    )
                                    )
                    )
                    .collectList();
        } catch (R2dbcException e) {
            log.error("Database error occurred: {}", e.getMessage(), e);
            throw new RuntimeException("Database error occurred", e);
        }
    }


    @Override
    public Flux<Saunter> getSaunterById(String insertId) {
        return null;
    }

    @Override
    public Mono<Saunter> updateSaunter(Integer id, SaunterDTO saunterDTO, String userId) {
        return null;
    }

    @Override
    public Mono<Void> deleteSaunter(Integer id) {
        return null;
    }

    @Override
    public Mono<Long> addLike(Long saunterId) {
        return saunterRepository.findById(parse.parseLong(saunterId)) // Taste 엔티티 조회
                .flatMap(saunter -> {
                    // 현재 like 값 증가
                    Long updatedLike = saunter.getRate() + 1;

                    // 업데이트된 Taste 엔티티 생성
                    Saunter updatedSaunter = saunter.toBuilder()
                            .rate(updatedLike)
                            .build();

                    // 저장 후 업데이트된 like 값 반환
                    return saunterRepository.save(updatedSaunter)
                            .thenReturn(updatedLike);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Saunter not found with id: " + saunterId)));
    }

    @Override
    public Mono<Long> minusLike(Long saunterId) {
        return saunterRepository.findById(parse.parseLong(saunterId)) // Taste 엔티티 조회
                .flatMap(saunter -> {
                    // 현재 like 값 증가
                    Long updatedLike = saunter.getRate() - 1 <= 0 ? 0 : saunter.getRate() - 1;

                    // 업데이트된 Taste 엔티티 생성
                    Saunter updatedSaunter = saunter.toBuilder()
                            .rate(updatedLike)
                            .build();

                    // 저장 후 업데이트된 like 값 반환
                    return saunterRepository.save(updatedSaunter)
                            .thenReturn(updatedLike);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Saunter not found with id: " + saunterId)));
    }
}