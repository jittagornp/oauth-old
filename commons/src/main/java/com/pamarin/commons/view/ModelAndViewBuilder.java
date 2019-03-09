/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.view;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/21
 */
public class ModelAndViewBuilder {

    private String name;

    private Map<String, Object> attributes;

    private Map<String, Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    public ModelAndViewBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ModelAndViewBuilder addAttribute(String attribute, Object value) {
        getAttributes().put(attribute, value);
        return this;
    }

    public ModelAndView build() {
        ModelAndView mav = new ModelAndView(name);
        mav.addAllObjects(getAttributes());
        return mav;
    }
}
