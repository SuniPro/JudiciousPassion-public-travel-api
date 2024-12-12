package com.suni.judiciouspassion.entity.saunter;

import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Table("saunter")
public class Saunter {

    @Id
    @Column("id")
    private Integer id;

    @Column("name")
    private String placeName;

    @Column("title")
    private String title;

    @Column("travel_mode")
    private String travelMode;

    @Column("description")
    private String description;

    @Column("contents")
    private String contents;

    @CreatedDate
    @Column("insert_date") // 삽입 시간
    private LocalDateTime insertDate;

    @CreatedBy
    @Column("insert_id") // 삽입 사용자 ID
    private String insertId;

    @LastModifiedDate
    @Column("update_date") // 수정 시간
    private LocalDateTime updateDate;

    @LastModifiedBy
    @Column("update_id") // 수정 사용자 ID
    private String updateId;

    @Column("delete_date") // 삭제 시간
    private LocalDateTime deleteDate;

    @Column("delete_id") // 삭제 사용자 ID
    private String deleteId;

    private List<SaunterWaypoint> waypoints;
}
