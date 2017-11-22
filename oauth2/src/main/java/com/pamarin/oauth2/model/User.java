/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/09
 */
@Getter
@Setter
@Builder
public class User {

    private Long id;

    private String name;

}
