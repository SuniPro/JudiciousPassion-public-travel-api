package com.suni.judiciouspassion.controller;

import com.suni.judiciouspassion.dto.LocationDTO;
import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.dto.TourDTO;
import com.suni.judiciouspassion.entity.personal.SuniLocation;
import com.suni.judiciouspassion.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("personal")
public class PersonalController {

    private final PersonalService personalService;

    @Autowired
    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @GetMapping("taste/{insertId}")
    public Flux<TasteDTO> getTasteByInsertId(@PathVariable String insertId) {

        return personalService.getTasteList(insertId);
    }

    @GetMapping("tour/{insertId}")
    public Flux<TourDTO> getTourByInsertId(@PathVariable String insertId) {

        return personalService.getTourList(insertId);
    }

    @PostMapping("location")
    public Mono<SuniLocation> createLocation(@RequestBody LocationDTO locationDTO) {

        return personalService.createLocation(locationDTO);
    }

    @GetMapping("get/location")
    public Mono<SuniLocation> getLocation() {

     return null;
    }
}
