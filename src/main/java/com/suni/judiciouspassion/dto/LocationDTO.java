package com.suni.judiciouspassion.dto;

import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {

    private Long id;

    private Double longitude;
    private Double latitude;
}
