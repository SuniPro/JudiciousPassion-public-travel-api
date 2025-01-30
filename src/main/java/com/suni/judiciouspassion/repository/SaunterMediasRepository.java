package com.suni.judiciouspassion.repository;

import com.suni.judiciouspassion.entity.saunter.SaunterMedias;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface SaunterMediasRepository extends R2dbcRepository<SaunterMedias, Long> {

    public Flux<SaunterMedias> findAllBySaunterId(Long saunterId);
}
