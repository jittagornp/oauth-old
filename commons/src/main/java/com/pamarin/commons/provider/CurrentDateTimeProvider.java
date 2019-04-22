/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.provider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author jitta
 */
public interface CurrentDateTimeProvider {

    LocalDateTime provideLocalDateTime();

    LocalDate provideLocalDate();

    Date provideDate();

    long provideTimestamp();

}
