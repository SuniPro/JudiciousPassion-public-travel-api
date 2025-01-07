package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.TourDTO;
import com.suni.judiciouspassion.entity.tour.Tour;
import com.suni.judiciouspassion.entity.tour.TourImages;
import com.suni.judiciouspassion.repository.TourRepository;
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

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    public TourServiceImpl(TourRepository tourRepository, R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.tourRepository = tourRepository;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Mono<Tour> createTour(TourDTO tourDto) {
        Tour tour = Tour.builder()
                .title(tourDto.getTitle())
                .contents(tourDto.getContents())
                .placeName(tourDto.getPlaceName())
                .latitude(tourDto.getLatitude())
                .longitude(tourDto.getLongitude())
                .insertDate(LocalDateTime.now())
                .rate(0L)
                .personalColor(tourDto.getPersonalColor())
                .insertId(tourDto.getInsertId())
                .build();

        return tourRepository.save(tour);
    }

    @Override
    public Mono<List<TourDTO>> getAllTours(int page, int size) {
        try {
            int offset = page * size;

            return r2dbcEntityTemplate.select(Tour.class)
                    .matching(Query.query(Criteria.empty())
                            .sort(Sort.by(Sort.Order.desc("id")))
                            .limit(size)
                            .offset(offset))
                    .all()
                    .flatMap(tour -> r2dbcEntityTemplate.select(TourImages.class)
                            .matching(Query.query(Criteria.where("tour_id").is(tour.getId())))
                            .all()
                            .map(TourImages::getImageUrl)
                            .collectList()
                            .map(imageUrls -> TourDTO.builder()
                                    .id(tour.getId())
                                    .placeName(tour.getPlaceName())
                                    .title(tour.getTitle())
                                    .contents(tour.getContents())
                                    .latitude(tour.getLatitude())
                                    .longitude(tour.getLongitude())
                                    .insertDate(tour.getInsertDate())
                                    .insertId(tour.getInsertId())
                                    .updateDate(tour.getUpdateDate())
                                    .updateId(tour.getUpdateId())
                                    .rate(tour.getRate())
                                    .personalColor(tour.getPersonalColor())
                                    .imageUrls(imageUrls)
                                    .build()))
                    .collectList();
        } catch (R2dbcException e) {
            log.error("Database error occurred: {}", e.getMessage(), e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    @Override
    public Flux<Tour> getTourById(String insertId) {
        return tourRepository.findAllByInsertId(insertId);
    }

    @Override
    public Mono<Tour> updateTour(Integer id, TourDTO tourDto, String userId) {
        return tourRepository.findById(id)
                .flatMap(existingtour -> {
                    Tour updatedTour = existingtour.toBuilder()
                            .id(tourDto.getId())
                            .placeName(tourDto.getPlaceName())
                            .title(tourDto.getTitle())
                            .contents(tourDto.getContents())
                            .latitude(tourDto.getLatitude())
                            .longitude(tourDto.getLongitude())
                            .updateDate(LocalDateTime.now())
                            .updateId(userId)
                            .build();
                    return tourRepository.save(updatedTour);
                });
    }

    @Override
    public Mono<Void> deleteTour(Integer id) {
        return tourRepository.deleteById(id);
    }

    @Override
    public Mono<Long> addLike(Integer tourId) {
        return tourRepository.findById(tourId) // Taste 엔티티 조회
                .flatMap(tour -> {
                    // 현재 like 값 증가
                    Long updatedLike = tour.getRate() + 1;

                    // 업데이트된 Taste 엔티티 생성
                    Tour updatedTaste = tour.toBuilder()
                            .rate(updatedLike)
                            .build();

                    // 저장 후 업데이트된 like 값 반환
                    return tourRepository.save(updatedTaste)
                            .thenReturn(updatedLike);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("tour not found with id: " + tourId)));
    }

    @Override
    public Mono<Long> minusLike(Integer tourId) {
        return tourRepository.findById(tourId) // Taste 엔티티 조회
                .flatMap(tour -> {
                    Long updatedLike = tour.getRate() - 1 <= 0 ? 0 : tour.getRate() - 1;

                    Tour updatedTour = tour.toBuilder()
                            .rate(updatedLike)
                            .build();

                    // 저장 후 업데이트된 like 값 반환
                    return tourRepository.save(updatedTour)
                            .thenReturn(updatedLike);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Taste not found with id: " + tourId)));
    }
}
