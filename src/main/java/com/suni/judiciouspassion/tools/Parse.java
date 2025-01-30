package com.suni.judiciouspassion.tools;

public class Parse<T> {

    /**
     * string 타입이나 int, double 타입 등을 Long 타입으로 변환합니다.
     * */
    public Long parseLong(T param) {
        if (param == null) {
            throw new IllegalArgumentException("Input parameter cannot be null");
        }

        try {
            return Long.parseLong(param instanceof String ? (String) param : param.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input parameter is not a number", e);
        }
    }
}
