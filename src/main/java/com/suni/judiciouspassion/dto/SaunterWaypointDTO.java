package com.suni.judiciouspassion.dto;

import com.suni.judiciouspassion.entity.saunter.WaypointType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
@Getter
@Setter
public class SaunterWaypointDTO {

    private Long id;

    private Long saunterId;

    private Double latitude;

    private Double longitude;

    private Integer orderIndex; // 기본값 설정

    private WaypointType type; // START, STOP, END
}
