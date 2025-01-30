package com.suni.judiciouspassion.dto;

import com.suni.judiciouspassion.entity.saunter.SaunterWaypoint;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaunterDTO {

    private Long id;

    private String placeName;

    private String title;

    private String travelMode;

    private String contents;

    private String personalColor;

    private Long rate;

    private List<String> mediaUrls;

    private String youtubeLink;

    private LocalDateTime insertDate;

    private String insertId;

    private LocalDateTime updateDate;

    private String updateId;

    private List<SaunterWaypoint> waypoints;
}