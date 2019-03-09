/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.service.DefaultScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Service
@Transactional
public class DefaultScopeImpl implements DefaultScope {

    @Override
    public String getDefault() {
        return "user:public_profile";
    }

}
