package com.unieus.garajea.web.el;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeFunctions {

    private DateTimeFunctions() {}

    public static String format(LocalDateTime value, String pattern) {
        if (value == null) {
            return "";
        }
        return value.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDate value, String pattern) {
        if (value == null) {
            return "";
        }
        return value.format(DateTimeFormatter.ofPattern(pattern));
    }
}