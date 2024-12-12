package com.suni.judiciouspassion.configuration;

import com.suni.judiciouspassion.utils.TravelModeReadConverter;
import com.suni.judiciouspassion.utils.TravelModeWriteConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.List;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.suni.judiciouspassion.repository")
public abstract class R2dbcConfig extends AbstractR2dbcConfiguration {

    @Override
    protected List<Object> getCustomConverters() {
        return List.of(new TravelModeReadConverter(), new TravelModeWriteConverter());
    }
}
