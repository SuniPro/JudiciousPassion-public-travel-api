package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.dto.TourDTO;
import com.suni.judiciouspassion.entity.saunter.Saunter;
import reactor.core.publisher.Flux;

public interface PersonalService {

    public Flux<TasteDTO> getTasteList(String insertId);

    public Flux<TourDTO> getTourList(String insertId);

    public Flux<Saunter> getSaunterList(String insertId);
}
