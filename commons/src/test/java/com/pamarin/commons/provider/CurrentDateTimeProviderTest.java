/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.provider;

import static com.pamarin.commons.util.DateConverterUtils.convert2Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jitta
 */
@Slf4j
public class CurrentDateTimeProviderTest {

    private CurrentDateTimeProvider provider;

    private LocalDateTime now;

    @Before
    public void before() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Bangkok"));
        now = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 0, 0, 0);
        provider = new DefaultCurrentDateTimeProvider(now);
    }

    @Test
    public void shouldBe1546275600000_whenProvideTimestamp() {
        long output = provider.provideTimestamp();
        long expected = 1546275600000L;
        log.debug("timestamp => {}", output);
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNow_whenProvideDate() {
        Date output = provider.provideDate();
        Date expected = convert2Date(now);
        log.debug("date => {}", output);
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNow_whenProvideLocalDate() {
        LocalDate output = provider.provideLocalDate();
        LocalDate expected = now.toLocalDate();
        log.debug("localDate => {}", output);
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNow_whenProvideLocalDateTime() {
        LocalDateTime output = provider.provideLocalDateTime();
        LocalDateTime expected = now;
        log.debug("localDateTime => {}", output);
        assertThat(output).isEqualTo(expected);
    }

}
