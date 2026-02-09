package com.unieus.garajea.desktop.util;

import javax.swing.JFormattedTextField;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class LocalDateFormatter
        extends JFormattedTextField.AbstractFormatter {

    private static final DateTimeFormatter FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text == null || text.isBlank()) {
            return null;
        }

        LocalDate localDate = LocalDate.parse(text, FORMAT);

        return Date.from(
            localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value == null) {
            return "";
        }

        LocalDate localDate;

        if (value instanceof Date date) {
            localDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        } else if (value instanceof Calendar calendar) {
            localDate = calendar.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        } else {
            return "";
        }

        return localDate.format(FORMAT);
    }
}
