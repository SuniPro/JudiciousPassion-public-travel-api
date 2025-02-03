package com.suni.judiciouspassion.service;

import com.suni.judiciouspassion.dto.LocationDTO;
import com.suni.judiciouspassion.dto.TasteDTO;
import com.suni.judiciouspassion.dto.TourDTO;
import com.suni.judiciouspassion.entity.personal.SuniLocation;
import com.suni.judiciouspassion.entity.saunter.Saunter;
import com.suni.judiciouspassion.entity.taste.TasteImages;
import com.suni.judiciouspassion.entity.tour.TourImages;
import com.suni.judiciouspassion.repository.*;
import com.suni.judiciouspassion.tools.Parse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonalServiceImpl implements PersonalService {

    private final TasteRepository tasteRepository;
    private final TasteImagesRepository tasteImagesRepository;

    private final TourRepository tourRepository;
    private final TourImagesRepository tourImagesRepository;

    private final SuniLocationRepository locationRepository;

    Parse<Object> parseTool = new Parse<>();

    @Autowired
    public PersonalServiceImpl(TasteRepository tasteRepository, TasteImagesRepository tasteImagesRepository, TourRepository tourRepository, TourImagesRepository tourImagesRepository, SuniLocationRepository locationRepository) {
        this.tasteRepository = tasteRepository;
        this.tasteImagesRepository = tasteImagesRepository;
        this.tourRepository = tourRepository;
        this.tourImagesRepository = tourImagesRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public Flux<TasteDTO> getTasteList(String insertId) {

        return tasteRepository.findAllByInsertId(insertId)
                .flatMap(taste ->
                        tasteImagesRepository.findAllByTasteId(parseTool.parseLong(taste.getId())) // 이미지 조회
                                .map(TasteImages::getImageUrl) // 이미지 URL 추출
                                .collectList() // List<String>으로 변환
                                .map(imageUrls -> TasteDTO.builder()
                                        .id(taste.getId())
                                        .placeName(taste.getPlaceName())
                                        .title(taste.getTitle())
                                        .contents(taste.getContents())
                                        .latitude(taste.getLatitude())
                                        .longitude(taste.getLongitude())
                                        .insertDate(taste.getInsertDate())
                                        .insertId(taste.getInsertId())
                                        .updateDate(taste.getUpdateDate())
                                        .updateId(taste.getUpdateId())
                                        .rate(taste.getRate())
                                        .personalColor(taste.getPersonalColor())
                                        .imageUrls(imageUrls)
                                        .build()
                                )
                );
    }

    @Override
    public Flux<TourDTO> getTourList(String insertId) {
        return tourRepository.findAllByInsertId(insertId).flatMap(tour ->
                tourImagesRepository.findByTourId(tour.getId())
                        .map(TourImages::getImageUrl)
                        .collectList()
                        .map(imageUrls -> TourDTO.builder()
                                .id(tour.getId())
                                .placeName(tour.getPlaceName())
                                .title(tour.getTitle())
                                .contents(tour.getContents())
                                .latitude(tour.getLatitude())
                                .longitude(tour.getLongitude())
                                .insertDate(tour.getInsertDate())
                                .insertId(tour.getInsertId())
                                .updateDate(tour.getUpdateDate())
                                .updateId(tour.getUpdateId())
                                .rate(tour.getRate())
                                .personalColor(tour.getPersonalColor())
                                .imageUrls(imageUrls)
                                .build()
                        )
        );
    }

    @Override
    public Flux<Saunter> getSaunterList(String insertId) {
        return null;
    }

    @Override
    public Mono<SuniLocation> createLocation(LocationDTO locationDTO) {

        SuniLocation suniLocation = SuniLocation.builder().longitude(locationDTO.getLongitude()).latitude(locationDTO.getLatitude()).build();
        return locationRepository.save(suniLocation);
    }

    @Override
    public Mono<LocationDTO> getLocation() {
        return null;
    }
}