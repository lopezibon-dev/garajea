package com.unieus.garajea.web.balidazioa;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.regex.Pattern;

public final class BalidazioTresnak {

    private static final int MAX_IZENA = 50;
    private static final int MAX_ABIZENAK = 100;
    private static final int MAX_TELEFONOA = 20;
    private static final int MAX_EMAILA = 100;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(".+@.+\\..+");

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);

    private BalidazioTresnak() {
    }

    /* ===========================
       STRING BALIDAZIOA
       =========================== */

    public static String getRequiredString(
            HttpServletRequest req,
            String paramName,
            int maxLen,
            String fieldLabel,
            List<String> erroreak) {

        return getRequiredString(
                req.getParameter(paramName),
                maxLen,
                fieldLabel,
                erroreak
        );
    }

    public static String getOptionalString(
            HttpServletRequest req,
            String paramName,
            int maxLen,
            String fieldLabel,
            List<String> erroreak) {

        return getOptionalString(
                req.getParameter(paramName),
                maxLen,
                fieldLabel,
                erroreak
        );
    }

    public static String getRequiredString(
            String value,
            int maxLen,
            String fieldLabel,
            List<String> erroreak) {

        if (value == null || value.trim().isEmpty()) {
            erroreak.add(fieldLabel + " derrigorrezkoa da");
            return null;
        }

        String trimmed = value.trim();

        if (trimmed.length() > maxLen) {
            erroreak.add(fieldLabel + " gehienez " + maxLen + " karaktere izan ditzake");
            return null;
        }

        return trimmed;
    }

    public static String getOptionalString(
            String value,
            int maxLen,
            String fieldLabel,
            List<String> erroreak) {

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String trimmed = value.trim();

        if (trimmed.length() > maxLen) {
            erroreak.add(fieldLabel + " gehienez " + maxLen + " karaktere izan ditzake");
            return null;
        }

        return trimmed;
    }

    /* ===========================
       EMAIL BALIDAZIOA
       =========================== */

    public static String getRequiredEmail(
            HttpServletRequest req,
            String paramName,
            String fieldLabel,
            List<String> erroreak) {

        String emaila = getRequiredString(
                req,
                paramName,
                MAX_EMAILA,
                fieldLabel,
                erroreak
        );

        if (emaila == null) {
            return null;
        }

        if (!EMAIL_PATTERN.matcher(emaila).matches()) {
            erroreak.add(fieldLabel + " formatu okerra da");
            return null;
        }

        return emaila;
    }

    /* ===========================
       DATA BALIDAZIOA
       =========================== */

    public static LocalDate getRequiredDate(
            HttpServletRequest req,
            String paramName,
            String fieldLabel,
            List<String> erroreak) {

        String value = getRequiredString(req, paramName, 20, fieldLabel, erroreak);
        if (value == null) {
            return null;
        }

        try {
            return LocalDate.parse(value, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            erroreak.add(fieldLabel + " formatu okerra da (yyyy-MM-dd)");
            return null;
        }
    }

    public static LocalDate getOptionalDate(
            HttpServletRequest req,
            String paramName,
            String fieldLabel,
            List<String> erroreak) {

        String value = getOptionalString(req, paramName, 20, fieldLabel, erroreak);
        if (value == null) {
            return null;
        }

        try {
            return LocalDate.parse(value, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            erroreak.add(fieldLabel + " formatu okerra da (yyyy-MM-dd)");
            return null;
        }
    }

    public static LocalTime getRequiredTime(
            HttpServletRequest req,
            String paramName,
            String fieldLabel,
            List<String> erroreak) {

        String value = getRequiredString(req, paramName, 10, fieldLabel, erroreak);
        if (value == null) {
            return null;
        }

        try {
            return LocalTime.parse(value, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            erroreak.add(fieldLabel + " formatu okerra da (HH:mm)");
            return null;
        }
    }

    public static LocalDateTime getRequiredDateTime(
            HttpServletRequest req,
            String paramName,
            String fieldLabel,
            List<String> erroreak) {

        String value = getRequiredString(req, paramName, 25, fieldLabel, erroreak);
        if (value == null) {
            return null;
        }

        try {
            return LocalDateTime.parse(value, DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            erroreak.add(fieldLabel + " formatu okerra da (yyyy-MM-dd HH:mm)");
            return null;
        }
    }
}

