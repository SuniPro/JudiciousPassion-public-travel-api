package com.suni.judiciouspassion.repository;

import com.suni.judiciouspassion.entity.tour.Tour;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TourRepository extends ReactiveCrudRepository<Tour, Integer> {

    public Flux<Tour> findAllByInsertId(String InsertId);
}
