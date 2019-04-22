/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.provider;

import static com.pamarin.commons.util.DateConverterUtils.convert2Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class DefaultCurrentDateTimeProvider implements CurrentDateTimeProvider {

    private final LocalDateTime now;

    public DefaultCurrentDateTimeProvider() {
        this(null);
    }

    public DefaultCurrentDateTimeProvider(LocalDateTime now) {
        this.now = now;
    }

    private LocalDateTime getNow() {
        if (now == null) {
            return LocalDateTime.now();
        }
        return now;
    }

    @Override
    public LocalDateTime provideLocalDateTime() {
        return getNow();
    }

    @Override
    public LocalDate provideLocalDate() {
        return provideLocalDateTime()
                .toLocalDate();
    }

    @Override
    public Date provideDate() {
        return convert2Date(provideLocalDateTime());
    }

    @Override
    public long provideTimestamp() {
        return provideDate().getTime();
    }

}
