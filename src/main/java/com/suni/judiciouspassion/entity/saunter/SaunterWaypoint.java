package com.suni.judiciouspassion.entity.saunter;

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
@Table("saunter_waypoint")
public class SaunterWaypoint {

    @Id
    @Column("id")
    private Long id;

    @Column("saunter_id") // Saunter와 연관
    private Long saunterId;

    @Column("latitude")
    private Double latitude;

    @Column("longitude")
    private Double longitude;

    @Column("order_index")
    private Integer orderIndex = 0; // 기본값 설정

    @Column("type") // 출발지, 경유지, 목적지 구분
    private WaypointType type; // START, STOP, END
}

