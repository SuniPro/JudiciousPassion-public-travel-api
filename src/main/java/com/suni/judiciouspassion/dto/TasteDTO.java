package com.suni.judiciouspassion.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TasteDTO {

    private Integer id;

    private String placeName;

    private String title;

    private String description;

    private String contents;

    private Double latitude;

    private Double longitude;

    private LocalDateTime insertDate;

    private String insertId;

    private LocalDateTime updateDate;

    private String updateId;
}

