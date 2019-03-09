/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Component
public class JacksonJava8DateTimeConf {

    public static final DateTimeFormatter ISO_DATE_OPTIONAL_TIME = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .optionalStart()
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .toFormatter();

    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder.defaultViewInclusion(false)
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(ISO_DATE_OPTIONAL_TIME))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(ISO_DATE_OPTIONAL_TIME))
                .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE))
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(ISO_DATE_OPTIONAL_TIME))
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

}
