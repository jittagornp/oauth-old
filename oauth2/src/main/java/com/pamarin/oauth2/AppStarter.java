/*
 * Copyright 2017-2019 Pamarin.com
 */

package com.pamarin.oauth2;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/24
 */
@SpringBootApplication
@ComponentScan("com.pamarin")
public class AppStarter {
    
    @PostConstruct 
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Bangkok"));
    }

    public static void main(String[] args) {
        SpringApplication.run(AppStarter.class, args);
    }

}
