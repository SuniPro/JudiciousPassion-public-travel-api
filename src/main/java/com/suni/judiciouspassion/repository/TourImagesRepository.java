package com.suni.judiciouspassion.repository;

import com.suni.judiciouspassion.entity.tour.TourImages;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TourImagesRepository extends ReactiveCrudRepository<TourImages, Integer> {

    public Flux<TourImages> findByTourId(Integer tourId);
}
