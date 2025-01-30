package com.suni.judiciouspassion.entity.saunter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table("saunter")
public class Saunter {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String placeName;

    @Column("title")
    private String title;

    @Column("travel_mode")
    private String travelMode;

    @Column("contents")
    private String contents;

    @Column("personal_color")
    private String personalColor;

    @Column("rate")
    private Long rate;

    @Column("youtube_link")
    private String youtubeLink;

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
}
