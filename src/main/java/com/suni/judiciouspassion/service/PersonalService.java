package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.LocationDTO;
import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.dto.TourDTO;
import com.suni.judiciouspassion.entity.personal.SuniLocation;
import com.suni.judiciouspassion.entity.saunter.Saunter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonalService {

    public Flux<TasteDTO> getTasteList(String insertId);

    public Flux<TourDTO> getTourList(String insertId);

    public Flux<Saunter> getSaunterList(String insertId);

    public Mono<SuniLocation> createLocation(LocationDTO locationDTO);

    public Mono<LocationDTO> getLocation();
}
