package com.suni.judiciouspassion.utils;

import com.suni.judiciouspassion.entity.TravelMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

@WritingConverter
public class TravelModeWriteConverter implements Converter<TravelMode, String> {

    @NonNull
    @Override
    public String convert(TravelMode source) {
        return source.name();
    }
}
