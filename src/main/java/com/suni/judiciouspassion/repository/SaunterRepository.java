package com.suni.judiciouspassion.repository;

import com.suni.judiciouspassion.entity.saunter.Saunter;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface SaunterRepository extends R2dbcRepository<Saunter, Long> {

    public Flux<Saunter> findAllByInsertId(String insertId);
}
