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
@Table("saunter_medias")
public class SaunterMedias {

    @Id
    @Column("id")
    private Long id;

    @Column("saunter_id")
    private Long saunterId;

    @Column("media_url")
    private String mediaUrl;
}


