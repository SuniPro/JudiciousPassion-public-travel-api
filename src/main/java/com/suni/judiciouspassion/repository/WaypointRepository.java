package com.suni.judiciouspassion.repository;

import com.suni.judiciouspassion.entity.saunter.SaunterWaypoint;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface WaypointRepository extends R2dbcRepository<SaunterWaypoint, Long> {

    public Flux<SaunterWaypoint> findAllBySaunterId(Long saunterId);
}
