/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/11
 */
public class DateConverterUtils {

    private DateConverterUtils() {
        
    }

    public static Date convert2Date(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return convert2Date(localDate.atStartOfDay());
    }

    public static Date convert2Date(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime convert2LocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDate convert2LocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return convert2LocalDateTime(date).toLocalDate();
    }

}
