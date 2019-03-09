/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import static com.pamarin.commons.util.ClassUtils.isPrivateConstructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/11
 */
public class DateConverterUtilsTest {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private Date createDate(String date) {
        try {
            return new SimpleDateFormat(FORMAT)
                    .parse(date);
        } catch (ParseException ex) {
            return null;
        }
    }

    private LocalDate createLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(FORMAT));
    }

    private LocalDateTime createLocalDateTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(FORMAT));
    }
    
    @Test
    public void shouldBePrivateConstructor() {
        assertTrue(isPrivateConstructor(DateConverterUtils.class));
    }

    @Test
    public void shouldBeNull_whenInputLocalDateIsNull_ofConvert2Date() {
        LocalDate input = null;
        Date output = DateConverterUtils.convert2Date(input);
        Date expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk_whenInputLocalDateIs20170101T010101_ofConvert2Date() {
        LocalDate input = createLocalDate("2017-01-01T01:01:01");
        Date output = DateConverterUtils.convert2Date(input);
        Date expected = createDate("2017-01-01T00:00:00");
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenInputLocalDateTimeIsNull_ofConvert2Date() {
        LocalDateTime input = null;
        Date output = DateConverterUtils.convert2Date(input);
        Date expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk_whenInputLocalDateTimeIs20170101T010101_ofConvert2Date() {
        LocalDateTime input = createLocalDateTime("2017-01-01T01:01:01");
        Date output = DateConverterUtils.convert2Date(input);
        Date expected = createDate("2017-01-01T01:01:01");
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenInputDateIsNull_ofConvert2LocalDate() {
        Date input = null;
        LocalDate output = DateConverterUtils.convert2LocalDate(input);
        LocalDate expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk_whenInputDateIs20170101T010101_ofConvert2LocalDate() {
        Date input = createDate("2017-01-01T01:01:01");
        LocalDate output = DateConverterUtils.convert2LocalDate(input);
        LocalDate expected = createLocalDate("2017-01-01T00:00:00");
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenInputDateIsNull_ofConvert2LocalDateTime() {
        Date input = null;
        LocalDateTime output = DateConverterUtils.convert2LocalDateTime(input);
        LocalDateTime expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk_whenInputDateIs20170101T010101_ofConvert2LocalDateTime() {
        Date input = createDate("2017-01-01T01:01:01");
        LocalDateTime output = DateConverterUtils.convert2LocalDateTime(input);
        LocalDateTime expected = createLocalDateTime("2017-01-01T01:01:01");
        assertThat(output).isEqualTo(expected);
    }
}
