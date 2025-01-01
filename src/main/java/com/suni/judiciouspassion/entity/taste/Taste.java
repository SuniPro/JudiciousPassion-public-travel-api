package com.suni.judiciouspassion.entity.taste;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table("taste")
public class Taste {

    @Id
    @Column("id")
    private Integer id;

    @Column("name")
    private String placeName;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("contents")
    private String contents;

    @Column("latitude")
    private double latitude;

    @Column("longitude")
    private double longitude;

    @Column("image_list")
    private List<String> imageList; // 추가된 필드

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
}
