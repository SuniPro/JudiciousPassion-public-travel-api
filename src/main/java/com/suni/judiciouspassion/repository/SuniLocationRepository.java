package com.suni.judiciouspassion.repository;

import com.suni.judiciouspassion.entity.personal.SuniLocation;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface SuniLocationRepository extends R2dbcRepository<SuniLocation, Long> {
}
