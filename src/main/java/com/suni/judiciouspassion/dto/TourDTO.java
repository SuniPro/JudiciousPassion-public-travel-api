package com.suni.judiciouspassion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class TourDTO {

    private Integer id;

    private String placeName;

    private String title;

    private String contents;

    private Double latitude;

    private Double longitude;

    private Long rate;

    private LocalDateTime insertDate;

    private String insertId;

    private LocalDateTime updateDate;

    private String updateId;

    private String personalColor;

    private List<String> imageUrls;
}
