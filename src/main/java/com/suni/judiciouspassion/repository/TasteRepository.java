package com.suni.judiciouspassion.repository;

import com.suni.judiciouspassion.entity.taste.Taste;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TasteRepository extends ReactiveCrudRepository<Taste, Integer> {

    public Flux<Taste> findAllByInsertId(String insertId);
}