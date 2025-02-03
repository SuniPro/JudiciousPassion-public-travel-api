package com.suni.judiciouspassion.entity.personal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table("suni_location")
public class SuniLocation {

    @Id
    private Long id;

    @Column("longitude")
    private Double longitude;

    @Column("latitude")
    private Double latitude;
}
