package com.suni.judiciouspassion.entity.taste;

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
@Table("taste_images")
public class TasteImages {

    @Id
    @Column("id")
    private Long id;

    @Column("taste_id")
    private Long tasteId;

    @Column("image_url") // 컬럼 이름 수정
    private String imageUrl; // 필드 이름 수정
}
