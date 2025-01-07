package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.TourDTO;
import com.suni.judiciouspassion.entity.tour.Tour;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TourService {

    Mono<Tour> createTour(TourDTO tourDto);

    Mono<List<TourDTO>> getAllTours(int page, int size);

    Flux<Tour> getTourById(String id);

    Mono<Tour> updateTour(Integer id, TourDTO tourDto, String userId);

    Mono<Void> deleteTour(Integer id);

    Mono<Long> addLike(Integer tourId);

    Mono<Long> minusLike(Integer tourId);
}
