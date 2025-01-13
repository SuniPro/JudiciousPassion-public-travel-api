package com.suni.judiciouspassion.controller;

import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.dto.TourDTO;
import com.suni.judiciouspassion.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
}
