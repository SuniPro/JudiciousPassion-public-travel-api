package com.suni.judiciouspassion.entity.tour;

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
@Table("tour_images")
public class TourImages {

    @Id
    @Column("id")
    private Integer id;

    @Column("tour_id")
    private Integer tourId;

    @Column("image_url") // 컬럼 이름 수정
    private String imageUrl; // 필드 이름 수정
}
