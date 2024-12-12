package com.suni.judiciouspassion.controller;

import com.suni.judiciouspassion.entity.taste.Taste;
import com.suni.judiciouspassion.service.TasteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("taste")
public class TasteController {

    private final TasteService tasteService;

    public TasteController(TasteService tasteService) {
        this.tasteService = tasteService;
    }

    @GetMapping("/all")
    public Flux<Taste> getTasteList() {
        return tasteService.getAllTastes(); // Flux 반환
    }
}
