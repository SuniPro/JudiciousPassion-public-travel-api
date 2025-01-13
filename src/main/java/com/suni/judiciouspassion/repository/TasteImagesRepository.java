package com.suni.judiciouspassion.repository;

import com.suni.judiciouspassion.entity.taste.TasteImages;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TasteImagesRepository extends ReactiveCrudRepository<TasteImages, Integer> {

    public Flux<TasteImages> findAllByTasteId(Integer tasteId);
}
