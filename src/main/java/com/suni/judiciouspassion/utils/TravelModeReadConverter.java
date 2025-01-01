package com.suni.judiciouspassion.utils;

import com.suni.judiciouspassion.entity.TravelMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

@ReadingConverter
public class TravelModeReadConverter implements Converter<String, TravelMode> {

    @Override
    public TravelMode convert(@NonNull String source) {
        return TravelMode.valueOf(source);
    }
}
